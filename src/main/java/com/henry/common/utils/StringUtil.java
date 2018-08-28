package com.henry.common.utils;


import com.henry.common.utils.encrypt.AesUtil;
import com.henry.common.utils.encrypt.EncrypUtil;
import org.apache.commons.codec.binary.Base64;


import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
   /**
   *  去掉指定字符串的开头和结尾的指定字符

   * @param stream 要处理的字符串
   * @param trimstr 要去掉的字符串
   * @return 处理后的字符串
   */
  public static String sideTrim(String stream, String trimstr) {
    // null或者空字符串的时候不处理
    if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
      return stream;
    }
 
    // 结束位置
    int epos = 0;
 
    // 正规表达式
    String regpattern = "[" + trimstr + "]*+";
    Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
 
    // 去掉结尾的指定字符 
    StringBuffer buffer = new StringBuffer(stream).reverse();
    Matcher matcher = pattern.matcher(buffer);
    if (matcher.lookingAt()) {
      epos = matcher.end();
      stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
    }
 
    // 去掉开头的指定字符 
    matcher = pattern.matcher(stream);
    if (matcher.lookingAt()) {
      epos = matcher.end();
      stream = stream.substring(epos);
    }
 
    // 返回处理后的字符串
    return stream;
  }
  
  /**
    *检测字符串为null或空
   */
  public static boolean isEmptyOrNull(String str) {
	  return str == null || "".equals(str);
  }
  
  public static String[] StringtoStrings(String input) {
		String[] result = new String[input.length()];
		for (int i = 0; i < input.length(); i++) {
			result[i] = input.substring(i, i + 1);
		}
		return result;
	}

	public static String Int2KeyChar(Integer aNum, String SCarry, int ICarry,
			Integer AFixLen) {
		String result = "";
		int a1 = aNum;
		int a2;
		String[] scarry = StringtoStrings(SCarry);
		if (a1 == 0) {
			a2 = a1 % ICarry;
			result = scarry[a2] + result;
			a1 = a1 / ICarry;
		}
		while (Math.abs(a1) != 0) {
			a2 = a1 % ICarry;
			result = scarry[a2] + result;
			a1 = a1 / ICarry;
		}
		while (result.length() < AFixLen) {
			result = scarry[0] + result;
		}
		return result;
	}

	public static long KeyChar2Int(String aStr, String SCarry, int ICarry) {
		long result = 0;
		String[] astr = StringtoStrings(aStr);
		for (int i = 0; i < astr.length; i++) {
			long inum = SCarry.indexOf(astr[i]);
			result = result * ICarry + inum;
		}
		return result;
	}

	public static String AdjustString(String AStr, boolean ARevert) {
		int iStart, iEnd, iLen;
		String c;
		String[] result = new String[AStr.length() + 1];
		for (int i = 1; i <= AStr.length(); i++) {

			result[i] = AStr.substring(i - 1, i);
		}

		iLen = AStr.length();
		iStart = 1;
		iEnd = 4;
		if (ARevert) {
			iStart = -4;
			iEnd = -1;
		}
		while (iStart <= iEnd) {
			switch (Math.abs(iStart)) {
			case 1: {
				for (int i = 1; i <= iLen - 1; i++) {

					if (1 == (i % 2)) {
						c = result[i];
						result[i] = result[i + 1];
						result[i + 1] = c;
					}
				}
				break;
			}
			case 2: { // 前半段与后半段交换 ABCDEF <-> DEFABC
				for (int i = 1; i <= iLen / 2; i++) {
					c = result[i];
					result[i] = result[iLen / 2 + i];
					result[iLen / 2 + i] = c;
				}
				break;
			}
			case 3: { // 前半段与后半段交换 ABCDEF <-> FBDCEA
				for (int i = 1; i <= iLen / 2; i++) {
					if (1 == (i % 2)) {
						c = result[i];
						result[i] = result[iLen - i + 1];
						result[iLen - i + 1] = c;
					}
				}
				break;
			}
			case 4: { // 以中间为界互换 ABCDEF <-> FEDCBA
				for (int i = 1; i <= iLen / 2; i++) {
					c = result[i];
					result[i] = result[iLen - i + 1];
					result[iLen - i + 1] = c;
				}
				break;
			}
			}
			iStart = iStart + 1;
		}
		;
		String str1 = "";
		for (int i = 1; i <= result.length - 1; i++) {
			str1 = str1 + result[i];
		}
		return str1;
	}

	public static String StrToKeyChar(String AIn, String SCarry, int ICarry) {
		Integer[] a = new Integer[AIn.length()];
		char tempchar;
		for (int i = 0; i < AIn.length(); i++) {
			tempchar = AIn.charAt(i);
			a[i] = tempchar + 0;
		}
		String temp = ByteToKeyChar(a, SCarry, ICarry, AIn.length());
		return temp;

	}

	public static String ByteToKeyChar(Integer[] ABuf, String SCarry,
			int ICarry, Integer ALen) {
		String result = "";
		for (int i = 0; i < ALen - 1; i++) {
			// Int2KeyChar()
			result = result + Int2KeyChar(ABuf[i], SCarry, ICarry, 2);

		}
		return result;

	}

	public static String TurnString(String AIn, Integer AStep, Integer AWay) {
		// 循环旋转字符串,AStep旋转距离
		// AWay旋转方向
		// 0-正向
		// 1-反向
		String result = AIn;
		Integer iLen = AIn.length();
		if ((iLen == 0) || (AStep % iLen == 0)) {
			return result;
		}
		Integer iStep = AStep % iLen;
		if (AWay == 0) {
			String a1 = AIn.substring(iLen - iStep , iLen - iStep  + iStep);
			String a2 = AIn.substring(0, iLen - iStep);
			result = a1 + a2;
			char[] tempchar = result.toCharArray();
			for (int i = 0; i < iLen / 2; i++) {
				char cSwap = tempchar[i];
				tempchar[i] = tempchar[iLen - 1 - i - 1];
				tempchar[iLen - 1 - i - 1] = cSwap;
			}
			result = String.valueOf(tempchar);
		} else {
			result = AIn;
			char[] tempchar = result.toCharArray();
			for (int i = 0; i < iLen / 2; i++) {
				char cSwap = tempchar[i];
				tempchar[i] = tempchar[iLen - 1 - i - 1];
				tempchar[iLen - 1 - i - 1] = cSwap;
			}
			result = String.valueOf(tempchar);
			// Copy(Result, iStep + 1, iLen) + Copy(Result, 1, iStep);
			String a1 = result.substring(iStep, iLen + iStep - iStep);
			String a2 = result.substring(0, iStep);
			result = a1 + a2;
		}
		return result;
	}

	public static String DeCodeDateStr(String date, String sCarry, int iCarry) {
		return String.valueOf(KeyChar2Int(date, sCarry, iCarry) + 10000000);
	}
	
	/**
	 * 用指定的密码加密指定的字符串
	 * 
	 * @param plainText
	 * @param password
	 * @return
	 */
	public final static String encryptToHexStr(String plainText, String password) {
		try {
			return EncrypUtil.ByteToHexStr(EncrypUtil.doEncrypt(plainText.getBytes("UTF-8"),
					password.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 用指定的密码解密指定的字符串
	 * 
	 * @param encryptText
	 * @param password
	 * @return
	 */
	public final static String decryptFromHexStr(String encryptText, String password) {
		try {
			return new String(EncrypUtil.doDecrypt(EncrypUtil.HexStrToByte(encryptText),
					password.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * 生成GUID,不带破析号
	 */
	public final static String createGuidStr() {
	    String guidStr = UUID.randomUUID().toString();
	    return guidStr.replaceAll("-", "");
	}
	
	public static void main(String[] a) throws Exception {
		String s = "2868d4212f3e17e8fd8757d2fe18bf20297ceee9fbc6361b3c53d4022b0c2ed4";
		System.out.println(decryptFromHexStr(s,"abcdefg.01234"));
		String tmp = "yxmzjztbs.366";
		
		System.out.println(new String(Base64.encodeBase64(tmp.getBytes(AesUtil.default_charset))));
		String _pwd = StringUtil.encryptToHexStr(new String(Base64.encodeBase64(tmp.getBytes(AesUtil.default_charset)), AesUtil.default_charset), AesUtil.deskey);
		System.out.println(_pwd);//4d72fff46097dce8a3a90b649839bccf09885c8b94da16ae109ff830e68cbcec
	}
}
