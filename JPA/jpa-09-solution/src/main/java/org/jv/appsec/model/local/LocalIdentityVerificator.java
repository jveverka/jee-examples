package org.jv.appsec.model.local;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Logger;

import org.jv.appsec.model.LocalCredentials;

public class LocalIdentityVerificator {
	
	private static final Logger logger = Logger.getLogger(LocalIdentityVerificator.class.getName());
	
	private static final String MD_ALGO = "SHA-256";
	private static final String CHARSET = "UTF-8";
	private static final int SEED_LENGTH = 32;
	
	public static boolean verify(LocalCredentials credentials, String password) throws NoSuchAlgorithmException {
		String message = credentials.getSalt() + password;
		MessageDigest md = MessageDigest.getInstance(credentials.getHashAlgorythm());
		byte[] digestBytes = md.digest(message.getBytes(Charset.forName(CHARSET)));
		String passwdHash = new String(Base64.getEncoder().encode(digestBytes), Charset.forName(CHARSET));
		if (passwdHash.equals(credentials.getPasswdHash())) {
			return true;
		}
		return false;
	}
	
	public static LocalCredentials generateCredentials(String userId, String password) throws NoSuchAlgorithmException {
		LocalCredentials localCredentials = new LocalCredentials();
		localCredentials = updateCredentials(password, localCredentials);
		localCredentials.setUserId(userId);
		return localCredentials;
	}

	public static LocalCredentials updateCredentials(String newPassword, LocalCredentials localCredentials) throws NoSuchAlgorithmException {
		SecureRandom random = new SecureRandom();
		String seed = new String(Base64.getEncoder().encode(random.generateSeed(SEED_LENGTH)), Charset.forName(CHARSET));
		String message = seed + newPassword;
		MessageDigest md = MessageDigest.getInstance(MD_ALGO);
		byte[] digestBytes = md.digest(message.getBytes(Charset.forName(CHARSET)));
		String passwdHash = new String(Base64.getEncoder().encode(digestBytes), Charset.forName(CHARSET));
		localCredentials.setHashAlgorythm(MD_ALGO);
		localCredentials.setSalt(seed);
		localCredentials.setPasswdHash(passwdHash);
		return localCredentials;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String userName = "juraj";
		String password = "password";
		LocalCredentials lc = LocalIdentityVerificator.generateCredentials(userName, password);
		logger.info("local credentials for " + userName + ": ALGO=" + lc.getHashAlgorythm() + " salt=" + lc.getSalt() + " pwdhash=" + lc.getPasswdHash());
	}

}
