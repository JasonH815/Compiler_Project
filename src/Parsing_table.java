import java.util.HashMap;

import com.cpsc323.compiler.tokenize.Tokenize;

/**
 * 
 * Author: @author
 * Project: Compiler
 * Package:
 * Date:17/11/2013
 * File: parsing_table.java
 * Todo: TODO
 */

/**
 * @author Sean, Jason
 * 
 */
public class Parsing_table {

	private static HashMap<Integer, HashMap<String, String>> table;

	static {
		table = new HashMap<>();
		HashMap<String, String> row;

		row = new HashMap<>();
		row.put("S", "1");
		row.put("A", "2");
		row.put("PROGRAM", "s3");
		table.put(0, row);

		row = new HashMap<>();
		row.put("$", "acc");
		table.put(1, row);

		row = new HashMap<>();
		row.put("B", "4");
		row.put("VAR", "s5");
		table.put(2, row);

		row = new HashMap<>();
		row.put("pname", "6");
		row.put("id", "s7");
		table.put(3, row);

		row = new HashMap<>();
		row.put("C", "8");
		row.put("BEGIN", "s9");
		table.put(4, row);

		row = new HashMap<>();
		row.put("dec-list", "11");
		row.put("dec", "12");
		row.put("id", "s13");
		table.put(5, row);

		row = new HashMap<>();
		row.put(";", "s10");
		table.put(6, row);

		row = new HashMap<>();
		row.put(";", "r5");
		table.put(7, row);

		row = new HashMap<>();
		row.put("$", "r1");
		table.put(8, row);

		row = new HashMap<>();
		row.put("stat-list", "20");
		row.put("stat", "21");
		row.put("print", "22");
		row.put("assign", "23");
		row.put("PRINT", "s24");
		row.put("id", "s25");
		table.put(9, row);

		row = new HashMap<>();
		row.put("VAR", "r2");
		table.put(10, row);

		row = new HashMap<>();
		row.put(";", "s14");
		table.put(11, row);

		row = new HashMap<>();
		row.put(":", "s15");
		table.put(12, row);

		row = new HashMap<>();
		row.put(":", "r7");
		row.put(",", "s16");
		table.put(13, row);

		row = new HashMap<>();
		row.put("BEGIN", "r3");
		table.put(14, row);

		row = new HashMap<>();
		row.put("type", "17");
		row.put("INTEGER", "s18");
		table.put(15, row);

		row = new HashMap<>();
		row.put("id", "s13");
		row.put("dec", "19");
		table.put(16, row);

		row = new HashMap<>();
		row.put(";", "r6");
		table.put(17, row);

		row = new HashMap<>();
		row.put(";", "r26");
		table.put(18, row);

		row = new HashMap<>();
		row.put(":", "r8");
		table.put(19, row);

		row = new HashMap<>();
		row.put("END", "s26");
		table.put(20, row);

		row = new HashMap<>();
		row.put(";", "s27");
		table.put(21, row);

		row = new HashMap<>();
		row.put(";", "r11");
		table.put(22, row);

		row = new HashMap<>();
		row.put(";", "r12");
		table.put(23, row);

		row = new HashMap<>();
		row.put("(", "s28");
		table.put(24, row);

		row = new HashMap<>();
		row.put("=", "s29");
		table.put(25, row);

		row = new HashMap<>();
		row.put(".", "s33");
		table.put(26, row);

		row = new HashMap<>();
		row.put("END", "r9");
		row.put("stat-list", "30");
		row.put("stat", "21");
		row.put("print", "22");
		row.put("assign", "23");
		row.put("PRINT", "s24");
		row.put("id", "s25");
		table.put(27, row);

		row = new HashMap<>();
		row.put("output", "31");
		row.put("string", "s34");
		row.put("id", "s35");
		table.put(28, row);

		row = new HashMap<>();
		row.put("expr", "38");
		row.put("term", "39");
		row.put("factor", "40");
		row.put("id", "s41");
		row.put("number", "s42");
		row.put("(", "s43");
		table.put(29, row);

		row = new HashMap<>();
		row.put("END", "r10");
		table.put(30, row);

		row = new HashMap<>();
		row.put(")", "s32");
		table.put(31, row);

		row = new HashMap<>();
		row.put(";", "r13");
		table.put(32, row);

		row = new HashMap<>();
		row.put("$", "r4");
		table.put(33, row);

		row = new HashMap<>();
		row.put(",", "s36");
		table.put(34, row);

		row = new HashMap<>();
		row.put(")", "r14");
		table.put(35, row);

		row = new HashMap<>();
		row.put("id", "s37");
		table.put(36, row);

		row = new HashMap<>();
		row.put(")", "r15");
		table.put(37, row);

		row = new HashMap<>();
		row.put(";", "r16");
		row.put("+", "s44");
		row.put("-", "s45");
		table.put(38, row);

		row = new HashMap<>();
		row.put("+", "r17");
		row.put("-", "r17");
		row.put(")", "r17");
		row.put(";", "r17");
		row.put("*", "s46");
		row.put("/", "s47");
		table.put(39, row);

		row = new HashMap<>();
		row.put("*", "r20");
		row.put("/", "r20");
		row.put("+", "r20");
		row.put("-", "r20");
		row.put(")", "r20");
		row.put(";", "r20");
		table.put(40, row);

		row = new HashMap<>();
		row.put("*", "r23");
		row.put("/", "r23");
		row.put("+", "r23");
		row.put("-", "r23");
		row.put(")", "r23");
		row.put(";", "r23");
		table.put(41, row);

		row = new HashMap<>();
		row.put("*", "r24");
		row.put("/", "r24");
		row.put("+", "r24");
		row.put("-", "r24");
		row.put(")", "r24");
		row.put(";", "r24");
		table.put(42, row);

		row = new HashMap<>();
		row.put("term", "39");
		row.put("factor", "40");
		row.put("id", "s41");
		row.put("number", "s42");
		row.put("(", "s43");
		row.put("expr", "48");
		table.put(43, row);

		row = new HashMap<>();
		row.put("term", "50");
		row.put("factor", "40");
		row.put("id", "s41");
		row.put("number", "s42");
		row.put("(", "s43");
		table.put(44, row);

		row = new HashMap<>();
		row.put("term", "51");
		row.put("factor", "40");
		row.put("id", "s41");
		row.put("number", "s42");
		row.put("(", "s43");
		table.put(45, row);

		row = new HashMap<>();
		row.put("factor", "52");
		row.put("id", "s41");
		row.put("numbber", "s42");
		row.put("(", "s43");
		table.put(46, row);

		row = new HashMap<>();
		row.put("factor", "53");
		row.put("id", "s41");
		row.put("number", "s42");
		row.put("(", "s43");
		table.put(47, row);

		row = new HashMap<>();
		row.put(")", "s49");
		row.put("+", "s44");
		row.put("-", "s45");
		table.put(48, row);

		row = new HashMap<>();
		row.put("*", "r25");
		row.put("/", "r25");
		row.put("+", "r25");
		row.put("-", "r25");
		row.put(")", "r25");
		row.put(";", "r25");
		table.put(49, row);

		row = new HashMap<>();
		row.put("+", "r18");
		row.put("-", "r18");
		row.put(")", "r18");
		row.put(";", "r18");
		row.put("*", "s46");
		row.put("/", "s47");
		table.put(50, row);

		row = new HashMap<>();
		row.put("+", "r19");
		row.put("-", "r19");
		row.put(")", "r19");
		row.put(";", "r19");
		row.put("*", "s46");
		row.put("/", "s47");
		table.put(51, row);

		row = new HashMap<>();
		row.put("*", "r21");
		row.put("/", "r21");
		row.put("+", "r21");
		row.put("-", "r21");
		row.put(")", "r21");
		row.put(";", "r21");
		table.put(52, row);

		row = new HashMap<>();
		row.put("*", "r22");
		row.put("/", "r22");
		row.put("+", "r22");
		row.put("-", "r22");
		row.put(")", "r22");
		row.put(";", "r22");
		table.put(53, row);

	}

