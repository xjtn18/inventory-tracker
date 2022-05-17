package com.shopify.inventorytracker.inventory;


/**
 * Simple exception class for number inputs from the user that are outside the range of possible values.
 */
public class NonExistentItemException extends RuntimeException {

    public NonExistentItemException(String msg){
        super(msg);
    }

}

