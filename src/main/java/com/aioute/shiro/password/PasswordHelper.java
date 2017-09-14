package com.aioute.shiro.password;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

@Service
public class PasswordHelper {

	public static void main(String[] args) {
		new PasswordHelper().encryptPassword(null,"123456");
	}
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	public String encryptPassword(String salt, String password) {

		if (salt == null) {
			salt = randomNumberGenerator.nextBytes().toHex();
		}

		String newPassword = new SimpleHash("md5", password, ByteSource.Util.bytes(salt), 4).toHex();

		return salt + newPassword;
	}

	public String encryptIMPassword(String source) {
		if (source != null && source.length() == 11) {
			String password = encryptPassword(null, source).substring(0, 21);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < 10; i++) {
				sb.append(password.substring(i * 2, i * 2 + 2));
				sb.append(source.substring(i, i + 1));
			}
			sb.append(password.substring(20)).append(source.substring(10));
			return sb.toString();
		} else if (source != null && source.length() == 64) {
			return encryptPassword(null, source).substring(0, 32);
		}
		return null;
	}

}
