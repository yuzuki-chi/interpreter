/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang4;

/**
 *
 * @author yuzukiyu
 */
public class ValueImpl implements Value{
    private ValueType type;
    private String val;
    
    public ValueImpl(String s){
        type = ValueType.STRING;
        val = s;
    }
    
    public ValueImpl(double d){
        type = ValueType.DOUBLE;
        val = String.valueOf(d);
    }
    
    public ValueImpl(int i){
        type = ValueType.INTEGER;
        val = String.valueOf(i);
    }
    
    public ValueImpl(boolean b){
        type = ValueType.BOOL;
        val = String.valueOf(b);
    }

    public ValueImpl(String s, ValueType t) {
//        type = ValueType.;
        val = s;
    }

    public String get_sValue() {
        return val;
    }

    public String getSValue() {
        return val;
    }

    public int getIValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double getDValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean getBValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ValueType getType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
