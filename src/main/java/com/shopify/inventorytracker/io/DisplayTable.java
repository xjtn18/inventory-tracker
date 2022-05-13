package com.shopify.inventorytracker.io;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;


/**
 * A class for managing a 2-column table of strings that can print to console.
 */
public class DisplayTable {


	// Attributes
	private String title;
	private int numCols;
	private List<String[]> rows;


	// Methods

	/** Constructor */
	public DisplayTable(String t, int n){
		title = t;
		numCols = n;
		rows = new ArrayList<>();
	}



	/**
	 * Removes all elements currently in the 'rows' list.
	 * @param key - The key string.
	 * @param value - The value string.
	 */
	public void addRow(String[] row){
		rows.add(row);
	}



	/**
	 * Prints the table to the console nicely formatted.
	 */
	public void print(){
		// find the widest string in each column
		int[] widestStrings = new int[numCols];
		for (String[] row : rows){
			for (int col = 0; col < numCols; ++col){
				widestStrings[col] = Math.max(widestStrings[col], row[col].length());
			}
		}

		// print a title string that will be centered above the table
		System.out.print("\n");
		int lineWidth = IntStream.of(widestStrings).sum() + (numCols+1)*5-4;
		int padding = (lineWidth - title.length())/2;
		System.out.printf("%" + (padding+title.length()) + "s\n", title);

		// set a char array to store the horizontal lines of the table
		char[] line = new char[lineWidth];
		Arrays.fill(line, 0, lineWidth, '-');

		// print the table
		System.out.println(line);
		for (int row = 0; row < rows.size(); ++row){
			for (int col = 0; col < numCols; ++col){
				System.out.printf("|  %-" + widestStrings[col] + "s  ", rows.get(row)[col]); // print row
			}
			System.out.print("|\n");
			if (row == 0) System.out.println(line); // if this is the column title row
		}
		System.out.println(line);
	}

}
