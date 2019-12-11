/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang4.node;
import newlang4.*;

import java.util.*;

/**
 *
 * @author yuzukiyu
 */
public class Program extends Node {
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
    
    private Program(Environment env) {
        super(env);
    }
    
    private Program(LexicalUnit first, Environment env) {
        this.env = env;
    }
    
    
    static boolean isFirst(LexicalType t) {
        return first.contains(t);
    }
    
    static Node getHandler(Environment env, LexicalUnit first, ) {
        return new Program(first, env);
    }
    
    public static boolean isMatch(LexicalType type) {
        return first.contains(type);
    }
    
    public boolean parse() {
        LexicalUnit first = env.getInput().get();
        if (StmtList.isFirst(first)) {
            stmt_list = StmtList.getHandler(first, env);
            return stmt_list.parse();
        }
        return false;
    }
}
