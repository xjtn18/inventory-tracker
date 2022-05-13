package com.shopify.inventorytracker;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.SQLException;

import com.shopify.inventorytracker.io.*;
import com.shopify.inventorytracker.api.ApiHandler;
import com.shopify.inventorytracker.model.Item;
import com.shopify.inventorytracker.inventory.*;




@SpringBootApplication
//@ComponentScan("com.shopify.inventorytracker")
public class InventoryTrackerApplication implements CommandLineRunner {
	@Autowired private ApplicationContext context;
	private ConsoleIO consoleIO;
	private ApiHandler apiHandler;
	private Inventory inventory;


	/**
	 * The main menu loop
	 */
	public void mainMenuLoop() {
		boolean looping = true;

		// Main program command loop
		while (looping){
			// display menu options to the user
			consoleIO.printMainMenu();

			try {
				// get user's selected option
				int choice = consoleIO.promptForNumberInRange("\nEnter a number to select an option:", 0, 4);

				switch (choice){
					case 0: { // Quit
						looping = false;
						consoleIO.log("Exiting...");
						break;


					} case 1: { // Add to inventory
						String itemName = consoleIO.promptForNonEmptyInput("\nEnter the item name:");
						// 5 hard coded storage locations
						String[] storageLocations = new String[]{"Las Vegas", "Ottawa", "New York", "London", "Singapore"};

						consoleIO.printLocationOptions(storageLocations);
						int itemLocationNumber = consoleIO.promptForNumberInRange("\nEnter a number to select a location:", 1, 5);
						String itemLocation = storageLocations[itemLocationNumber-1];

						inventory.insert(itemName, itemLocation);
						System.out.printf("\nSuccessfully added %s (%s) to inventory.\n", itemName, itemLocation);
						break;


					} case 2: { // Edit inventory
						break;


					} case 3: { // Remove from inventory
						inventory.display(apiHandler); // display the inventory so user can see what they want to remove.
						Long itemId = new Long(-1);
						inventory.delete(itemId);
						Item removedItem;
						while (true){
							try {
								itemId = consoleIO.promptForLong("\nEnter the ID of the item you want to remove:");
								removedItem = inventory.select(itemId);
								break;

							} catch (NonExistantItemException nie){
								consoleIO.log(nie.getMessage());
							}
						}
						inventory.delete(itemId);
						System.out.printf("\nSuccessfully removed %s (%s) from inventory.", removedItem.getName(), removedItem.getLocation());
						break;


					} case 4: { // Print current inventory table to the console
						inventory.display(apiHandler);
						consoleIO.promptForInput("\n[press 'Enter' to continue]");
						break;
					}
				}

			} catch (WebClientResponseException e){
				int statusCode = e.getRawStatusCode();
				if (statusCode >= 400 && statusCode < 500){
					consoleIO.log("\nError: Invalid or unknown query.");
				} else if (statusCode >= 500 && statusCode < 600){
					consoleIO.log("\nServer error occurred. Please try again later.");
				} else {
					consoleIO.log("\nError: Unknown network error.");
				}

			} catch (Exception e){
				consoleIO.log("\nError: Unknown problem occurred.");
				consoleIO.log(e.getMessage());
			}

		}
	}


	@Override
	public void run(String... args) throws Exception {
		consoleIO = new ConsoleIO();
		// Get the api handler and inventory from the application context since
		// 		they were injected with the application & database properties
		//		at program start ::
		apiHandler = context.getBean(ApiHandler.class);
		inventory = context.getBean(Inventory.class); 

		mainMenuLoop();

		System.exit(0);
	}


	public static void main(String[] args) throws Exception {
		SpringApplication.run(InventoryTrackerApplication.class, args);
	}
}

