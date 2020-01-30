package newlang4.node;

import newlang3.LexicalType;
import newlang4.*;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

public class BlockNode extends Node {
    //<block> ::=
    //      <if_prefix><stmt><NL>
    //      | <if_prefix><stmt><ELSE><stmt><NL>
    //      | <if_prefix><NL><stmt_list><else_block><ENDIF><NL>
    //      | <WHILE><cond><NL><stmt_list><NL><WEND>
    //      | <DO><WHILE><cond><NL><stmt_list><LOOP><NL>
    //      | <DO><UNTIL><cond><NL><stmt_list><LOOP><NL>
    //      | <DO><NL><stmt_list><LOOP><WHILE><cond><NL>
    //      | <DO><NL><stmt_list><LOOP><UNTIL><cond><NL>
    
    static Set<LexicalType> first = new HashSet<>(Arrays.asList(
            //<if_prefix>->
            LexicalType.IF,
            //<WHILE> <DO>
            LexicalType.WHILE,
            LexicalType.DO
    ));

    public static boolean isMatch(LexicalType type){
        return first.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) throws Exception{
        if (IfBlockNode.isMatch(type)) return IfBlockNode.getHandler(type, env);
        else if (LoopNode.isMatch(type)) return LoopNode.getHandler(type, env);
        else throw new Exception("syntax error");
    }
    
    public String toString() {
        return "block";
    }
}
