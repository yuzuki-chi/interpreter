package newlang4.node;

import newlang4.Node;
import newlang3.Value;
import newlang3.LexicalType;

import java.util.*;

public class VariableNode extends Node {
    private String name;
    private Value value;

    private final static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            LexicalType.NAME
    ));

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public VariableNode(String name) {
        this.name = name;
    }

    public VariableNode(String name,Value value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(Value value){
        this.value = value;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}