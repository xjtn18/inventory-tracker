package com.shopify.inventorytracker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.shopify.inventorytracker.io.*;
import com.shopify.inventorytracker.inventory.*;
import com.shopify.inventorytracker.api.ApiHandler;
import com.shopify.inventorytracker.model.Item;




@SpringBootApplication
public class InventoryTrackerApplication implements CommandLineRunner {
	@Autowired private ApplicationContext context;
	private ConsoleIO consoleIO;
	private ApiHandler apiHandler;
	private Inventory inventory;
	// 5 hard coded storage locations
	private final String[] storageLocations = new String[]{"Las Vegas", "Ottawa", "New York", "London", "Singapore"};


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
				int choice = consoleIO.promptForNumberInRange("\nEnter a number to select an option:", 0, 5);

				switch (choice){
					case 0: { // Quit
						looping = false;
						consoleIO.log("Exiting...");
						break;


					} case 1: { // Add to inventory
						String itemName = consoleIO.promptForNonEmptyInput("\nEnter the item name:");

						consoleIO.printAsMenu(storageLocations);
						int itemLocationNumber = consoleIO.promptForNumberInRange("\nEnter a number to select a location:", 1, 5);
						String itemLocation = storageLocations[itemLocationNumber-1];

						inventory.insert(itemName, itemLocation);

						System.out.printf("\nSuccessfully added %s (%s) to inventory.\n", itemName, itemLocation);
						break;


					} case 2: { // Edit inventory
						inventory.display(); // display the inventory so user can see what items they want to edit.

						Long itemId = consoleIO.promptForLong("\nEnter the ID of the item you want to edit:");
						String newItemName = consoleIO.promptForNonEmptyInput("\nEnter the new name for item " + String.valueOf(itemId) + ":");

						consoleIO.printAsMenu(storageLocations);
						int newItemLocationNumber = consoleIO.promptForNumberInRange("\nEnter a number to select a new location:", 1, 5);

						inventory.update(itemId, newItemName, storageLocations[newItemLocationNumber-1]);

						System.out.printf("\nSuccessfully updated item %d in inventory.\n", itemId);
						break;


					} case 3: { // Remove from inventory
						inventory.display(); // display the inventory so user can see what they want to remove.
						Long itemId = consoleIO.promptForLong("\nEnter the ID of the item you want to remove:");

						inventory.delete(itemId);

						System.out.printf("\nSuccessfully removed item %d from inventory.\n", itemId);
						break;


					} case 4: { // Create a shipment
						inventory.display(); // display the inventory so user can see what items they want to ship.
						String itemIdStrings = consoleIO.promptForNonEmptyInput("\nEnter comma seperated list of IDs in the shipment:");

						Set<Long> itemIds = Stream.of(itemIdStrings.split(","))
							.map(String::trim)
							.map(Long::parseLong)
							.collect(Collectors.toSet());
						inventory.processShipment(new Shipment(itemIds));
						System.out.println("\nFinished processing shipment.");

						break;


					} case 5: { // Print current inventory table to the console
						inventory.displayWithWeather(apiHandler);
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

			} catch (NonExistentItemException nie){
				consoleIO.log("\nError: " + nie.getMessage());

			} catch (NumberFormatException nfe){
				consoleIO.log("\nError: Invalid item ID provided in shipment.");

			} catch (Exception e){
				consoleIO.log("\nError: " + e.getMessage());
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

