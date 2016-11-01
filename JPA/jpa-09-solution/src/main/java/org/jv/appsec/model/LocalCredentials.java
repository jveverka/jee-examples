package org.jv.appsec.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="APPSEC_LOCALCREDENTIALS")
public class LocalCredentials {
	
	@Id
	private String userId;
	private String hashAlgorythm;
	private String salt;
	private String passwdHash;
	

	public LocalCredentials() {
	}

	public LocalCredentials(String userId, String hashAlgorythm, String salt,
			String passwdHash) {
		super();
		this.userId = userId;
		this.hashAlgorythm = hashAlgorythm;
		this.salt = salt;
		this.passwdHash = passwdHash;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHashAlgorythm() {
		return hashAlgorythm;
	}

	public void setHashAlgorythm(String hashAlgorythm) {
		this.hashAlgorythm = hashAlgorythm;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPasswdHash() {
		return passwdHash;
	}

	public void setPasswdHash(String passwdHash) {
		this.passwdHash = passwdHash;
	}
	
}
