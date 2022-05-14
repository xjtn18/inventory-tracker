package com.shopify.inventorytracker.inventory;


import java.util.List;
import java.util.ArrayList;



/**
 * Simple wrapper class to store a vector of item ID's.
 * The IDs correspond to items that we are sending out as a shipment.
 * In the future this class could expand and contain more functionality.
 */
public class Shipment {

	// Attributes
	private List<Long> itemIdList;


	// Methods

	// Default constructor
	public Shipment(){
		itemIdList = new ArrayList<Long>();
	}

	// Constructor that takes preset item id list
	public Shipment(List<Long> idList){
		itemIdList = idList;
	}


	public void assign(Long itemId){
		itemIdList.add(itemId);
	}

	public List<Long> getItemIdList(){
		return itemIdList;
	}


}
