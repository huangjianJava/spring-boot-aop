package com.henry.common.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class EncrypUtil {
    public EncrypUtil() {
        // TODO Auto-generated constructor stub
    }


    public final static String AL = "DES";
    // public final static String PASSWORD = "biwei123";

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // String str = "ThisThis123456789一个测试机密-=23km一个好得很";
        //
        //
        //
        // //str=Base64.encode(str.getBytes());
        // String pwd = "biwei123";
        //
        // String mstr=BaseUtil.encryptToHexStr(str,pwd);
        // System.out.println("base jiam:"+mstr);
        // System.out.println("base jiem:"+BaseUtil.decryptFromHexStr(mstr,pwd));


        // byte[] estr = JmUtil.doEncrypt(str.getBytes(),pwd.getBytes());//.doEncode(str, pwd);
        // String hexStr=JmUtil.ByteToHexStr(estr);
        // System.out.println(hexStr);
        // System.out.println(new String(estr));

        // byte[] pstr = JmUtil.doDecrypt(estr,pwd.getBytes());//.doDecode(estr, pwd);
        // System.out.println(new String(pstr));
        // String
        // hexstr2="67539EF6FF98FADC6BEAFB69D6A563B9A4023801E189A5F91B4806682166FDE6F1C5D0D1B860686B";
        // byte[] estr2=JmUtil.HexStrToByte(hexstr2);
        // pstr = JmUtil.doDecrypt(estr2,pwd.getBytes());//.doDecode(estr, pwd);
        // System.out.println(new String(pstr));
    }

    public static byte[] doEncrypt(byte[] plainText, byte[] desKey) throws Exception {
        // 为了和Delphi中的处理一致，预处理数据
        // 如果密码不及8位填满8位
        if (desKey.length < 8) {
            byte[] keynew = new byte[8];
            for (int i = 0; i < desKey.length; i++) {
                keynew[i] = desKey[i];
            }
            for (int i = desKey.length; i < keynew.length; i++) {
                keynew[i] = (byte) ((char) 0);
            }
            desKey = keynew;
        }
        // 如果不是8的倍数，特书处理，Delphi中的加密后面补0
        // 最后在+8个字节，最后一个字节存放填充了多少个补充字节
        int length = plainText.length;
        int cl = length % 8;
        if (cl != 0) {
            byte[] clbytes = new byte[length + (8 - cl) + 8];
            for (int i = 0; i < length; i++) {
                clbytes[i] = plainText[i];
            }
            for (int i = length; i < clbytes.length; i++) {
                clbytes[i] = (byte) ((char) 0);
            }
            clbytes[clbytes.length - 1] = (byte) (8 - cl + 8);
            plainText = clbytes;
        } else {
            byte[] clbytes = new byte[length + 8];
            for (int i = 0; i < length; i++) {
                clbytes[i] = plainText[i];
            }
            for (int i = length; i < clbytes.length; i++) {
                clbytes[i] = (byte) ((char) 0);
            }
            clbytes[clbytes.length - 1] = (byte) (8);
            plainText = clbytes;
        }

        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = desKey; /* 用某种方法获得密匙数据 */
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        if (keyFactory == null) {
            keyFactory = SecretKeyFactory.getInstance("DES");
        }


        SecretKey key = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");// PKCS5Padding");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        // 现在，获取数据并加密
        byte data[] = plainText; /* 用某种方法获取数据 */
        // 正式执行加密操作
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;

    }

    private static SecretKeyFactory keyFactory = null;

    public static byte[] doDecrypt(byte[] encryptText, byte[] desKey) throws Exception {

        // 为了和Delphi中的处理一致，预处理数据
        // 如果密码不及8位填满8位
        if (desKey.length < 8) {
            byte[] keynew = new byte[8];
            for (int i = 0; i < desKey.length; i++) {
                keynew[i] = desKey[i];
            }
            for (int i = desKey.length; i < keynew.length; i++) {
                keynew[i] = (byte) ((char) 0);
            }
            desKey = keynew;
        }
        // 解密时不用预处理密文

        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = desKey; /* 用某种方法获取原始密匙数据 */
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        // SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

        if (keyFactory == null) {
            keyFactory = SecretKeyFactory.getInstance("DES");
        }

        SecretKey key = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");// PKCS5Padding");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        // 现在，获取数据并解密
        byte encryptedData[] = encryptText; /* 获得经过加密的数据 */
        // 正式执行解密操作
        byte decryptedData[] = cipher.doFinal(encryptedData);

        int sublength = (int) decryptedData[decryptedData.length - 1];
        byte[] result = new byte[decryptedData.length - sublength];
        for (int i = 0; i < result.length; i++) {
            result[i] = decryptedData[i];
        }
        return result;

    }
    // ------------------------------------------

    public static byte[] StrToByte(String str)
    // "ABC..." -> [41,42,43,..]
    {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }

        int len = str.length();
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = (byte) str.charAt(i);
        }
        return data;
    }

    // ------------------------------------------

    public static byte[] HexStrToByte(String str)
    // "10213F..." -> [10,21,3F,..]
    {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }

        int len = (str.length() >> 1);
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
        }
        return data;
    }

    // ------------------------------------------

    public static String HexStrToString(String str)
    // "414243..." -> "ABC..."
    {
        if (str == null || str.length() == 0) {
            return new String("");
        }

        int len = (str.length() >> 1);
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            sb.append((char) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16));
        }
        return sb.toString();
    }

    // ------------------------------------------

    private static void appendBytes(StringBuffer stringBuffer, byte int8) {
        stringBuffer.append(Character.forDigit(((int8 >> 4) & 0x0F), 16));
        stringBuffer.append(Character.forDigit((int8 & 0x0F), 16));
    }

    // ------------------------------------------

    public static String ByteToHexStr(byte[] data)
    // [10,21,3F,..] -> "10213F..."
    {
        if (data == null || data.length == 0) {
            return new String("");
        }

        StringBuffer sb = new StringBuffer(2 * data.length);
        for (int i = 0; i < data.length; i++) {
            appendBytes(sb, data[i]);
        }

        return sb.toString();
    }

    // ------------------------------------------

    public static String ByteToDumpStr(byte[] data)
    // [10,21,3F,..] -> "10 21 3F XX XX XX XX XX ..."
    {
        if (data == null || data.length == 0) {
            return new String("");
        }

        StringBuffer sb = new StringBuffer(4 * data.length);
        short kk = 0;
        for (short i = 0; i < data.length; i++) {
            appendBytes(sb, data[i]);
            sb.append(" ");
            kk++;
            if (kk == 24) {
                sb.append("\n");
                kk = 0;
            }
        }
        if (kk != 0) {
            sb.append("\n");
        }
        return sb.toString();
    }

    // ------------------------------------------

    public static String ByteToString(byte[] data)
    // [41,42,43,..] -> "ABC..."
    {
        if (data == null || data.length == 0) {
            return new String("");
        }

        StringBuffer sb = new StringBuffer(data.length);
        for (short i = 0; i < data.length; i++) {
            sb.append((char) data[i]);
        }

        return sb.toString();
    }

    // ------------------------------------------

    public static String removeSpace(String str) {
        if (str == null || str.length() == 0) {
            return new String("");
        }

        int len = str.length();
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            char car = str.charAt(i);
            if (car != (char) 32 && car != '\t' && car != '\n' && car != '\r') {
                sb.append(car);
            }
        }
        return sb.toString();
    }
}
