package function;

import newlang3.Value;
import newlang4.node.ExprListNode;

/**
 * Printを行うFunction
 * @author y_takaya
 */
public class PrintFunction extends Function {
    @Override
    public Value eval(ExprListNode exprList) throws Exception {
        // ExprList -> list -> value -> (String)value
        System.out.println(exprList.getValue(0).getSValue());
        return null;
    }
}
