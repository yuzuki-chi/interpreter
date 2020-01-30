package newlang4;

import newlang3.*;
import newlang4.node.*;
import function.*;

import java.util.Hashtable;

/**
 * つまるところ変数テーブルを持つクラス
 * @author y_takaya
 */
public class Environment {
	private LexicalAnalyzerImpl input;
        
        // 変数varテーブル | 関数funcテーブル
	private Hashtable<String, VariableNode> var_table = new Hashtable<>() ;
	private Hashtable<String, Function> func_table = new Hashtable<>();

	public Environment(LexicalAnalyzerImpl input) {
		this.input = input;
		func_table.put("PRINT", new PrintFunction());
	}
	    
	public LexicalAnalyzerImpl getInput() {
		return input;
	}


	public VariableNode getVariable(String varName) {
		VariableNode vn;
		vn = var_table.get(varName);
		if (vn == null) {
			vn = new VariableNode(varName);
			var_table.put(varName, vn);
		}
		return vn;
	}

	public Function getFunction(String funcName) {
		Function res = func_table.get(funcName);
		if (res == null) throw new InternalError("Not found function: "+funcName);
		return res;
	}


}
