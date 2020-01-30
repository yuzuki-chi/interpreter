package newlang4.node;

import newlang3.*;
import newlang4.*;
import java.util.*;

public class LoopNode extends Node {

    Node cond;
    Node process;
    boolean isDo = false;
    boolean isUntil = false;

    static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            //<block>->
            LexicalType.WHILE,
            LexicalType.DO
    ));

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env){
        if (!isMatch(type)) return null;
        return new LoopNode(env);
    }

    private LoopNode(Environment env) {
        this.env = env;
        this.type = NodeType.LOOP_BLOCK;
    }

    @Override
    public void parse() throws Exception {
        LexicalType ptype = env.getInput().peek().getType();
        if (ptype == LexicalType.WHILE) {
            // WHILEを破棄
            env.getInput().get();

            LexicalType type2 = env.getInput().peek().getType();
            if (CondNode.isMatch(type2)) {
                cond = CondNode.getHandler(type2, env);
                cond.parse();
            } else {
                throw new Exception("syntax error");
            }

            // 条件式の直後にNLがない場合
            if (env.getInput().get().getType() != LexicalType.NL) throw new Exception("syntax error");

            type2 = env.getInput().peek().getType();
            if (StmtListNode.isMatch(type2)) {
                process = StmtListNode.getHandler(type2, env);
                process.parse();
            } else {
                throw new Exception("syntax error");
            }

            if (env.getInput().get().getType() != LexicalType.NL) throw new Exception("syntax error");
            if (env.getInput().get().getType() != LexicalType.WEND) throw new Exception("syntax error");

        } else if (ptype == LexicalType.DO) {
            env.getInput().get();

            isDo = true;
            getCond();
            if (env.getInput().get().getType() != LexicalType.NL) throw new Exception("Invalid constitution of 'DO'. Not found NewLine before processing.");

            LexicalType type2 = env.getInput().peek().getType();
            if (StmtListNode.isMatch(type2)) {
                process = StmtListNode.getHandler(type2, env);
                process.parse();
            } else {
                throw new Exception("syntax error");
            }

            // 処理内容の直後にNLがない場合
            if (env.getInput().get().getType() != LexicalType.NL) throw new Exception("syntax error");

            // 処理内容の後にLOOPがない場合
            if (env.getInput().get().getType() != LexicalType.LOOP) throw new Exception("syntax error");

            // WHILEまたはUNTILで始まる条件文が見つからない場合
            if (cond == null && !getCond()) throw new Exception("syntax error");
        } else {
            throw new Exception("syntax error");
        }

        if (env.getInput().get().getType() != LexicalType.NL) {
            throw new Exception("syntax error");
        }
    }

    private boolean getCond() throws Exception {
        switch (env.getInput().peek().getType()){
            case UNTIL:
                isUntil = true;
            case WHILE:
                // WHILEを破棄
                env.getInput().get();

                LexicalType type = env.getInput().peek().getType();
                if (CondNode.isMatch(type)) {
                    cond = CondNode.getHandler(type, env);
                    cond.parse();
                } else {
                    // WHILEもしくはUNTILの後に条件式がない場合
                    throw new Exception("syntax error");
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public Value getValue() throws Exception {
        boolean flag = false;
        if (isDo) {
            process.getValue();
        }
        
        while (true) {
            flag = (cond.getValue().getBValue() && !isUntil) ||
                   (!cond.getValue().getBValue() && isUntil);
            if (!flag) {
                return null;
            }
            process.getValue();
        }
    }

    @Override
    public String toString() {
        String ret = "";
        // ex: LOOP[<[1]:[a]][PRINT[Hello]a[-[a,1]]]
        ret += "LOOP[";
        ret += cond;
        ret += "][" + process.toString() + "]";
        return ret;
    }
}
