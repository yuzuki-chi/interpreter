import newlang3.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main_3 {
    public static void main(String[] args) {
        String FILEPATH = "/Users/yuzukiyu/NetBeansProjects/interpreter/newlang3/src/newlang3/importText";
        
        try {
            FileInputStream FIStream = new FileInputStream(FILEPATH);
            LexicalUnit lexicalUnit;
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzerImpl(FIStream);

            while (true){
                lexicalUnit = lexicalAnalyzer.get();
                if(!String.valueOf(lexicalUnit).isEmpty()) System.out.println(lexicalUnit);
                if(lexicalUnit.getType() == LexicalType.EOF) {
//                System.out.println("解析終了しました");
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}