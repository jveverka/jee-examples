package org.jv.appsec.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Id;

import org.jv.appsec.RoleData;

@Entity
@Table(name="APPSEC_ROLES")
public class Role {
	
	@Id
	private String name;
	
	@ElementCollection
	@CollectionTable(name="APPSEC_ROLE_PROPERTIES")
	@MapKeyColumn(name="name")
	@Column(name="active")
	private Map<String, Boolean> properties;
	
	public Role() {
	}

	public Role(String name, Map<String, Boolean> properties) {
		super();
		this.name = name;
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, Boolean> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Boolean> properties) {
		this.properties = properties;
	}

	public RoleData getRoleData() {
		return new RoleData(name, new HashMap<String, Boolean>(properties));
	}

}
