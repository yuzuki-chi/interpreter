package newlang4.node;

import newlang4.*;
import newlang3.*;
import function.*;

import java.util.*;
import java.util.List;

public class ExprNode extends Node {
    //<expr> ::=
    //      <expr><ADD><expr>
    //      |<expr><SUB><expr>
    //      |<expr><MUL><expr>
    //      |<expr><DIV><expr>
    //      |<SUB><expr>
    //      |<LP><expr><RP>
    //      |<NAME>
    //      |<INTVAL>
    //      |<DOUBLEVAL>
    //      |<LITERAL>
    //      |<call_func>

    Node left;
    Node right;
    LexicalType operator;

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

    private final static Map<LexicalType, Integer> OPERATORS = new HashMap<>();
    static {
        // 演算子と優先順位を設定
        OPERATORS.put(LexicalType.DIV,1);
        OPERATORS.put(LexicalType.MUL,2);
        OPERATORS.put(LexicalType.SUB,3);
        OPERATORS.put(LexicalType.ADD,4);
    }

    public ExprNode(Environment env) {
        this.env = env;
        this.type = NodeType.EXPR;
    }

    public ExprNode(Node left, Node right, LexicalType operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }
    
    public static boolean isMatch(LexicalType type){
        return FIRST.contains(type);
    }

    public static Node getHandler(LexicalType type, Environment env) {
        if (isMatch(type)) return new ExprNode(env);
        else return null;
    }

    @Override
    public void parse() throws Exception {
        List<Node> result=new ArrayList<>();
        List<LexicalType> operators = new ArrayList<>();

        while(true) {
            switch (env.getInput().peek().getType()) {
                case LP:
                    env.getInput().get();
                    Node handler = ExprNode.getHandler(env.getInput().peek().getType(), env);
                    handler.parse();
                    result.add(handler);
                    if (env.getInput().get().getType() != LexicalType.RP) {
                        throw new Exception("syntax error");
                    }
                    break;
                case INTVAL:
                case DOUBLEVAL:
                case LITERAL:
                    LexicalUnit lu = env.getInput().get();
                    result.add(ConstNode.getHandler(lu.getType(), env, lu.getValue()));
                    break;
                case SUB:
                    switch (env.getInput().peek(2).getType()){
                        case INTVAL:
                        case DOUBLEVAL:
                        case LP:
                            env.getInput().get();
                            result.add(ConstNode.getHandler(LexicalType.INTVAL, env, new ValueImpl(-1)));
                            addOperator(result, operators, LexicalType.MUL);
                            continue;
                        default:
                            throw new Exception("syntax error");
                    }
                case NAME:
                    if (env.getInput().peek(2).getType() == LexicalType.LP) {
                        Node tmpNode = CallNode.getHandler(env.getInput().peek().getType(), env);
                        tmpNode.parse();
                        result.add(tmpNode);
                    } else {
                        lu = env.getInput().get();
                        result.add(env.getVariable(lu.getValue().getSValue()));
                    }
                    break;
                default:
                    // 計算式の構成が不正な場合
                    throw new Exception("syntax error");
            }
            if (OPERATORS.containsKey(env.getInput().peek().getType())) {
                addOperator(result, operators, env.getInput().get().getType());
            } else {
                break;
            }
        }

        for(int i=operators.size()-1;i>=0;i--){
            if (operators.size()==1){
                left=result.get(0);
                right=result.get(1);
                operator=operators.get(0);
                return;
            }
            result.add(new ExprNode(result.get(result.size()-2),
                    result.get(result.size()-1),operators.get(i)));
            result.remove(result.size()-3);
            result.remove(result.size()-2);
        }
        left = result.get(0);
    }


    private void addOperator (List<Node> rList, List<LexicalType> oList, LexicalType newOperator) throws Exception {
        boolean flag = false;
        for (int i=oList.size()-1; i>=0; i--) {
            if (OPERATORS.get(oList.get(i)) < OPERATORS.get(newOperator)) {
                flag = true;
                rList.add(new ExprNode(rList.get(rList.size()-2), rList.get(rList.size()-1), oList.get(i)));
                rList.remove(rList.size()-3);
                rList.remove(rList.size()-2);
                oList.remove(i);
            } else if (flag && OPERATORS.get(oList.get(i)) >= OPERATORS.get(newOperator)) {
                break;
            }
        }
        oList.add(newOperator);
    }

    @Override
    public Value getValue() throws Exception {
        if (operator == null) return left.getValue();

        Value leftVal = left.getValue();
        Value rightVal = right.getValue();

        if (leftVal.getType() == ValueType.STRING || rightVal.getType() == ValueType.STRING) {
            switch (operator) {
                case ADD:
                    return new ValueImpl(leftVal.getSValue() + rightVal.getSValue());
                default:
                    throw new Exception("syntax error");
            }
        } else if (leftVal.getType() == ValueType.DOUBLE || rightVal.getType() == ValueType.DOUBLE) {
            double leftNum = leftVal.getDValue();
            double rightNum = rightVal.getDValue();
            switch (operator) {
                case ADD:
                    return new ValueImpl(leftNum + rightNum);
                case SUB:
                    return new ValueImpl(leftNum - rightNum);
                case MUL:
                    return new ValueImpl(leftNum * rightNum);
                case DIV:
                    if (rightNum != 0.00) {
                        return new ValueImpl(leftNum / rightNum);
                    } else {
                        throw new Exception("too many error");
                    }
                default:
                    throw new Exception("syntax error");
            }
        } else {
            int leftNum = leftVal.getIValue();
            int rightNum = rightVal.getIValue();
            switch (operator) {
                case ADD:
                    return new ValueImpl(leftNum + rightNum);
                case SUB:
                    return new ValueImpl(leftNum - rightNum);
                case MUL:
                    return new ValueImpl(leftNum * rightNum);
                case DIV:
                    if (rightNum != 0) {
                        return new ValueImpl(leftNum / rightNum);
                    } else {
                        throw new Exception("too many error");
                    }
                default:
                    throw new Exception("syntax error");
            }
        }
    }

    @Override
    public String toString() {
        String op = "";
        String str = "";
        if (operator!=null){
            if (operator == operator.ADD) op = "+";
            else if (operator == operator.SUB) op = "-";
            else if (operator == operator.MUL) op = "*";
            else if (operator == operator.DIV) op = "/";            
            str = "[" + op + "[" + left + "," + right + "]]";
        }else{
            str = "[" + left + "]";
        } 
        return str;
    }
}
