package newlang3;

public interface LexicalAnalyzer {

    /**
     * ここでString, Number, Literal, Symbolのどれか解析をして, LexicalUnitとして返す.
     * @return LexicalUnit
     * @throws Exception
     * 
     */
    public LexicalUnit get() throws Exception;
    
    /**
     *
     * @param type
     * @return
     * @throws Exception
     */
    public boolean expect(LexicalType type) throws Exception;
    
    /**
     *
     * @param token
     * @throws Exception
     */
    public void unget(LexicalUnit token) throws Exception;

    /**
     *
     * @return INTEGER Line
     */
    public int getLine();
}
