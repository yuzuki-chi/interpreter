package newlang4.node;

import newlang3.*;
import newlang4.*;

import java.util.*;

public class IfBlockNode extends Node {

    private Node cond;
    private Node process;
    private Node elseProcess;

    private boolean isELSEIF = false;

    private List<IfBlockNode> followIfBlock = new ArrayList<>();

    static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            LexicalType.IF
    ));

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env){
        if (!isMatch(type)) return null;
        return new IfBlockNode(env);
    }

    private IfBlockNode(Environment env) {
        this.env = env;
        this.type = NodeType.IF_BLOCK;
    }

    public void parse() throws Exception {
        // ELSEIFならばtrueへ
        LexicalType ptype = env.getInput().peek().getType();
        if (ptype == LexicalType.ELSEIF){
            isELSEIF = true;
            env.getInput().get();
        } else if (ptype == LexicalType.IF) {
            env.getInput().get();
        } else {
            throw new InternalError("syntax error");
        }


        // 条件文の確認
        ptype = env.getInput().peek().getType();
        if (CondNode.isMatch(ptype)){
            cond = CondNode.getHandler(ptype, env);
            cond.parse();
        } else {
            throw new Exception("syntax error");
        }

        // THENの確認
        if (env.getInput().get().getType() != LexicalType.THEN) throw new Exception("syntax error");


        ptype = env.getInput().peek().getType();
        if (ptype == LexicalType.NL) {
            // NL
            env.getInput().get();

            LexicalType ltype = env.getInput().peek().getType();
            if (StmtListNode.isMatch(ltype)){
                process = StmtListNode.getHandler(ltype, env);
                process.parse();
            } else {
                throw new Exception("syntax error");
            }

            // NL
            if (env.getInput().get().getType() != LexicalType.NL) throw new Exception("syntax error");

            // ELSEIF or ELSE
            ltype = env.getInput().peek().getType();
            while (!isELSEIF && ltype == LexicalType.ELSEIF) {
                // 別のIFBlockNodeを生成
                IfBlockNode elseIfBlock = (IfBlockNode) IfBlockNode.getHandler(LexicalType.IF, env);
                elseIfBlock.parse();
                followIfBlock.add(elseIfBlock);

                ltype = env.getInput().peek().getType();
            }

            if (!isELSEIF && ltype == LexicalType.ELSE) {
                // ELSEを破棄
                env.getInput().get();

                ltype = env.getInput().peek().getType();
                if (env.getInput().get().getType() == LexicalType.NL){

                    LexicalType type3 = env.getInput().peek().getType();
                    if (StmtListNode.isMatch(type3)){
                        elseProcess = StmtListNode.getHandler(type3, env);
                        elseProcess.parse();
                    } else {
                        throw new Exception("syntax error");
                    }

                    if (env.getInput().get().getType() != LexicalType.NL){
                        throw new Exception("syntax error");
                    }

                } else {
                    throw new Exception("syntax error");
                }
            }

            // ENDIF
            if (!isELSEIF){
                if (env.getInput().get().getType() != LexicalType.ENDIF) throw new Exception("syntax error");
            }

        } else if (StmtNode.isMatch(ptype)) {
            process = StmtNode.getHandler(ptype, env);
            process.parse();

            if (env.getInput().peek().getType() == LexicalType.ELSE) {
                env.getInput().get();

                LexicalType type2 = env.getInput().peek().getType();
                if (StmtNode.isMatch(type2)) {
                    elseProcess = StmtNode.getHandler(type2, env);
                    elseProcess.parse();
                } else {
                    throw new Exception("syntax error");
                }
            }
        } else {
            throw new Exception("syntax error");
        }

        if (!isELSEIF){
            // 終端（ENDIFなど）の後にNLが必要
            if (env.getInput().get().getType() != LexicalType.NL) throw new Exception("syntax error");
        }
    }

    @Override
    public Value getValue() throws Exception {
        if (cond.getValue().getBValue()) {
            process.getValue();
        } else {
            // ELSEIF
            for (IfBlockNode elseIfNode : followIfBlock) {
                if (elseIfNode.cond.getValue().getBValue()) {
                    elseIfNode.getValue();
                    return null;
                }
            }

            // ELSE
            if (elseProcess != null) {
                elseProcess.getValue();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String res = "";
        String dent = "";

        res += cond;

        if (process.getType() != NodeType.STMT_LIST) res += dent;

        res += process.toString();

        if (followIfBlock.size() > 0) {
            for (Node elseifblock : followIfBlock) {
                res += dent;
                res += "][";
                res += elseifblock.toString();
            }
        }

        if (elseProcess != null) {
            res += dent;
            res += "][";
            if (elseProcess.getType() != NodeType.STMT_LIST) res += dent;
            res += elseProcess.toString();
        }
        res += dent;
        if (!isELSEIF) res += "]";
        return res;
    }
}
