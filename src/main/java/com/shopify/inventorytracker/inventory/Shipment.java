package com.shopify.inventorytracker.inventory;


import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



/**
 * Simple wrapper class to store a vector of item ID's.
 * The IDs correspond to items that we are sending out as a shipment.
 * In the future this class could expand and contain more functionality.
 */
public class Shipment {

	// Attributes
	private Set<Long> itemIdSet;


	// Methods

	// Default constructor
	public Shipment(){
		itemIdSet = new HashSet<Long>();
	}

	// Constructor that takes preset item id list
	public Shipment(Set<Long> idSet){
		itemIdSet = idSet;
	}


	public void assign(Long itemId){
		itemIdSet.add(itemId);
	}

	public Set<Long> getItemIdSet(){
		return itemIdSet;
	}


}
