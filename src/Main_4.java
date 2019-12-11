//import newlang3.ValueImpl;
import newlang4.*;

import java.io.FileInputStream;
import newlang4.node.Program;

public class Main_4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	       FileInputStream fin = null;
	        LexicalAnalyzer lex;
	        LexicalUnit		first;
	        Environment		env;
	        Node			program;
	  
	        System.out.println("basic parser");
	        fin = new FileInputStream("/Users/yuzukiyu/NetBeansProjects/interpreter/newlang3/src/newlang3/importText");
	        lex = new LexicalAnalyzerImpl(fin);
	        env = new Environment(lex);
	        first = lex.get();
	        
	        program = Program.isMatch(env, first);
	        if (program != null && program.Parse()) {
	        	System.out.println(program);
	        	System.out.println("value = " + program.getValue());
	        }
	        else System.out.println("syntax error");
	}

}
