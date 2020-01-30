package newlang4.node;

import newlang3.*;
import newlang4.*;
import function.Function;

import java.util.*;

public class CallNode extends Node {
    /* <call_sub> and <call_func> */
    
    //<call_sub> ::=
    //      <NAME> <expr_list>
    
    //<call_func> ::=
    //      <NAME> <LP> <expr_list> <RP>

    private String funcName;
    private ExprListNode arguments;

    private final static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            //<NAME>
            LexicalType.NAME
    ));

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    private CallNode(Environment env) {
        this.env = env;
        this.type = NodeType.FUNCTION_CALL;
    }

    public static Node getHandler(LexicalType type, Environment env) throws Exception{
        if (!isMatch(type)) return null;
        else return new CallNode(env);
    }

    @Override
    public void parse() throws Exception {
        boolean isBracket = false;  // カッコの有無

        if (env.getInput().peek().getType() == LexicalType.NAME) {
            funcName = env.getInput().get().getValue().getSValue();
        } else {
            throw new Exception("syntax error");
        }

        if (env.getInput().peek().getType() == LexicalType.LP) {
            env.getInput().get();
            isBracket = true;
        }

        LexicalType type = env.getInput().peek().getType();
        if (ExprListNode.isMatch(type)) {
            arguments = ExprListNode.getHandler(type, env);
            arguments.parse();
        }

        if (isBracket) {
            if (env.getInput().get().getType() != LexicalType.RP) {
                throw new Exception("syntax error");
            }
        }
    }

    @Override
    public Value getValue() throws Exception {
        Function function = env.getFunction(funcName);
        return function.eval(arguments);
    }

    @Override
    public String toString() {
        return funcName + arguments + ";";
    }
}

