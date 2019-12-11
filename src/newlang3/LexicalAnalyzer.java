package newlang3;

public interface LexicalAnalyzer {
    //今回作るのはgetだけで、expectとungetは今は使わない
    public LexicalUnit get() throws Exception;
    public boolean expect(LexicalType type) throws Exception;
    public void unget(LexicalUnit token) throws Exception;    
}
