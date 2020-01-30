package newlang4.node;

import newlang4.NodeType;
import newlang4.Node;
import newlang4.Environment;
import newlang3.Value;
import newlang3.LexicalType;
import newlang3.LexicalUnit;

import java.util.*;

/**
 *
 * @author y_takaya
 */
public class StmtListNode extends Node {
    //<stmt_list> ::=
    //      <stmt>
    //      | <stmt_list><NL><stmt>
    //      | <block>
    //      | <stmt_list><block>

    List<Node> list = new ArrayList<>();
    static Set<LexicalType> first = new HashSet<>(Arrays.asList(
        //<stmt>->
        LexicalType.NAME,
        LexicalType.FOR,
        LexicalType.END,
        //<block>->
        LexicalType.IF,
        LexicalType.WHILE,
        LexicalType.DO
    ));

    private StmtListNode(Environment env){
        this.env = env;
        this.type = NodeType.STMT_LIST;
    }

    /**
     * StmtList -> *end*
     * @param LexicalType
     * @param Environment
     * @return
     */
    public static Node getHandler(LexicalType type, Environment env){
        if (!isMatch(type)) return null; // first集合に該当するtypeがなければnullを返す(おそらく通ることはない)
        return new StmtListNode(env); // あれば新しいenvを作成
    }

    public static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    /**
     * 実際に解析を行う
     * @throws Exception
     */
    @Override
    public void parse() throws Exception {
        while (true) {
            try {
                //env(変数) -> peek(return lexicalUnit) -> getType
                while (env.getInput().peek(1).getType() == LexicalType.NL &&
                       isMatch(env.getInput().peek(2).getType())) {
                    // NLは飛ばす
                    env.getInput().get();
                }

                Node handler;
                LexicalType lexicalType = env.getInput().peek(1).getType();
                
                // stmt
                if (StmtNode.isMatch(lexicalType)) {
                    handler = StmtNode.getHandler(lexicalType, env);
                    handler.parse();
                    list.add(handler);
                // block
                } else if (BlockNode.isMatch(lexicalType)) {
                    handler = BlockNode.getHandler(lexicalType, env);
                    handler.parse();
                    list.add(handler);
                // それ以外
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println(e.fillInStackTrace());
                LexicalUnit lexicalUnit = env.getInput().get();
                // ひとまず終わりが来るまでget()
                while ( lexicalUnit.getType() != LexicalType.NL && 
                        lexicalUnit.getType() != LexicalType.EOF ) {
                    lexicalUnit = env.getInput().get();
                }
            }
        }
    }

    @Override
    public Value getValue() throws Exception {
        for (Node node : list) {
            node.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        //ex: a
        String str = "";
        for (int i = 0; i< list.size(); i++) {
            str += list.get(i).toString();
        }
        return str;
    }
}
