package newlang4.node;

import newlang4.*;
import newlang3.*;

import java.util.*;

public class ExprListNode extends Node {
    //<expr_list>  ::=
    //      <expr>
    //      | <expr_list> <COMMA> <expr>
    
    List<Node> list = new ArrayList<>();

    private final static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            //<SUB><LP><NAME><INTVAL><DOUBLEVAL><LITERAL>
            LexicalType.SUB,
            LexicalType.LP,
            LexicalType.NAME,
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL
            //<call_func>-><NAME>
    ));

    private ExprListNode(Environment env) {
        this.env = env;
        this.type = NodeType.EXPR_LIST;
    }

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static ExprListNode getHandler(LexicalType type, Environment env) {
        if (!isMatch(type)) return null;
        else return new ExprListNode(env);
    }

    @Override
    public void parse() throws Exception {
        LexicalType ptype = env.getInput().peek().getType();
        if (ExprNode.isMatch(ptype)) {
            Node handler = ExprNode.getHandler(ptype, env);
            handler.parse();
            list.add(handler);
        } else {
            throw new InternalError("syntax error");
        }

        while (true) {
            if (env.getInput().peek().getType() == LexicalType.COMMA) {
                env.getInput().get();
            } else break;

            ptype = env.getInput().peek().getType();
            if (ExprNode.isMatch(ptype)) {
                Node handler = ExprNode.getHandler(ptype, env);
                handler.parse();
                list.add(handler);
            } else {
                throw new Exception("syntax error");
            }
        }
    }

    public Value getValue(int n) throws Exception {
        return list.get(n).getValue();
    }

    public String toString() {
        String result = "";
        for (int i=0; i < list.size(); i++){
            result += list.get(i);
            if (i != list.size() -1){
                result += ",";
            }
        }
        return result;
    }
}
