package com.aioute.shiro.password;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

@Service
public class PasswordHelper {

	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	public String encryptPassword(String salt, String password) {

		if (salt == null) {
			salt = randomNumberGenerator.nextBytes().toHex();
		}

		String newPassword = new SimpleHash("md5", password, ByteSource.Util.bytes(salt), 4).toHex();

		return salt + newPassword;
	}

}
