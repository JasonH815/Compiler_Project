package com.cpsc323.compiler.tokenize;

/**
 * 
 * Author: @author
 * Project: Compiler
 * Package:
 * Date:17/11/2013
 * File: Tokenizer.java
 * Todo: TODO
 */

/**
 * @author Sean
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizes a given File or String into c++ tokens.
 * 
 * @author jason
 * 
 */
public class Tokenize {

	// traces the tokenizer output
	private static final boolean showDebugMessages = false;

	private static Matcher m;

	// token groups
	public static final Pattern IDENTIFIERS = Pattern.compile("[a-f][a-f0-9]*");

	public static final Pattern NUMBERS = Pattern.compile("[0-9]+");

	public static final Pattern STRINGS = Pattern
			.compile("\"([^\\\\]|\\.)*?\"");

	public static final Pattern COMMENTS = Pattern
			.compile("(?s)\\(\\*.*?\\*\\)\\n*"); // dotall

	public static final Pattern SPECIAL_CHARS = Pattern
			.compile("\\(|\\)|\\+|-|\\*|/|=|,|;|:|\\.");

	public static final Pattern RESERVED_WORDS = Pattern
			.compile("\\w*?PROGRAM\\w*|\\w*?VAR\\w*|\\w*?BEGIN\\w*|\\w*?END\\w*|\\w*?INTEGER\\w*|\\w*?PRINT\\w*");

	public static final Pattern NEWLINES = Pattern.compile("\\n+");

	public static final Pattern WHITESPACE = Pattern.compile("\\s+");

	public static final Pattern LEFTOVER_WORDS = Pattern.compile("\\w+");

	public static final Pattern LEFTOVER_CHARS = Pattern.compile("(?s)."); // dot
																			// all

	public static final Pattern[] patterns = { COMMENTS, STRINGS, NUMBERS,
			RESERVED_WORDS, IDENTIFIERS, SPECIAL_CHARS, NEWLINES, WHITESPACE,
			LEFTOVER_WORDS, LEFTOVER_CHARS };

	public static final HashMap<String, TOKEN_TYPE> classification = new HashMap<>();

	public static enum TOKEN_TYPE {
		COMMENT, STRING, NUMBER, ID, SPECIAL, RES_WORD, NEWLINE, WHITESPACE, NORM
	}

	/**
	 * Tokenizes a list of strings line by line into c++ tokens
	 * 
	 * @param f
	 * @return A list of string representing the tokens
	 * @throws FileNotFoundException
	 */
	public static ArrayList<String> tokenize(String input, boolean newlines) {
		ArrayList<String> tokens = new ArrayList<>();

		// check if theres is input to tokenize
		if (input == null || input.trim().isEmpty())
			return tokens;

		MatchResult result = null;

		// get the remaining tokens in the input
		while (!input.isEmpty()) {
			result = getMatch(input);
			if (result == null) {
				// add whatever didn't get matched to the tokens list. The
				// parser will handle the bad input
				if (!input.equals("\n")) {
					tokens.add(input);
					classification.put(input, TOKEN_TYPE.NORM);
				}
			}

			input = result.getRemainder();

			// keep only non-comments and non-whitepsace and non-newlines
			if (classification.get(result.getToken()) != TOKEN_TYPE.COMMENT
					&& classification.get(result.getToken()) != TOKEN_TYPE.WHITESPACE
					&& classification.get(result.getToken()) != TOKEN_TYPE.NEWLINE)
				tokens.add(result.getToken());

			// add newlines if switch turned on and add only a single newline if
			// multiple in a row are encountered (removes blank lines)
			if (newlines
					&& classification.get(result.getToken()) == TOKEN_TYPE.NEWLINE)
				tokens.add("\n");

		}
		return tokens;
	}

	/**
	 * Split a file into a list of String representing lines and tokenizes it.
	 * 
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 */
	public static ArrayList<String> tokenizeFile(File f, boolean newlines) {

		try {
			Scanner scanner = new Scanner(f);

			// scan the input file
			ArrayList<String> input = new ArrayList<>();
			while (scanner.hasNextLine()) {
				input.add(scanner.nextLine());
			}
			scanner.close();

			// convert the input to a string
			StringBuilder sb = new StringBuilder();
			for (String line : input) {
				sb.append(line);
				if (newlines)
					sb.append("\n");
			}
			String inputString = sb.toString();

			// tokenize the input
			return tokenize(inputString, newlines);

		} catch (Exception e) {
			System.out
					.println("Problem reading or converting input file\n" + e);
			return null;
		}

	}

	/**
	 * Tokenizes a string of input.
	 * 
	 * @param input
	 * @return
	 */
	public static ArrayList<String> tokenizeString(String input) {
		return tokenize(input, false);
	}

	// matches part of a line and returns the resulting token and remaining line
	public static MatchResult getMatch(String line) {

		MatchResult result = null;

		// match one of the other patterns in the order specified in the array
		int i = 0;
		while (result == null && i < patterns.length) {
			result = matchPattern(patterns[i], line);
			++i;
		}

		// classify the token type (will need to be used later on)
		TOKEN_TYPE type = null;
		switch (i) {
		case 1:
			type = TOKEN_TYPE.COMMENT;
			break;
		case 2:
			type = TOKEN_TYPE.STRING;
			break;
		case 3:
			type = TOKEN_TYPE.NUMBER;
			break;
		case 4:
			type = TOKEN_TYPE.RES_WORD;
			break;
		case 5:
			type = TOKEN_TYPE.ID;
			break;
		case 6:
			type = TOKEN_TYPE.SPECIAL;
			break;
		case 7:
			type = TOKEN_TYPE.NEWLINE;
			break;
		case 8:
			type = TOKEN_TYPE.WHITESPACE;
			break;
		default:
			type = TOKEN_TYPE.NORM;
			break;
		}
		classification.put(result.getToken(), type);
		return result;
	}

	/**
	 * Matches a pattern in a string. returns a struct representing the matched
	 * string and the remaining string after the match
	 * 
	 * @param p
	 * @param input
	 * @return
	 */
	public static MatchResult matchPattern(Pattern p, String input) {

		// match the input
		m = p.matcher(input);
		boolean hasMatch = m.lookingAt();

		// split input into match + remainder
		if (hasMatch) {
			int index = m.end();
			String match = input.substring(0, index);

			if (showDebugMessages) {
				System.out.print("###SUCCESS###" + p.toString() + "###   ->");// debug
				System.out.println(match + "<-");// debug
			}
			String remainder = input.substring(index);
			return new MatchResult(match, remainder);

		} else {
			if (showDebugMessages) {
				System.out.print(p.toString() + "###   ->");// debug
				System.out.println(input + "<-");// debug
			}
			return null;
		}
	}

	/**
	 * Returns a listing of all the invalid tokens that the tokenizer has
	 * currently seen. Note that this only collects values the tokenizer has
	 * already seen, so an input must be tokenized first to collect invalid
	 * tokens.
	 * 
	 * @return
	 */
	public static List<String> get_bad_tokens() {
		Set<String> keys = classification.keySet();
		List<String> result = new ArrayList<String>();
		for (String key : keys) {
			if (classification.get(key) == TOKEN_TYPE.NORM)
				result.add(key);
		}
		return result;
	}

}
