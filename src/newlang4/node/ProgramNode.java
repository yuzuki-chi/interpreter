package newlang4.node;

import newlang4.*;
import newlang3.LexicalType;

/**
 * Programノード
 * @author y_takaya
 */
public class ProgramNode extends Node {

    private ProgramNode(Environment env) {
        this.env = env;
        this.type = NodeType.PROGRAM;
    }
    
    /**
     * Program -> StmtList
     * @param type
     * @param env
     * @return
     */
    public static Node getHandler(LexicalType type, Environment env) {
        return StmtListNode.getHandler(type, env);
    }
    
    @Override
    public String toString() {
        if (type==NodeType.PROGRAM) return "PROGRAM";
        else return "Node";
    }
}
