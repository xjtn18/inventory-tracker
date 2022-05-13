package com.shopify.inventorytracker.io;

import java.util.Scanner;



/**
 * Handles the program's main console input and output operations.
 */ 
public class ConsoleIO {

	// Attributes
	private final Scanner scanner;



	// Methods

	/** Constructor */
	public ConsoleIO(){
		scanner = new Scanner(System.in); // allocate the scanner for user input
	}



	/**
	 * Prints given string to the console and a new line.
	 * @param text - The string to be printed.
	 */
	public void log(String text){
		System.out.println(text);
	}



	/**
	 * Prints prompt and returns a line of input from the user with leading & trailing whitespace removed.
	 * @param prompt - The prompt message string
	 */
	public String promptForInput(String prompt){
		System.out.print(prompt + " ");
		return scanner.nextLine().trim();
	}


	/**
	 * Prints prompt and takes only a non-empty input from the user.
	 * @param prompt - The prompt message string
	 */
	public String promptForNonEmptyInput(String prompt){
		String input;
		while ((input = promptForInput(prompt)).equals("")){
			log("\nError: Please provide a non-empty string.");
		}
		return input;
	}



	/**
	 * Prompts for integer in the specified range as input from user through the command line.
	 * 
	 * @param prompt - The prompt message string
	 * @param start - Start of the valid range
	 * @param end - End of the valid range
	 * @return int representing the user's choice
	 * @throws InputOutOfRangeException - if the user doesn't provide a number within the given range
	 */
	public int promptForNumberInRange(String prompt, int start, int end) {
		int choice;
		while (true){
			try {
				choice = Integer.parseInt(promptForInput(prompt)); // grab input from command line, convert to int
				if (choice < start || choice > end){
					log("\nError: Please enter a number within the range " + start + " and " + end + " inclusive.");
				} else {
					break;
				}
			} catch (NumberFormatException nfe){
				log("\nError: Please enter a valid number.");
			}
		}

		return choice;
	}



	/**
	 * Prompts for integer in the specified range as input from user through the command line.
	 * 
	 * @param prompt - The prompt message string
	 * @return Long representing the user's choice
	 * @throws InputOutOfRangeException - if the user doesn't provide a number within the given range
	 */
	public Long promptForLong(String prompt) {
		Long choice;
		while (true){
			try {
				choice = Long.parseLong(promptForInput(prompt)); // grab input from command line, convert to int
				break;
			} catch (NumberFormatException nfe){
				log("\nError: Please enter a valid number.");
			}
		}

		return choice;
	}



	/**
	 * Prints the main menu of the options that the user can choose from.
	 */
	public void printMainMenu(){
		log("\n\n-------------------- Menu --------------------");
		log("[0] << Quit");
		log("[1] Add item");
		log("[2] Edit item");
		log("[3] Delete item");
		log("[4] Display inventory");
	}



	public void printLocationOptions(String[] options){
		log("");
		for (int i = 0; i < options.length; ++i){
			log("[" + (i+1) + "] " + options[i]);
		}
	}


}
