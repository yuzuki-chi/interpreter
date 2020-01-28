package newlang3;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LexicalAnalyzerImpl implements LexicalAnalyzer{
    PushbackReader reader; //文字をストリームにプッシュバックできる文字ストリームリーダー
    
    static HashMap<String, LexicalUnit> RESERVED_WORD_MAP = new HashMap<>();
    static HashMap<String, LexicalUnit> SYMBOL_MAP = new HashMap<>();
    
    //RESERVED_WORD_MAP語録追加
    static { 
        RESERVED_WORD_MAP.put("IF", new LexicalUnit(LexicalType.IF));
        RESERVED_WORD_MAP.put("THEN", new LexicalUnit(LexicalType.THEN));
        RESERVED_WORD_MAP.put("ELSE", new LexicalUnit(LexicalType.ELSE));
        RESERVED_WORD_MAP.put("ELSEIF", new LexicalUnit(LexicalType.ELSEIF));
        RESERVED_WORD_MAP.put("ENDIF", new LexicalUnit(LexicalType.ENDIF));
        RESERVED_WORD_MAP.put("FOR", new LexicalUnit(LexicalType.FOR));
        RESERVED_WORD_MAP.put("FORALL", new LexicalUnit(LexicalType.FORALL));
        RESERVED_WORD_MAP.put("NEXT", new LexicalUnit(LexicalType.NEXT));
        RESERVED_WORD_MAP.put("SUB", new LexicalUnit(LexicalType.FUNC));
        RESERVED_WORD_MAP.put("DIM", new LexicalUnit(LexicalType.DIM));
        RESERVED_WORD_MAP.put("AS", new LexicalUnit(LexicalType.AS));
        RESERVED_WORD_MAP.put("END", new LexicalUnit(LexicalType.END));
        RESERVED_WORD_MAP.put("WHILE", new LexicalUnit(LexicalType.WHILE));
        RESERVED_WORD_MAP.put("DO", new LexicalUnit(LexicalType.DO));
        RESERVED_WORD_MAP.put("UNTIL", new LexicalUnit(LexicalType.UNTIL));
        RESERVED_WORD_MAP.put("LOOP", new LexicalUnit(LexicalType.LOOP));
        RESERVED_WORD_MAP.put("TO", new LexicalUnit(LexicalType.TO));
        RESERVED_WORD_MAP.put("WEND", new LexicalUnit(LexicalType.WEND));
        //これらに該当しないものは"NAME"
        
        SYMBOL_MAP.put("=", new LexicalUnit(LexicalType.EQ));
        SYMBOL_MAP.put("<", new LexicalUnit(LexicalType.LT));
        SYMBOL_MAP.put(">", new LexicalUnit(LexicalType.GT));
        SYMBOL_MAP.put("<=", new LexicalUnit(LexicalType.LE));
        SYMBOL_MAP.put("=<", new LexicalUnit(LexicalType.LE));
        SYMBOL_MAP.put(">=", new LexicalUnit(LexicalType.GE));
        SYMBOL_MAP.put("=>", new LexicalUnit(LexicalType.GE));
        SYMBOL_MAP.put("<>", new LexicalUnit(LexicalType.NE));
        SYMBOL_MAP.put(".", new LexicalUnit(LexicalType.DOT));
        SYMBOL_MAP.put("+", new LexicalUnit(LexicalType.ADD));
        SYMBOL_MAP.put("-", new LexicalUnit(LexicalType.SUB));
        SYMBOL_MAP.put("*", new LexicalUnit(LexicalType.MUL));
        SYMBOL_MAP.put("/", new LexicalUnit(LexicalType.DIV));
        SYMBOL_MAP.put(")", new LexicalUnit(LexicalType.RP));
        SYMBOL_MAP.put("(", new LexicalUnit(LexicalType.LP));
        SYMBOL_MAP.put(",", new LexicalUnit(LexicalType.COMMA));
        SYMBOL_MAP.put("\n", new LexicalUnit(LexicalType.NL));
    }
    
    //解析用の文字を格納するList lexicalUnitList
    List<LexicalUnit> lexicalUnitList = new ArrayList<>();
    int line = 1;

    public LexicalAnalyzerImpl(FileInputStream fis) throws Exception {
        //Reader : 文字ストリームを読み込むための抽象クラス
        Reader inputsr = new InputStreamReader(fis);
        this.reader = new PushbackReader(inputsr);
    }

    @Override
    public LexicalUnit get() throws Exception {
        
        //解析
        while(true) {
            int ci = reader.read(); // 文字をintとして読む
            char c = (char) ci;     // int文字をcharへ変換
            reader.unread(ci);      // 読んだciを戻す
            
            // EOF
            if (ci < 0) {
                return new LexicalUnit(LexicalType.EOF);
                
            } else {
                // space, tab -> 無視
                if ((c == ' ') || (c == '\t')) {
                //System.out.println("SPACE => continue");
                    reader.read();
                    continue;
                }

                // 英字 -> getString();
                if ((c >= 'a' && c<= 'z') || (c >= 'A' && c <= 'Z')) 
                    return getString();

                // 数字 -> getNumber();
                if (c >= '0' && c <= '9') 
                    return getNumber();

                // リテラル（文字列定数） -> getLiteral();
                if (c == '"') 
                    return getLiteral();

                // シンボル -> getSymbol();
                if (SYMBOL_MAP.containsKey(String.valueOf(c))) 
                    return getSymbol();
                // map.containsKey : Mapキー検索

                throw new InternalError("ERROR : Inputted unknown code");
            }
        }
    }

    //英字処理
    private LexicalUnit getString() throws Exception {
        String target = ""; // targetにStringが追加されていく

        while(true) {
            int ci = reader.read();
            char c = (char) ci;     // 1文字を読み出す

            if (ci < 0) break; // EOFがきたらString終了
  
            if ((c >= 'a' && c<= 'z') ||  (c >= 'A' && c <= 'Z') || (c >= '0'  && c <= '9')) {
                target += c; //文字の追加
                continue;
            }
            reader.unread(ci);  // Stringを超えた1文字をreadしたためunread
            break;
        }
        // この時点でtarget(1語句)が完成している

        //RESERVED_WORD_MAPのキーからtargetを検索
        if (RESERVED_WORD_MAP.containsKey(target)) { //予約語
            return RESERVED_WORD_MAP.get(target); //valueを返す
        } else {
            // 予約語ではなかったため、LexicalTypeにNAMEをもつUnitを作成.
            ValueImpl valueImpl = new ValueImpl(target, ValueType.STRING);
            return new LexicalUnit(LexicalType.NAME, valueImpl);
        }
    }
    
    //数字処理
    private LexicalUnit getNumber() throws Exception {
        String target = "";
        
        //小数点の判断Boolean
        Boolean decimalFlag = false;

        while(true) {
            int ci = reader.read();

            if (ci < 0) break; //EOF
            char c = (char) ci;

            if (c >= '0' && c <= '9') {
                target += c; //文字の追加
                continue;
            } else if (c == '.' && !decimalFlag) { //少数きた
                decimalFlag = true;
                target += c; //文字の追加
                continue;
            } else if (c == '.' && decimalFlag) { //.がふたつ以上
                throw new Exception("ERROR : Too many dots");
            }
            
            //この時点で数字が完成
            reader.unread(ci); //数字が終わって次の1文字を読んでしまっているのでunread
            break;
        }
        //if (!decimalFlag) System.out.println("INT: " + target);
        //else System.out.println("DOBLE: "+ target);
        
        //少数->DOUBLE  それ以外->INTEGER
        if (decimalFlag) return new LexicalUnit(LexicalType.DOUBLEVAL, new ValueImpl(target, ValueType.DOUBLE));
        else return new LexicalUnit(LexicalType.INTVAL, new ValueImpl(target, ValueType.INTEGER));
    }

    //シンボル処理
    private LexicalUnit getSymbol() throws Exception {
        String target = "";

        while(true) {
            int ci = reader.read();
            char c = (char) ci;

            if (ci < 0) return SYMBOL_MAP.get(target); //EOF
            
            //改行処理
            if (String.valueOf(c).equals("\n")) {
                this.line++;
                //System.out.println("NL");
            }

            if (SYMBOL_MAP.containsKey(target + c)) { //SYMBOL_MAPのkey検索
                target += c;
            } else {
                // cに余分な文字が入るとここにくる
                reader.unread(ci); // SYMBOLが終わり, 次の1文字を読んだためunread
                //if (target.equals("\n")) System.out.println("\\n \t=> Symbol");
                //if (!target.equals("\n")) System.out.println(target + " => Symbol");
                return SYMBOL_MAP.get(target);
            }
        }
    }
    
    //リテラル処理
    private LexicalUnit getLiteral() throws Exception {
        String target = "";
        reader.read(); //ひとまず1文字目である"を飛ばす
        
        //リテラルの中身解析
        while(true) {
            int ci = reader.read();
            char c = (char) ci;

            if (ci < 0) break; //EOF
            
            if (c != '"') { //次の「閉じ"」がくるまでひたすらtargetにcを入れ続ける
                target += c;
                continue;
            }
            //ここの時点でtargetは完成している. 
            //System.out.println(target + " \t=> Literal");
            return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(target, ValueType.STRING));
        }
        // "が閉じずに最後まで読んでしまった.
        throw new Exception("ERROR: Not closing double quote");
    }
    
    //例外処理
    @Override
    public boolean expect(LexicalType type) throws Exception { return true; }

    @Override
    public void unget(LexicalUnit token) {
        if (token.getType() == LexicalType.NL) line--;
        lexicalUnitList.add(token);
    }

    @Override
    public int getLine() { return line; }
}