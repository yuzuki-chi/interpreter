/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang3;

/**
 *
 * @author yuzukiyu
 */
public class ValueImpl extends Value{
    private ValueType type;
    private String val;
    
    public ValueImpl(String s){
        super(s);
        type = ValueType.STRING;
        val = s;
    }
    
    public ValueImpl(double d){
        super(d);
        type = ValueType.DOUBLE;
        val = String.valueOf(d);
    }
    
    public ValueImpl(int i){
        super(i);
        type = ValueType.INTEGER;
        val = String.valueOf(i);
    }
    
    public ValueImpl(boolean b){
        super(b);
        type = ValueType.BOOL;
        val = String.valueOf(b);
    }

    public ValueImpl(String s, ValueType t) {
        super(s, t);
//        type = ValueType.;
        val = s;
    }

    @Override
    public String get_sValue() {
        return val;
    }

    @Override
    public String getSValue() {
        return val;
    }

    @Override
    public int getIValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getDValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getBValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ValueType getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
