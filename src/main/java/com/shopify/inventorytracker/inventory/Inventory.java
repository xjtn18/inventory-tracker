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

	public List<Item> selectAll(){
		String sql = "SELECT * FROM Stock";
		return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Item.class));
	}



	public Item select(final Long itemId) throws NonExistantItemException {
		String sql = "SELECT * FROM Stock WHERE id=?";

		try {
			return jdbcTemplate.query(
				sql,
				new PreparedStatementSetter(){
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1, itemId);
					}
				},
				BeanPropertyRowMapper.newInstance(Item.class))
			.get(0); // UID is primary key, resulting list should have 1 element

		} catch (IndexOutOfBoundsException obe){ // the selection was empty, ID is invalid.
			throw new NonExistantItemException("Error: Non-existant item ID was looked up.");
		}
	}



	public void insert(final String itemName, final String itemLocation){
		String sql = "INSERT INTO Stock(name, location) VALUES(?, ?)";
		jdbcTemplate.execute(sql, new PreparedStatementCallback<Void>(){  
			@Override public Void doInPreparedStatement(PreparedStatement ps)
			throws SQLException, DataAccessException
			{
				ps.setString(1, itemName);
				ps.setString(2, itemLocation);
				ps.execute();
				return null;
			}
		});
	}



	public void delete(final Long itemId){
		String sql = "DELETE FROM Stock WHERE id=?";
		jdbcTemplate.execute(sql, new PreparedStatementCallback<Void>(){  
			@Override public Void doInPreparedStatement(PreparedStatement ps)
			throws SQLException, DataAccessException
			{
				ps.setLong(1, itemId);
				ps.execute();
				return null;
			}
		});
	}



	/**
	 * Display the current inventory.
	 * 
	 */
	public void display(ApiHandler apiHandler){
		List<Item> items = selectAll();
		DisplayTable table = new DisplayTable("=== Inventory ===", 4);
		Map<String, String> weatherResultCache = new HashMap<>(); // Map to cache weather API response messages.
			// Prevents making uneccessary duplicate weather requests for the same city in one display call.

		// Add the column titles row
		table.addRow(new String[]{"UID", "Name", "Location", "Weather"});

		// Add all of the entries
		for (Item item : items){
			if (!weatherResultCache.containsKey(item.getLocation())){
				WeatherResponse weatherResponse = apiHandler.getWeatherInCity(item.getLocation());
				weatherResultCache.put(item.getLocation(), weatherResponse.weather[0].main + " ~ " + weatherResponse.weather[0].description + " (" + weatherResponse.main.temp + " °F)");
			}
			table.addRow(new String[]{String.valueOf(item.getId()), item.getName(), item.getLocation(), weatherResultCache.get(item.getLocation())});
		}

		// Print to the console
		table.print();	
	}

}

