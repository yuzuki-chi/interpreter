import java.io.FileInputStream;
import newlang3.*;
import newlang4.*;
import newlang4.node.*;

public class Main_5 {
    public static void main(String[] args) throws Exception {
        String FILEPATH = "./src/BASIC_testcode/BASIC_code_01.bas";

        //System.out.println("basic parser");

        try {
            FileInputStream FIStream = new FileInputStream(FILEPATH);
            LexicalAnalyzerImpl lexicalAnalyzerImpl = new LexicalAnalyzerImpl(FIStream);
            
            LexicalUnit lexicalUnit  = lexicalAnalyzerImpl.get(); //1Unitを解析して返す
            lexicalAnalyzerImpl.unget(lexicalUnit);
            Environment env = new Environment(lexicalAnalyzerImpl);
            
            Node program = ProgramNode.getHandler(lexicalUnit.getType(), env);
            if (program != null) { 
                program.parse();
                System.out.println("-- Syntax Analyzer ---");
                System.out.println(program);
                System.out.println("-- Interpreter --");
                System.out.println(program.getValue());
            } else { 
                System.out.println("ERROR: 構文が誤っているか、まだ対応していない構文を使用しています");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}