package newlang3;

public class ValueImpl extends Value{
    private ValueType type;
    private String val;
    
    public ValueImpl(String s){
        super(s);
        this.type = ValueType.STRING;
        this.val = s;
    }
    
    public ValueImpl(double d){
        super(d);
        this.type = ValueType.DOUBLE;
        this.val = String.valueOf(d);
    }
    
    public ValueImpl(int i){
        super(i);
        this.type = ValueType.INTEGER;
        this.val = String.valueOf(i);
    }
    
    public ValueImpl(boolean b){
        super(b);
        this.type = ValueType.BOOL;
        this.val = String.valueOf(b);
    }

    public ValueImpl(String s, ValueType t) {
        super(s, t);
        this.val = s;
        this.type = t;
    }
    
    /* GET METHOD */
    @Override
    public String get_sValue() { return val; }

    @Override
    public String getSValue() { return val; }

    @Override
    public int getIValue() { return Integer.parseInt(val); }

    @Override
    public double getDValue() { return Double.parseDouble(val); }

    @Override
    public boolean getBValue() { return Boolean.parseBoolean(val); }

    @Override
    public ValueType getType() { return type; }
    
}