	/**
	 * Tests if if a given row/col combination is blank or has a value
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public static boolean isTableBlank(String row, String col) {
		String lookupResult = null;
		lookupResult = lookup(row, col);
		if (lookupResult == null)
			return true;
		return false;
	}

	/**
	 * Returns a the value associated with a given row/col combination. If no
	 * combination exists, returns null instead.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public static String lookup(String row, String col) {
		if (row == null || row.trim().isEmpty() || col == null
				|| col.trim().isEmpty())
			return null;

		row = row.trim();
		col = col.trim(); // because just in case

		// parse int from row string
		Integer r = null;
		try {
			r = Integer.parseInt(row);
		} catch (NumberFormatException e) {
			System.out.println(e);
		}
		if (r == null)
			return null;

		String colString = reformatCol(col);

		// // TODO Fix is in place, should no long be needed (this is buggy
		// anyway since it confuses the PRINT non-terminal and PRINT terminal)
		// if (!(colString.equals("A") || colString.equals("B")
		// || colString.equals("C") || colString.equals("PRINT")
		// || colString.equals("INTEGER") || colString.equals("PROGRAM")
		// || colString.equals("VAR") || colString.equals("BEGIN") || colString
		// .equals("END"))) {
		// colString = colString.toLowerCase();
		// }
		// System.out.println("sent to table:" + r + ' ' + colString);// debug

		String result = null;
		result = table.get(r).get(colString);
		return result;

	}

	/**
	 * 
	 * Will reformat numbers, identifiers, and strings appropriately for hash
	 * table lookup. If given value was not a number, string, or identifier, the
	 * returned value will be the same as input. otherwise "number", "string",
	 * "id" will be returned as appropriate.
	 * 
	 * @param input
	 * @return
	 */
	private static String reformatCol(String token) {
		if (token == null || token.trim().isEmpty())
			return "";

		if (Tokenize.classification.get(token) == Tokenize.TOKEN_TYPE.STRING)
			return "string";
		if (Tokenize.classification.get(token) == Tokenize.TOKEN_TYPE.NUMBER)
			return "number";
		if (Tokenize.classification.get(token) == Tokenize.TOKEN_TYPE.ID)
			return "id";
		else
			return token;
	}
}
