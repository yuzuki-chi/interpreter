//今回の課題では使用しない
package newlang4.node;

import newlang3.*;
import newlang4.*;

import java.util.*;

public class ForNode extends Node {
    Node init;
    Node max;
    Node process;
    String step;

    private final static Set<LexicalType> first = new HashSet<>(Arrays.asList(
            //<FOR>
            LexicalType.FOR
    ));

    public ForNode(Environment env) {
        this.env = env;
        this.type = NodeType.FOR_STMT;
    }
        
    public static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (!isMatch(type)) return null;
        return new ForNode(env);
    }

    @Override
    public void parse() throws Exception {
        env.getInput().get();

        LexicalType type = env.getInput().peek().getType();
        if (SubstNode.isMatch(type)) {
            init = SubstNode.getHandler(type, env);
            init.parse();
        } else {
            throw new Exception("syntax error");
        }

        // TO
        if (env.getInput().get().getType()!=LexicalType.TO) throw new Exception("syntax error : TO"); 

        // max
        if (env.getInput().peek().getType()==LexicalType.INTVAL){
            LexicalUnit lu = env.getInput().get();
            max = ConstNode.getHandler(lu.getType(), env, lu.getValue());
        } else {
            throw new Exception("syntax error");
        }

        // NL
        if (env.getInput().get().getType() != LexicalType.NL) throw new Exception("syntax error");

        // 処理部分の内容
        type = env.getInput().peek().getType();
        if (StmtListNode.isMatch(type)) {
            process = StmtListNode.getHandler(type, env);
            process.parse();
        } else {
            throw new Exception("syntax error");
        }
        
        // NEXT
        if (env.getInput().get().getType() != LexicalType.NEXT) throw new Exception("syntax error");

        // 更新対象の確認
        if (env.getInput().peek().getType() == LexicalType.NAME) {
            step = env.getInput().get().getValue().getSValue();
        } else {
            throw new Exception("syntax error");
        }

    }

    @Override
    public Value getValue() throws Exception {
        init.getValue();
        while (true) {
            if (env.getVariable(step).getValue().getIValue() > max.getValue().getIValue()) {
                return null;
            }
            process.getValue();
            env.getVariable(step).setValue(
                    new ExprNode(env.getVariable(step), ConstNode.getHandler(LexicalType.INTVAL, env, new ValueImpl(1)),
                            (LexicalType.ADD)).getValue()
            );
        }
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "[" + init + ":" + max + "][";
        ret += process.toString();
        ret += "]" + step;
        return ret;
    }
}
