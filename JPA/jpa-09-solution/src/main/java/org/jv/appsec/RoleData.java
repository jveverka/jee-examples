package org.jv.appsec;

import java.util.Map;

public class RoleData {

	private String name;
	private Map<String, Boolean> properties;
	
	public RoleData(String name, Map<String, Boolean> properties) {
		super();
		this.name = name;
		this.properties = properties;
	}

	public String getName() {
		return name;
	}
	
	public Map<String, Boolean> getProperties() {
		return properties;
	}
	
	public boolean isPropertyActive(String propertyName) {
		Boolean value = properties.get(propertyName);
		if (value != null) {
			return value.booleanValue();
		}
		return false;
	}
	
}
