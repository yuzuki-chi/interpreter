package newlang4.node;

import newlang4.*;
import newlang3.*;
import java.util.*;

public class CondNode extends Node {

    // <cond> ::=
    //      <expr><EQ><expr>
    //      |<expr><GT><expr>
    //      |<expr><LT><expr>
    //      |<expr><GE><expr>
    //      |<expr><LE><expr>
    //      |<expr><NE><expr>
    
    Node left;
    LexicalType operator;
    Node right;
    String op;

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

    private final static Set<LexicalType> OPERATORS = new HashSet<>(Arrays.asList(
            LexicalType.EQ,
            LexicalType.GT,
            LexicalType.LT,
            LexicalType.GE,
            LexicalType.LE,
            LexicalType.NE
    ));

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static Node getHandler (LexicalType type, Environment env) {
        if (!isMatch(type)) return null;
        else return new CondNode(env);
    }

    private CondNode(Environment env) {
        this.env = env;
        this.type = NodeType.COND;
    }

    @Override
    public void parse() throws Exception {
        LexicalType ptype = env.getInput().peek().getType();
        if (ExprNode.isMatch(ptype)) {
            left = ExprNode.getHandler(ptype, env);
            left.parse();
        } else {
            throw new Exception("syntax error");
        }

        if (OPERATORS.contains(env.getInput().peek().getType())) {
            operator = env.getInput().get().getType();
        } else {
            throw new Exception("syntax error");
        }

        ptype = env.getInput().peek().getType();
        if (ExprNode.isMatch(ptype)) {
            right = ExprNode.getHandler(ptype, env);
            right.parse();
        } else {
            throw new Exception("syntax error");
        }
    }

    @Override
    public Value getValue() throws Exception {
        Value leftValue = left.getValue();
        Value rightValue = right.getValue();

        boolean res = false;
        switch (operator) {
            case EQ:
                res = leftValue.getSValue().equals(rightValue.getSValue());
                op = "=";
                break;
            case LT:
                res = leftValue.getDValue() < rightValue.getDValue();
                op = "<";
                break;
            case GT:
                res = leftValue.getDValue() > rightValue.getDValue();
                op = ">";
                break;
            case GE:
                res = leftValue.getDValue() >= rightValue.getDValue();
                op = ">=";
                break;
            case LE:
                res = leftValue.getDValue() <= rightValue.getDValue();
                op = "<=";
                break;
            default:
                throw new Exception("syntax error");
        }

        return new ValueImpl(res);
    }

    @Override
    public String toString() {
        switch (operator) {
            case EQ:
                op = "=";
                break;
            case LT:
                op = "<";
                break;
            case GT:
                op = ">";
                break;
            case GE:
                op = ">=";
                break;
            case LE:
                op = "<=";
                break;
        }

        return op + right + ":" + left;
    }
}
