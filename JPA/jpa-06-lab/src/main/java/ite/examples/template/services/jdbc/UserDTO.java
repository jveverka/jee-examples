package ite.examples.template.services.jdbc;

public class UserDTO {

	private Long id;
	private String name;
	
	public UserDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
}
