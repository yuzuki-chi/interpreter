package newlang4.node;

import newlang3.*;
import newlang4.*;
import java.util.*;

public class SubstNode extends Node {
    //<subst> ::=
    //      <leftvar> <EQ> <expr>
    private String leftVar = "";
    private Node expr;

    private final static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            //<leftvar>->
            LexicalType.NAME
    ));

    public SubstNode(Environment env){
        this.env = env;
        this.type = NodeType.SUBST_STMT;
    }
    
    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (isMatch(type)) return new SubstNode(env);
        else return null;
    }

    @Override
    public void parse() throws Exception {
        LexicalUnit lexicalUnit = env.getInput().peek();
        if (lexicalUnit.getType() == LexicalType.NAME) {
            leftVar = env.getInput().get().getValue().getSValue();
        } else {
            throw new Exception("sytaxerror");
        }

        if (env.getInput().get().getType() != LexicalType.EQ) throw new Exception("Not found EQ");

        LexicalType ptype = env.getInput().peek().getType();
        if (ExprNode.isMatch(ptype)){
            expr = ExprNode.getHandler(ptype, env);
            expr.parse();
        } else {
            throw new Exception("sytax error");
        }
    }

    @Override
    public Value getValue() throws Exception {
        env.getVariable(leftVar).setValue(expr.getValue());
        return null;
    }

    @Override
    public String toString() {
        return leftVar + expr;
    }
}
