package com.henry.common.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;


public class AesUtil {

    public static final String default_charset = "utf-8";
    public static final String key = "0102030405060708";
    public static final String serverKey = "GPGCHfXVHERsHyCwXhGcGMRjXgCtXKXJ";
    public static final String deskey = "abcdefg.01234";
    public static final String defaultKey = "ztbs.366";

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key 加密密码
     * @param md5Key 是否对key进行md5加密
     * @param iv 加密向量
     * @return 加密后的字节数据
     */
    public static byte[] encrypt(byte[] content, byte[] key, boolean md5Key, byte[] iv) {
        try {
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); // "算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivps);
            return cipher.doFinal(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static byte[] decrypt(byte[] content, byte[] key, boolean md5Key, byte[] iv) {
        try {
            if (md5Key) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                key = md.digest(key);
            }
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding"); // "算法/模式/补码方式"
            IvParameterSpec ivps = new IvParameterSpec(iv);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivps);
            return cipher.doFinal(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        String s = "yxmzjztbs.366";
        byte[] b2 = Base64.encodeBase64(s.getBytes("utf-8"));
        byte[] en = AesUtil.encrypt(b2, "MDEwMjAzMDQwNTA2".getBytes(default_charset), false,
                "DzwjTw2ADMNEQMMA".getBytes(default_charset));
        byte[] en1 = Base64.encodeBase64(en);
        // System.out.println(en1);
        System.out.println(new String(en1, default_charset));
        //
        byte[] de1 = Base64.decodeBase64(en1);
        //
        s = new String(en1, default_charset);
        de1 = Base64.decodeBase64(s.getBytes(default_charset));

        en = AesUtil.decrypt(de1, "MDEwMjAzMDQwNTA2".getBytes(default_charset), false,
                "DzwjTw2ADMNEQMMA".getBytes(default_charset));
        System.out.println(new String(en, default_charset));
    }
}
