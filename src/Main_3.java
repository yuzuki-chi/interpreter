import newlang3.*;

import java.io.FileInputStream;

public class Main_3 {
    public static void main(String[] args) {
        String FILEPATH = "./src/BASIC_testcode/BASIC_code_01.bas";
        
        try {
            FileInputStream FIStream = new FileInputStream(FILEPATH);
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl(FIStream);
            LexicalUnit lexicalUnit;

            while (true){
                lexicalUnit = lexicalAnalyzer.get();
                if(!String.valueOf(lexicalUnit).isEmpty()) //FIStreamの1行文を
                    System.out.println(lexicalUnit);
                if(lexicalUnit.getType() == LexicalType.EOF) {
                    break;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}