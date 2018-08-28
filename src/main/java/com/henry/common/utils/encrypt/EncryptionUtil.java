package com.henry.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;

public class EncryptionUtil {
	
	public static final String DECRYPT_KEY="Cherrypicks-@)!#";
	private static final String ALGO = "AES";
	private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

	public static String encrypt(String Data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = Base64.encodeBase64String(encVal);
		return encryptedValue;
	}

	public static String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.decodeBase64(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}

	public static String encrypt(String Data, String keystr) throws Exception {
		byte[] keybyte = keystr.getBytes();
		Key key = new SecretKeySpec(keybyte, ALGO);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		String encryptedValue = Base64.encodeBase64String(encVal);
		return encryptedValue;
	}

	public static String decrypt(String encryptedData, String keystr) throws Exception {
		byte[] keybyte = keystr.getBytes();
		Key key = new SecretKeySpec(keybyte, ALGO);
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.decodeBase64(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}

	public static void main(String[] args) {
		try {
			String pTest = EncryptionUtil.encrypt("accountType=0&userAccount=timchan@cherrypicks.com&device=1&password=iamtim&nickName=林国强&deviceVerNum=1.0&", DECRYPT_KEY);
			System.out.println(pTest);
			pTest = URLEncoder.encode(pTest, "UTF-8");
			//System.out.println(pTest);

			//String decryptTest = EncryptionUtil.decrypt("IyK3cNGdaW/N9lGq1G5wCl0dURLJaKtW3y4Jc1mlVIFyiBD03eWEb3fBajFG8obcWoZKxDKAaBND3k/HrT59KVSk697wMoEhtlH/Qtqvpk6Xubfy5rG6F1uLPg3n9TOQ2dQLgn1UEbLr/xFI9iWLgm6o2aLTA4bRdULWh/yY5FE=", Constants.DECRYPT_KEY);
			pTest = URLDecoder.decode(pTest, "UTF-8");
			//pTest = URLDecoder.decode(pTest, "UTF-8");
			//pTest = URLDecoder.decode("jyacHRbbTJjiwV%2FvIVf1E7K0iDWSBWiEWayaZ7DTOS1nskQiuqCY8LP2tpg8a4Y4YOQShMuh1rYv0T2cWV2%2BAoE5bXfMAuMQ0tQdLKdsQwJrMR9C9Vwy5RiJyxr5%0A34YgH4ASu%2B2GdlTI8hvKPZZqT9bdp8yEd6SMsHE28iO8umY%3D", "UTF-8");
			String decryptTest = EncryptionUtil.decrypt(pTest, DECRYPT_KEY);
			System.out.println(decryptTest);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
