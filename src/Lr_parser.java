/**
 * 
 * Author: Sean Callahan, Jason Hellwig
 * Project: Compiler
 * Package:
 * Date:17/11/2013
 * File: lr_parser.java
 * Todo: TODO
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.cpsc323.compiler.tokenize.Tokenize;
import com.cpsc323.compiler.tokenize.Tokenize.TOKEN_TYPE;

public class Lr_parser {

	// the stack
	// TODO: This should probably be an arraylist
	private static LinkedList<String> lr_stack = new LinkedList<String>();

	// List of words used for error checking. All of these words must be in
	// input arraylist.
	private static ArrayList<String> essential_words = new ArrayList<String>(
			Arrays.asList("PROGRAM", "VAR", "BEGIN", "PRINT", "END"));

	// List of words used for checking whether incoming token is a reserved word
	// or a variable. TODO: Use this in scope check
	private static ArrayList<String> reserved_words = new ArrayList<String>(
			Arrays.asList("PROGRAM", "VAR", "BEGIN", "PRINT", "END", "INTEGER",
					"PRINT"));

	// the output stream
	private static String parser_history = "";
	// ending symbol
	private static String specialSymbol = ";";
	// X axis variable
	private static String q = "";

	// Y axis variable
	private static String a = "";
	// index of input
	private static int index = 0;
	// Variable for return from table lookup
	private static String tableRet = "";
	// Starting state
	private static String start_state = "0";
	// switch to turn on identifier checking
	private static boolean scope_checking = false;
	// switch to turn on identifier tracking
	private static boolean scope_tracking = false;
	// variable scope table
	private static HashSet<String> declared_vars = new HashSet<>();
	// error message
	private static String errmsg = null;

	public static boolean debug_output = true;

	boolean parse_grammar(List<String> input) {
		// append $ to input
		input.add("$");

		// variables are not being checked for scope yet
		scope_checking = scope_tracking = false;
		declared_vars.clear();

		// state 0 onto stack.
		lr_stack.push(start_state);

		// While list is not empty.
		while (!lr_stack.isEmpty()) {

			debug_parser_status();
			// Let 'q' be the current state
			q = lr_stack.peek();

			// Let 'a' be the incoming token
			a = input.get(index);

			// check for errors with the identifiers being defined.
			errmsg = scope_error(a);
			if (errmsg != null) {
				System.out.println(errmsg);
				return false;
			}

			debug_parser_lookup_attempt();
			// check this grammar exists within our table
			if (Parsing_table.isTableBlank(q, a)) {

				debug_parser_lookup_result();

				return false; // The grammar does not exist. This line will not
								// compile.
			} else {
				// The grammar exists
				// Get the required SHIFT or RULE from the table.
				tableRet = Parsing_table.lookup(q, a);
				parser_history += tableRet + " ";
				// If the first char is an S, the grammar is telling us to
				// SHIFT.
				if (tableRet.charAt(0) == 's') {
					lr_stack.push(a);
					lr_stack.push(tableRet.substring(1));
					// Increment the index for this line.
					if (input.size() > index + 1) {
						index++;

					}

				} else if (tableRet.charAt(0) == 'r') {
					// We have a RULE. Reduce N.

					// Apply Rule to stack.
					a = rule(tableRet.substring(1)); // Get Letter back rule
					debug_parser_rule(tableRet);
					q = lr_stack.peek();
					lr_stack.push(a); // Push Terminal to stack

					debug_parser_status();
					debug_parser_lookup_attempt();
					if (!Parsing_table.isTableBlank(q, a)) {

						lr_stack.push(Parsing_table.lookup(q, a));

					} else {
						// If for any reason the lookup fails, then the grammar
						// check has failed.

						return false;
					}
				} else if ("acc".equals(tableRet)) {

					// ACC= Done
					return true;
				}

			}

		}

		return false;
	}

	/**
	 * Checking for missing reserved words. If there are reserved words missing
	 * it will return FALSE.
	 * 
	 * @param input
	 * @return
	 */
	boolean essential_words_check(List<String> input) {
		List<String> missing_check = new ArrayList<String>(essential_words);
		missing_check.removeAll(input);

		// if reserved words are not found
		if (missing_check.size() > 0) {

			// Print out the missing ones
			for (String x : missing_check) {
				System.out.println(x + " EXPECTED.");
			}
			return false;
		}
		// else, input is OK.
		return true;
	}

	/**
	 * The only period in the grammar is after END. This function assumes the
	 * presence of "END" but checks if the period is missing.
	 * 
	 * @param tokens
	 * @return
	 */
	boolean fullstop_check(List<String> tokens) {

		// searching from the back, first thing we should encounter is .
		// for (int q = tokens.size() - 1; q > 0; q--) {
		// if (tokens.get(q).contains(".")) {
		// return true;
		// } else if (!tokens.get(q).contains(" ")
		// || (!tokens.get(q).contains("\n"))) {
		// System.out.println(". MISSING");
		// return false;
		// }
		// }

		if (!(tokens.get(tokens.size() - 2).contains("."))) {
			System.out.println(". MISSING");
			return false;
		}

		return true;
	}

	/**
	 * Checks each line for last character being SEMICOLON. Takes paramater as
	 * whole line.
	 * 
	 * @param input_line
	 * @return
	 */
	boolean semicolon_end_check(List<String> tokens) {

		String whole_line = "";

		for (String x : tokens) {

			// if x is not a newline, add it to while_line.
			if (!x.equals("/n")) {
				whole_line += x;
			} else { // if it is a newline, test it for ending with ";".
				if (!(whole_line.substring(whole_line.length() - 1)
						.contains(";"))) {
					System.out.println("; IS MISSING.");
					return false;
				} else {
					whole_line = "";
				}
			}

		}
		return true;
	}

	/**
	 * Takes VAR line as a list. Returns false if a comma is missing. Function
	 * goes by odds to check for a comma or a :. If we get a comma, then the
	 * loop continues. If we get a : then we are done. If we are missing a
	 * comma, return false.
	 * 
	 * @param input_line
	 * @return
	 */
	boolean comma_var_check(List<String> tokens) {

		int p = 0;
		// Find index of "var"
		for (String x : tokens) {
			if (x.contains("VAR")) {
				break;
			} else {
				p++;
			}
		}
		p = p + 3; // skip VAR, NEWLINE and FIRST VAR.
		// Check to see for commmas between every variable
		for (int q = p; q < tokens.size(); q = q + 2) {

			if (tokens.get(q).contains(":")) {
				return true;
			}

			if (!(tokens.get(q).contains(","))) {
				System.out.println(", IS MISSING.");
				return false;
			}
		}

		return false;
	}

	/**
	 * checks for missing paranthesis
	 * 
	 * @param tokens
	 * @return
	 */
	boolean print_check(List<String> tokens) {
		int p = 0; // index of print
		int q = 0; // used in loop
		boolean isString = false;
		// Find index of every "print"
		for (String x : tokens) {
			if (x.contains("PRINT")) {
				q = p;
				// search this line for ( and )
				while (!tokens.get(q).contains("\n")) {
					// TODO: theres probably a better way of doing this
					// first var needs to be (

					if (q == p + 1 && !(tokens.get(q).contains("("))) {
						System.out.println("( IS MISSING");
						return false;
					}

					if (tokens.get(q + 2).contains("\n")) {
						if (!tokens.get(q).contains(")")) {
							System.out.println(") IS MISSING");
							return false;
						}
					}
					if (tokens.get(q).contains("\"")) {
						isString ^= false;
					}

					q++;

				}
				// This means teh string boolean has not been flipped back.
				if (isString) {
					System.out.println("\" MISSING");
					return false;
				}

			} else {
				p++;
			}

		}
		return true;
	}

	/**
	 * Conducts all rule operations. Returns appropriate string.
	 * 
	 * @param value
	 * @return
	 */
	private String rule(String value) {

		int z = Integer.parseInt(String.valueOf(value));
		switch (z) {
		case 1:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "S";

		case 2:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "A";

		case 3:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "B";

		case 4:
			for (int q = 0; q < 8; q++) {
				lr_stack.pop();
			}
			return "C";

		case 5:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "pname";

		case 6:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "dec-list";

		case 7:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "dec";

		case 8:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "dec";

		case 9:
			for (int q = 0; q < 4; q++) {
				lr_stack.pop();
			}
			return "stat-list";

		case 10:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "stat-list";

		case 11:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "stat";

		case 12:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "stat";

		case 13:
			for (int q = 0; q < 8; q++) {
				lr_stack.pop();
			}
			return "print";

		case 14:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "output";

		case 15:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "output";

		case 16:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "assign";

		case 17:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "expr";

		case 18:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "expr";

		case 19:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "expr";

		case 20:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "term";

		case 21:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "term";

		case 22:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "term";

		case 23:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "factor";

		case 24:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "factor";

		case 25:
			for (int q = 0; q < 6; q++) {
				lr_stack.pop();
			}
			return "factor";

		case 26:
			for (int q = 0; q < 2; q++) {
				lr_stack.pop();
			}
			return "type";

		default:
			System.out.println("Unknown Rule!");

		}
		return "ERROR";
	}

	/**
	 * Outputs stack, q,a index and history of parser.
	 */
	private void debug_parser_status() {
		if (debug_output) {
			System.out.println("PARSER STATUS:" + "Q-VALUE:" + q + " A-VALUE:"
					+ a + " INDEX:" + index + " HISTORY: " + parser_history
					+ " STACK:" + lr_stack.toString());
		}

	}

	/**
	 * Returns Q and A and Index indicating a lookup attempt is about to be
	 * conducted.
	 */
	private void debug_parser_lookup_attempt() {
		if (debug_output) {
			System.out.println("**ATTEMPTING LOOKUP " + "Q-VALUE:" + q
					+ " A-VALUE:" + a + " INDEX:+" + index);
		}
	}

	/**
	 * Outputs Q A Index and the returned value from recent parsing table
	 * lookup.
	 */
	private void debug_parser_lookup_result() {
		if (debug_output) {
			System.out.println("****LOOKUP RESULT: " + "Q-VALUE:" + q
					+ " A-VALUE:" + a + " INDEX:+" + index + " table_ret:"
					+ tableRet);
		}

	}

	/**
	 * ' Outputs current parser values Q, A INDEX and Recent rule
	 * 
	 * @param x
	 */
	private void debug_parser_rule(String x) {
		if (debug_output) {
			System.out.println("********RULE APPLIED: " + "Q-VALUE:" + q
					+ " A-VALUE:" + a + " INDEX:+" + index + "  RULE: " + x);
		}
	}

	/**
	 * Returns grammar trace history.
	 */
	void output_parser_history() {
		System.out.println("**PARSER HISTORY**");
		System.out.println(parser_history);
	}

	private static String scope_error(String token) {
		if (scope_checking) {
			if (Tokenize.classification.get(token) == TOKEN_TYPE.ID)
				if (!declared_vars.contains(token))
					return "Unknown identifier: " + token;
		} else if (token.equals("VAR")) {
			scope_tracking = true;
		} else if (token.equals("BEGIN")) {
			scope_tracking = false;
			scope_checking = true;
		} else if (scope_tracking) {
			if (Tokenize.classification.get(token) == TOKEN_TYPE.ID) {
				if (declared_vars.contains(token))
					return "Duplicate identifier: " + token;
				declared_vars.add(token);
			}
		}
		return null;
	}

	public static String check_for_errors(List<String> input) {
		// check the program definition line
		if (!input.get(0).equals("PROGRAM"))
			return "PROGRAM is expected";
		if (!input.get(2).equals(";"))
			return "; is missing";

		// check var declarations lines
		if (!input.get(3).equals("VAR"))
			return "VAR is expected";
		int i;
		for (i = 3; i < input.size() && !input.get(i).equals(";"); ++i) {
			if (Tokenize.classification.get((input.get(i))) == TOKEN_TYPE.ID) {
				if (i + 1 >= input.size()
						|| (!input.get(i + 1).equals(",") && !input.get(i + 1)
								.equals(":")))
					return ", is missing";
			} else if (input.get(i).equals("INTEGER")
					&& !input.get(i - 1).equals(":"))
				return ": is missing";
			else if (input.get(i).equals(":"))
				if (i + 1 >= input.size()
						|| !input.get(i + 1).equals("INTEGER"))
					return "INTEGER is expected";
				else if (i + 2 >= input.size() || !input.get(i + 2).equals(";"))
					return "; is missing";
				else if (i + 3 >= input.size()
						|| !input.get(i + 3).equals("BEGIN"))
					return "BEGIN is expected";
		}
		i = i + 3;

		// check for ;
		boolean hasSemicolon = true;
		for (int j = i; j < input.size(); ++j) {
			if (input.get(j).equals("=") || input.get(j).equals("PRINT")) {
				if (!hasSemicolon)
					return "; is missing";
				else
					hasSemicolon = false;
			} else if (input.get(j).equals(";"))
				hasSemicolon = true;
		}

		// check for ( and )
		int rightCount = 0;
		int leftCount = 0;
		for (int j = i; j < input.size() && !input.get(j).equals("END"); ++j) {
			if (input.get(j).equals(")"))
				++rightCount;
			if (input.get(j).equals("("))
				++leftCount;
		}
		if (rightCount > leftCount)
			return "( is missing";
		if (leftCount > rightCount)
			return ") is missing";

		// check for , after strings
		for (int j = i; j < input.size() && !input.get(j).equals("END"); ++j) {
			if (Tokenize.classification.get(input.get(j)) == TOKEN_TYPE.STRING)
				if (j + 1 >= input.size() || !input.get(j + 1).equals(","))
					return ", is missing";
		}

		// check for missing "
		if (Tokenize.get_bad_tokens().contains("\""))
			return "\" is missing";

		// check for missing =
		boolean expectEquals = true;
		for (int j = i; j < input.size() && !input.get(j).equals("END"); ++j) {
			if (expectEquals) {
				if (j + 1 >= input.size()
						|| Tokenize.classification.get(input.get(j + 1)) == TOKEN_TYPE.ID) {
					if (j + 2 >= input.size() || !input.get(j + 2).equals("="))
						return "= is missing";
				} else
					expectEquals = false;
			} else if (input.get(j).equals(";"))
				expectEquals = true;
		}

		// check for final END and . Dont forget there is a $ added!
		if (!input.get(input.size() - 3).equals("END"))
			return "END is expected.";
		if (!input.get(input.size() - 2).equals("."))
			return ". is missing";

		return null;
	}

}
