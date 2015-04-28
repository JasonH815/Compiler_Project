import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cpsc323.compiler.tokenize.Tokenize;

/**
 * 
 * Author: Sean Callahan & Jason Hellwig
 * 
 * Project: Compiler Package:
 * 
 * Date:17/11/2013
 * 
 * File: final_project.java
 * 
 * Todo:
 */

public class Final_project {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Translator someTranslator = new Translator();
		Lr_parser someParser = new Lr_parser();

		String filen = "input.txt";

		// Read in our file. Make a list with newlines and without.
		List<String> tokens = Tokenize.tokenizeFile(new File(filen), true);
		List<String> tokens_no_newlines = new ArrayList<String>();
		for (String token : tokens) {
			if (!token.equals("\n"))
				tokens_no_newlines.add(token);
		}

		// output of the tokenizer (Answer to question 1)
		System.out.println("Tokenized input:\n");
		for (String token : tokens)
			System.out.print(token + " ");
		System.out.println("\n");

		// // output of the tokenizer without newlines --for debugging
		// System.out.println("\n\n\n\n");
		// for (String token : tokens_no_newlines)
		// System.out.println(token + "     "
		// + Tokenize.classification.get(token));

		// Checks for invalid language being used before parsing the grammar
		// if (!Tokenize.get_bad_tokens().isEmpty()) {
		// System.out.println("Invalid language used:");
		// for (String token : Tokenize.get_bad_tokens()) {
		// System.out.println("\t" + token);
		// }
		// return;
		// }
		// if grammar is OK, else show errors
		if (someParser.parse_grammar(tokens_no_newlines)) {
			someParser.output_parser_history();
			System.out.println("\n\n"
					+ someTranslator.translate(tokens_no_newlines) + "\n\n");
		} else {
			// someParser.semicolon_end_check(tokens);
			// someParser.essential_words_check(tokens_no_newlines);
			//
			// someParser.comma_var_check(tokens);
			//
			// someParser.print_check(tokens);
			// someParser.fullstop_check(tokens);

			// jason's check
			Lr_parser.check_for_errors(tokens_no_newlines);

		}

	}

}
