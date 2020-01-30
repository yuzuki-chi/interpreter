package function;

import newlang3.Value;
import newlang4.node.ExprListNode;

public abstract class Function {
    public abstract Value eval(ExprListNode arg) throws Exception;
}
