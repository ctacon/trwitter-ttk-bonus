/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trwitter.ttk.bonus;

import java.math.BigDecimal;
import java.util.StringTokenizer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author ctacon
 */
public class Calculation {

    private Calculation() {
    }

    public static Calculation getInstance() {
	return CalculationHolder.INSTANCE;
    }

    private static class CalculationHolder {

	private static final Calculation INSTANCE = new Calculation();
    }

    public String calculate(String s) {
	try {
	    if (s.contains("+") || s.contains("-") || s.contains("*") || s.contains("/")) {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		return (long) Double.parseDouble(new BigDecimal(engine.eval(s) + "").toPlainString()) + "";
	    } else {
		return s;
	    }

	} catch (Exception ex) {
	    ex.printStackTrace();
	    System.out.println(ex);
	    return null;
	}
    }
}
