package newlang4.node;

import newlang4.*;
import newlang3.*;

import java.util.*;

public class ConstNode extends Node {

    private Value value;

    private final static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            LexicalType.SUB,
            LexicalType.INTVAL,
            LexicalType.DOUBLEVAL,
            LexicalType.LITERAL
    ));

    private ConstNode(Environment env, Value value) {
        this.env = env;
        switch (value.getType()){
            case INTEGER:
                this.type = NodeType.INT_CONSTANT;
                break;
            case DOUBLE:
                this.type = NodeType.DOUBLE_CONSTANT;
                break;
            case STRING:
                this.type = NodeType.STRING_CONSTANT;
                break;
            default:
                throw new InternalError("value type error");
        }
        this.value = value;
    }
    
    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env, Value value) {
        if (isMatch(type)) return new ConstNode(env, value);
        else return null;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value.getSValue());
    }

}
