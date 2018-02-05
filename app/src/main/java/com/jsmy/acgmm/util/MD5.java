package com.jsmy.acgmm.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	private MD5() {}

	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public final static String getMessageDigest(byte[] buffer) {

		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	public static String md5(String string) {
		byte[] encodeBytes = null;
		try {
			encodeBytes = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException neverHappened) {
			throw new RuntimeException(neverHappened);
		} catch (UnsupportedEncodingException neverHappened) {
			throw new RuntimeException(neverHappened);
		}

		return toHexString(encodeBytes);
	}

	public static String toHexString(byte[] bytes) {
		if (bytes == null) return "";
		StringBuilder hex = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			hex.append(hexDigits[(b >> 4) & 0x0F]);
			hex.append(hexDigits[b & 0x0F]);
		}
		return hex.toString();
	}
}
