package com.neusoft.cas.domain;

public class Unit {

	private String unit_id;
	private String unit_name;
	
	public Unit(String unit_id, String unit_name) {
		this.unit_id = unit_id;
		this.unit_name = unit_name;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getUnit_name() {
		return unit_name;
	}
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}
	
	@Override
	public String toString() {
		return unit_name;
	}
}
