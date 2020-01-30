package newlang4.node;

import newlang3.*;
import newlang4.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author y_takaya
 */
public class StmtNode extends Node {
    //<stmt> ::=
    //      <subst>
    //      | <call_sub>
    //      | <FOR><subst><to><INTVAL><NL><stmt_list><NEXT><NAME>
    //      | <END>
    
    private final static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            //<subst>-><leftvar>->
            LexicalType.NAME,
            //<FOR> <END>
            LexicalType.FOR,
            LexicalType.END
    ));

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    /**
     *
     * @param type
     * @param env
     * @return Node or null
     * @throws Exception
     */
    public static Node getHandler(LexicalType type, Environment env) throws Exception{
        if (!isMatch(type)) return null;
        switch (type){
            case NAME:
                LexicalType ptype = env.getInput().peek(2).getType();
                if (ptype == LexicalType.EQ) return SubstNode.getHandler(type, env);
                else if (ExprListNode.isMatch(ptype)) return CallNode.getHandler(type, env);
                else throw new Exception("syntax error");
            case FOR:
                return ForNode.getHandler(type, env);
            case END:
                return EndNode.getHandler(type, env);
            default:
                throw new Exception("syntax error");
        }
    }
}
