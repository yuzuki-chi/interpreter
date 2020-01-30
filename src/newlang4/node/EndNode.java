package newlang4.node;

import newlang3.*;
import newlang4.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EndNode extends Node {

    private static Set<LexicalType> FIRST = new HashSet<>(Arrays.asList(
            //<END>
            LexicalType.END
    ));

    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) throws Exception {
        if (isMatch(type)) return new EndNode(env);
        else return null;
    }

    private EndNode(Environment env){
        this.env = env;
        this.type = NodeType.END;
    }

    public void parse() throws Exception {
        if (env.getInput().get().getType()!=LexicalType.END){
            throw new InternalError("ERROR : NOT END.");
        }
    }

    public Value getValue() {
        System.exit(0); //end
        return null;
    }

    @Override
    public String toString() {
        return "END";
    }
}
