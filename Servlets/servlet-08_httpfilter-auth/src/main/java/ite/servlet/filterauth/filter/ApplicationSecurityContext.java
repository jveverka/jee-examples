package ite.servlet.filterauth.filter;

import java.security.Principal;
import java.util.List;

import javax.ws.rs.core.SecurityContext;

public class ApplicationSecurityContext implements SecurityContext {
	
	private String userName;
	private List<String> roles;
	private String scheme;
	
	public ApplicationSecurityContext(String userName, List<String> roles, String scheme) {
		this.userName = userName;
		this.roles = roles;
		this.scheme = scheme;
	}

	@Override
	public Principal getUserPrincipal() {
		return new Principal() {
			@Override
			public String getName() {
				return userName;
			}
		};
	}

	@Override
	public boolean isUserInRole(String role) {
		return roles.contains(role);
	}

	@Override
	public boolean isSecure() {
		return "https".equals(this.scheme);
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

}
