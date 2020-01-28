package newlang3;

/**
 *
 * @author y_takaya
 * 抽象メソッド
 */
public abstract class Value {
    // ValueTypeを定義するtype
    private ValueType type;
    // 実際のValueを格納するval
    private String val;
    
    /**
     *
     * @param s
     */
    public Value(String s) {};

    /**
     *
     * @param i
     */
    public Value(int i) {};

    /**
     *
     * @param d
     */
    public Value(double d) {};

    /**
     *
     * @param b
     */
    public Value(boolean b) {};

    /**
     *
     * @param s
     * @param t
     */
    public Value(String s, ValueType t) {};
    
    /**
     *
     * @return
     */
    public abstract String get_sValue();

    /**
     *
     * @return
     */
    public abstract String getSValue();
	// ストリング型で値を取り出す。必要があれば、型変換を行う。

    /**
     *
     * @return
     */
    public abstract int getIValue();
    	// 整数型で値を取り出す。必要があれば、型変換を行う。

    /**
     *
     * @return
     */
    public abstract double getDValue();
    	// 小数点型で値を取り出す。必要があれば、型変換を行う。

    /**
     *
     * @return
     */
    public abstract boolean getBValue();
    	// 論理型で値を取り出す。必要があれば、型変換を行う。

    /**
     *
     * @return
     */
    public abstract ValueType getType();
}
