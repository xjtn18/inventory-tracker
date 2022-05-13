package com.shopify.inventorytracker.model;

import java.util.Objects;


public class Item {

	private Long id;
	private String name;
	private String location;

	public Item(){
	}

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getLocation(){
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public boolean equals(Object o){
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Item item = (Item) o;
		return Objects.equals(location, item.location) &&
				Objects.equals(id, item.id) &&
				Objects.equals(name, item.name);
	}

	@Override
	public int hashCode(){
		return Objects.hash(id);
	}

	@Override
	public String toString(){
		final StringBuilder sb = new StringBuilder("Item{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append(", location=").append(location);
		sb.append('}');
		return sb.toString();
	}
}
