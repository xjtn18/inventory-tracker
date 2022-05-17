package com.shopify.inventorytracker.inventory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.shopify.inventorytracker.io.DisplayTable;
import com.shopify.inventorytracker.api.ApiHandler;
import com.shopify.inventorytracker.response.WeatherResponse;
import com.shopify.inventorytracker.model.Item;
import com.shopify.inventorytracker.api.ApiHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.ResultSet;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;



@Component
public class Inventory {

	// Attributes
	@Autowired
	private JdbcTemplate jdbcTemplate; // the object we will use to interface with the SQL database


	// Methods


	public boolean hasItem(final Long itemId){
		String sql = "SELECT 1 FROM Stock WHERE id = ?";
		return jdbcTemplate.query(
			sql,
			new PreparedStatementSetter(){
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setLong(1, itemId);
				}
			},
			new ResultSetExtractor<Boolean>(){
				public Boolean extractData(ResultSet rs) throws SQLException {
					return rs.next();
				}
			}
		);
	}



	public List<Item> selectAll(){
		String sql = "SELECT * FROM Stock";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Item.class));
	}



	public Item select(final Long itemId) throws NonExistentItemException {
		String sql = "SELECT * FROM Stock WHERE id=?";

		try {
			return jdbcTemplate.query(
				sql,
				new PreparedStatementSetter(){
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1, itemId);
					}
				},
				BeanPropertyRowMapper.newInstance(Item.class)
			).get(0); // UID is primary key, resulting list should have 1 element

		} catch (IndexOutOfBoundsException obe){ // the selection was empty, ID is invalid.
			throw new NonExistentItemException("Attempted to select a non-existent item.");
		}
	}



	public void insert(final String itemName, final String itemLocation){
		String sql = "INSERT INTO Stock(name, location) VALUES(?, ?)";

		jdbcTemplate.update(
			sql,
			new PreparedStatementSetter(){
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, itemName);
					ps.setString(2, itemLocation);
				}
			}
		);
	}



	public void update(final Long itemId, final String newItemName, final String newItemLocation){
		String sql = "UPDATE Stock SET name = ?, location = ? where id = ?";

		if (jdbcTemplate.update(
			sql,
			new PreparedStatementSetter(){
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, newItemName);
					ps.setString(2, newItemLocation);
					ps.setLong(3, itemId);
				}
			}
		) == 0){
			throw new NonExistentItemException("Attempted to update a non-existent item.");
		}
	}



	public void delete(final Long itemId){
		String sql = "DELETE FROM Stock WHERE id=?";

		if (jdbcTemplate.update(
			sql,
			new PreparedStatementSetter(){
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setLong(1, itemId);
				}
			}
		) == 0){
			throw new NonExistentItemException("Attempted to remove a non-existent item.");
		}
	}



	public void processShipment(Shipment shipment){
		String sql;
		for (Long itemId : shipment.getItemIdSet()){
			try {
				this.delete(itemId);
			} catch (NonExistentItemException nie){
				System.out.printf("\nError: Could not ship item %d; item does not exist.\n", itemId);
			}
		}
	}



	/**
	 * Display the current inventory.
	 * 
	 */
	public void display(){
		List<Item> items = selectAll();
		DisplayTable table = new DisplayTable("=== Inventory ===", 3);

		// Add the column titles row
		table.addRow("UID", "Name", "Location");

		// Add all of the entries
		for (Item item : items){
			table.addRow(String.valueOf(item.getId()), item.getName(), item.getLocation());
		}

		// Print to the console
		table.print();	
	}


	/**
	 * Display the current inventory.
	 * 
	 */
	public void displayWithWeather(ApiHandler apiHandler){
		List<Item> items = selectAll();
		DisplayTable table = new DisplayTable("=== Inventory ===", 4);
		Map<String, String> weatherResultCache = new HashMap<>(); // Map to cache weather API response messages.
			// Prevents making uneccessary duplicate weather requests for the same city in one display call.

		// Add the column titles row
		table.addRow("UID", "Name", "Location", "Weather");

		// Add all of the entries
		for (Item item : items){
			String itemLocation = item.getLocation();
			if (!weatherResultCache.containsKey(itemLocation)){
				WeatherResponse weatherResponse = apiHandler.getWeatherInCity(itemLocation);
				weatherResultCache.put(
					itemLocation, weatherResponse.weather[0].main + " ~ " + weatherResponse.weather[0].description + " (" + weatherResponse.main.temp + " °F)"
				);
			}
			table.addRow(String.valueOf(item.getId()), item.getName(), item.getLocation(), weatherResultCache.get(itemLocation));
		}

		// Print to the console
		table.print();	
	}

}

