import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author Sean Callahan, Jason Hellwig
 */
public class Translator {

	String translate(List<String> input) throws FileNotFoundException,
			IOException {

		// the final output
		StringBuilder adjusted = new StringBuilder();

		// This is used to hold program name
		String programName = "";

		// working temp var
		String nextLine = "";

		// This at the top of every .cpp
		adjusted.append("\n#include <iostream> \nusing namespace std; \nint main()\n{\n");

		// While nextline is not null
		for (int q = 0; q < input.size(); q++) {

			nextLine = input.get(q);

			// Switch statement based on what is in the arraylist.
			switch (nextLine) {
			// if program, then get the next on the list which will be the name,
			// then discard the semicolon
			case "PROGRAM":

				programName = input.get(q + 1);
				q = q + 2; // we dont need the semicolon.
				break;

			case "VAR":
				// we need everything AFTER the VAR until :
				adjusted.append("int "); // We only have integers in this
											// assignment.
				q++; // skip over VAR
				nextLine = input.get(q);
				q++; // Make sure this first var does not repeat
				while (!nextLine.contains(":")) {

					adjusted.append(nextLine + " ");
					nextLine = input.get(q);
					q++;
				}

				break;
			// PRINT CASE
			case "PRINT":
				adjusted.append("cout<<");
				q++;
				nextLine = input.get(q);
				while (!nextLine.contains(";")) {

					if (nextLine.contains(",")) {
						adjusted.append("<<");
					} else if (nextLine.contains("(") || nextLine.contains(")")) {
						// do nothing
					} else {
						adjusted.append(nextLine);
					}
					nextLine = input.get(q);
					q++;
				}
				adjusted.append("; \n");
				q--;
				break;

			case "BEGIN":
				// Do nothing with this.
				break;

			case "END":
				// Should never reach this.
				break;

			case ".":
				break;

			case "$":
				break;

			// This means its a variable
			default:
				if (!nextLine.equals(";")) {
					adjusted.append(nextLine);

					// if (q < input.size())
					// nextLine = input.get(q);
				} else
					adjusted.append("; \n");

			}

		}
		adjusted.append("return 0; \n}");
		String translated = adjusted.toString();

		try {
			// Create file
			// TODO: Program name
			FileWriter fstream = new FileWriter(programName + ".cpp");
			try (BufferedWriter out = new BufferedWriter(fstream)) {
				out.write(translated);
			}
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		return translated;
	}

	String trimMulti(String alpha) {
		// //Trim, twice. 2nd time for multiple whitepsaces in a row.
		alpha = alpha.trim();
		alpha = alpha.trim().replaceAll("\\s+", " ");

		return alpha;
	}

}
