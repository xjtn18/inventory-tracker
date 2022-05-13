package com.shopify.inventorytracker.inventory;


/**
 * Simple exception class for number inputs from the user that are outside the range of possible values.
 */
public class NonExistantItemException extends RuntimeException {

    public NonExistantItemException(String msg){
        super(msg);
    }

}

