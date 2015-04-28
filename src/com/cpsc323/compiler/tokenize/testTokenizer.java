package com.cpsc323.compiler.tokenize;

import java.io.File;
import java.util.List;

public class testTokenizer {

	public static void main(String[] args) {
		List<String> tokens = Tokenize.tokenizeFile(new File("input.txt"),
				false);
		for (String token : tokens)
			System.out.println(token + "     "
					+ Tokenize.classification.get(token));

		System.out.println("\nbad tokens: " + Tokenize.get_bad_tokens());

	}

}
