/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang3;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author yuzukiyu
 */
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

    public LexicalUnit get() throws Exception {
        
        //解析
        while(true) {
            int ci = reader.read();
            char c = (char) ci;
            //System.out.println(ci + " is " + c);
            reader.unread(ci);
            
            // EOF
            if (ci < 0) {
                return new LexicalUnit(LexicalType.EOF);
                
            } else {

                // space, tab -> 無視
                if ((c == ' ') || (c == '\t')) {
                    //System.out.println("=>space");
                    reader.read();
                    continue;
                }

                // 英字
                if ((c >= 'a' && c<= 'z') || (c >= 'A' && c <= 'Z')) return getString();

                // 数字
                if (c >= '0' && c <= '9') return getNumber();

                // リテラル（文字列定数）
                if (c == '"') return getLiteral();

                // シンボル
                if (SYMBOL_MAP.containsKey(String.valueOf(c))) return getSymbol();
                // map.containsKey : Mapキー検索

                throw new InternalError("ERROR : unknown code");
            }
        }
    }

    //英字処理
    private LexicalUnit getString() throws Exception {
        String target = "";

        while(true) {
            //System.out.println(target);
            int ci = reader.read();
            char c = (char) ci;

            if (ci < 0) break; //EOF
  
            if ((c >= 'a' && c<= 'z') ||  (c >= 'A' && c <= 'Z') || (c >= '0'  && c <= '9')) {
                target += c; //文字の追加
                //System.out.println(target);
                continue;
            }
            reader.unread(ci);
            break;
        }

        //RESERVED_WORD_MAPのキーからtargetを検索, なければ新しいLexicalUnitを返す
        if (RESERVED_WORD_MAP.containsKey(target)) return RESERVED_WORD_MAP.get(target);
        else return new LexicalUnit(LexicalType.NAME, new ValueImpl(target, ValueType.STRING));
    }
    
    //数字処理
    private LexicalUnit getNumber() throws Exception {
        String target = "";
        //小数点の判断Boolean
        Boolean decimalFlag = false;

        while(true) {
            //System.out.println(target);
            int ci = reader.read();

            if (ci < 0) break;
            char c = (char) ci;

            if (c >= '0' && c <= '9') {
                target += c; //文字の追加
                continue;
            } else if (c == '.' && !decimalFlag) { //少数きた
                decimalFlag = true;
                target += c; //文字の追加
                continue;
            } else if (c == '.' && decimalFlag) { //.がふたつ以上
                throw new Exception("EROOR : too many dots");
            }
            reader.unread(ci);
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
            //System.out.println(target);
            int ci = reader.read();

            if (ci < 0) return SYMBOL_MAP.get(target);
            char c = (char) ci;
            
            //改行処理
            if (String.valueOf(c).equals("\n")) {
                this.line++;
                //System.out.println("NL");
            }

            if (SYMBOL_MAP.containsKey(target + c)) {
                target += c;
            } else {
                reader.unread(ci);
                //if (target.equals("\n")) System.out.println("\\n \t=> Symbol");
                //if (!target.equals("\n")) System.out.println(target + " => Symbol");
                return SYMBOL_MAP.get(target);
            }
        }
    }
    
    //リテラル処理
    private LexicalUnit getLiteral() throws Exception {
        String target = "";
        //ここで " とばせ
        reader.read();
        
        //リテラルの中身解析
        while(true) {
            int ci = reader.read();
            char c = (char) ci;

            if (ci < 0) break; //EOF
            
            //" までぶっとんでくぜ
            if (c != '"') {
                target += c;
                continue;
            }
            //System.out.println(target + " \t=> Literal");
            return new LexicalUnit(LexicalType.LITERAL, new ValueImpl(target, ValueType.STRING));
        }
        throw new Exception("You don't closing double quote");
    }
    
    //例外処理
    public boolean expect(LexicalType type) throws Exception { return true; }

    public void unget(LexicalUnit token) {
        if (token.getType() == LexicalType.NL) line--;
        lexicalUnitList.add(token);
    }

    public int getLine() { return line; }

}