/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang4.node;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import newlang4.*;

/**
 *
 * @author yuzukiyu
 */
public class StmtList extends Node {
    Node stmt_list;
    Environment env;
    
    private static Set<LexicalType> first = new HashSet<LexicalType>(Arrays.asList(
        LexicalType.IF,
        LexicalType.WHILE,
        LexicalType.DO,
        LexicalType.NAME,
        LexicalType.FOR,
        LexicalType.END,
        LexicalType.NL
    ));
    
    private StmtList(Environment env) {
        super(env);
    }
    
    private StmtList(LexicalUnit first) {
        super(first);
    }
    
    private StmtList(LexicalUnit first, Environment env) {
        this.env = env;
    }
    
    
    static boolean isFirst(LexicalType t) {
        return first.contains(t);
    }
    
    static Node getHandler(LexicalUnit first, Environment env) {
        return new StmtList(first, env);
    }
}
