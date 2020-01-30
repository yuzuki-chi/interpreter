package newlang4;
import newlang3.Value;

public abstract class Node {
    protected NodeType type;
    protected Environment env;

    /** Creates a new instance of Node */
    public Node() {
    }
    public Node(NodeType my_type) {
        type = my_type;
    }
    public Node(Environment my_env) {
        env = my_env;
    }
    
    public NodeType getType() {
        return type;
    }

    public void parse() throws Exception {
        //boolean -> void
    }

    public Value getValue() throws Exception {
        return null;
    }

    public String toString() {
    	if (type == NodeType.END) return "END";
    	else return "Node";        
    }
}
