package utils;

import android.text.TextUtils;

public class StringUtil
{
	private static final String HexChars = "1234567890abcdefABCDEF";
	public static final int LCD_WIDTH = 16;

	/** A table of hex digits */
	public static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	* Convert a nibble to a hex character
	* 
	* @param nibble
	*          the nibble to convert.
	*/
	public static char toHexChar(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}



	public static String returnString(String str) {
		return (null == str) ? "" : str;
	}

	public static String returnString(int intValue) {
		return intValue < 0 ? "" : "" + intValue;
	}

	public static String returnString(short shortValue) {
		return returnString((int)shortValue);
	}

	public static String returnString(byte byteValue) {
		return returnString((int)byteValue);
	}

	/**
	* Method trim space
	*
	* @param The string to be format.
	*
	*/
	public static String trimSpace(String oldString) {
		if (null == oldString)
			return null;
		if (0 == oldString.length())
			return "";
	
		StringBuffer sbuf = new StringBuffer();
		int oldLen = oldString.length();
		for (int i = 0; i < oldLen; i++) {
			if (' ' != oldString.charAt(i))
				sbuf.append(oldString.charAt(i));
		}
		String returnString = sbuf.toString();
		sbuf = null;
		return returnString;
	}
	/**
	* Method trim space
	*
	* @param oldString The string to be format.
	* @param trimFlag The trim flag, 
	*         =0:trim both sides,
	*         >0:trim right sides,
	*         <0:trim left sides,
	*
	*/
	public static String trimSpace(String oldString, int trimFlag) 
	{
		if (null == oldString)
			return null;
		if (0 == oldString.length())
			return "";
			
		int length = oldString.length();
		int j = 0;
		for(j = 0; j < length && (oldString.charAt(j) == ' ' || oldString.charAt(j) == '\0'); j++);
		if (trimFlag < 0) // trim left sides
			return (j <= 0 ? oldString : oldString.substring(j));
		for(; j < length && (oldString.charAt(length - 1) == ' ' || oldString.charAt(length - 1) == '\0'); length--);
		if (trimFlag > 0) //trim right sides
			return (length >= oldString.length() ? oldString:oldString.substring(0, length));
			
		return (j <= 0 && length >= oldString.length() ? oldString : oldString.substring(j, length));
	}
	/**
	* Method convert byte[] to String
	*
	* @param The string to be format.
	*
	*/
	public static String toString(byte abyte0[]) {
		return (null == abyte0) ? null : new String(abyte0);
	}

	public static String[] buffer2Message(String bufferString, int width, int height) 
	{
		int buffLen;
		int i = 0;
		int h, w;
		if (null == bufferString)
			buffLen = 0;
		else
			buffLen = bufferString.length();
	
		// 求 w/h 的逻辑太复杂，可能有BUG. DuanCS@[20140623]
		if (height < 1 && width > 0) {
			if (0 == (buffLen % width))
				h = buffLen / width;
			else
				h = (buffLen / width) + 1;
			w = width;
		} else {
			if (height > 0 && width < 1) {
				if (0 == (buffLen % height))
					w = buffLen / height;
				else
					w = (buffLen / height) + 1;
				h = height;
			} else {
				if (height > 0 && width > 0) {
					h = height;
					w = width;
				} else {
					return null;
				}
			}
		}
			
		String[] buff = new String[h];
		// 这段也低效... DuanCS@[20140623]
		for (i = 0; i < h; i++) {
			if ((w * (i + 1)) < buffLen)
				buff[i] = bufferString.substring(w * i, w * (i+1));
			else if ((w * (i + 1)) >= buffLen && (w * i) < buffLen)
				buff[i] = bufferString.substring(w * i, buffLen);
			else
				buff[i] = "";
		}

		return buff;
	}

	/**
	* Method Format string
	*
	* @param The string to be format.
	*
	*/
	public static String[] buffer2Message(String bufferString) {
		return buffer2Message(bufferString, LCD_WIDTH, 3);
	}

	/**
	* Method fill string
	*
	* @param The string to be format.
	*
	*/
	public static String fillString(String formatString, int length, char fillChar, boolean leftFillFlag) {
		if (null == formatString) {
			formatString = "";
		}
		int strLen = formatString.length();
		if (strLen >= length) {
			if (leftFillFlag)  // left fill 
				return formatString.substring(strLen - length, strLen);
			else
				return formatString.substring(0, length);
		}

		StringBuffer sbuf = new StringBuffer();
		int fillLen = length - formatString.length();
		for (int i = 0; i < fillLen; i++) { 
			sbuf.append(fillChar);
		}
		
		if (leftFillFlag) { // left fill 
			sbuf.append(formatString);
		} else {
			sbuf.insert(0, formatString);
		}
		String returnString = sbuf.toString();
		sbuf = null;
		return returnString;
	}

	/**
	* Method fill string
	*
	* @param The string to be format.
	*
	*/
	public static String fillSpace(String formatString, int length) {
		return fillString(formatString, length, ' ', false);
	}

	/**
	* Method Format string
	*
	* @param The string to be format.
	*
	*/
	public static String formatLine(String formatString, boolean leftFillFlag) {
		return fillString(formatString, LCD_WIDTH, ' ', leftFillFlag);
	}


	private static final char[] space8 = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
	/**
	* Method fill space , converted String lenth to LCD_WIDTH
	*
	* @param The string to be format.
	*
	*/
	public static String fillShowSpace(String formatString) {
		if(null == formatString)
			return "";

		if (formatString.length() <= LCD_WIDTH) {
			int len = 8 - (formatString.length() / 2);
			StringBuffer sbuf = new StringBuffer();
			sbuf.append(space8, 0, len);
			sbuf.append(formatString);
			sbuf.append(space8, 0, len);
			sbuf.setLength(LCD_WIDTH);
			
			String returnString = sbuf.toString();
			sbuf = null;
			return returnString;
		}

		return formatString.substring(0, LCD_WIDTH);
	}

	// 简化对fillZero(Integer.toString(...), int length)的调用. DuanCS@[20150903]
	public static String fillZero(int value, int length) {
		return fillZero(String.valueOf(value), length);
	}

	public static String fillZero(long value, int length) {
		return fillZero(String.valueOf(value), length);
	}

	/**
	* Method Format string
	*
	* @param The string to be format.
	*
	*/
	public static String fillZero(String formatString, int length) {
		return fillString(formatString, length, '0', true);
	}

	/**
	* @param s source string (with Hex representation)
	* @return byte array
	*/
	public static byte[] hexString2bytes (String s)  {
		if (null == s)
			return null;

		s = trimSpace(s);
		if (!isHexChar(s, false))
			return null;

		return hex2byte (s, 0, s.length() >> 1);
	}

	/**
	* @param   s       source string
	* @param   offset  starting offset
	* @param   len     number of bytes in destination (processes len*2)
	* @return  byte[len]
	*/
	public static byte[] hex2byte (String s, int offset, int len) {
		byte[] d = new byte[len];
		int byteLen = len * 2;
		for (int i = 0; i < byteLen; i++) {
			int shift = (i%2 == 1) ? 0 : 4;
			d[i>>1] |= Character.digit(s.charAt(offset+i), 16) << shift;
		}
		return d;
	}
	
	private static void appendHex(StringBuffer stringbuffer, byte byte0) {
		stringbuffer.append(toHexChar(byte0 >> 4));
		stringbuffer.append(toHexChar(byte0));
	}

	public static String toHexString(byte abyte0[], int beginIndex, int endIndex, boolean spaceFlag) {
		if (null == abyte0)
			return null;
		if (0 == abyte0.length)
			return "";

		StringBuffer sbuf = new StringBuffer();
		appendHex(sbuf, abyte0[beginIndex]);
		for(int i = (beginIndex + 1); i < endIndex; i++) {
			if (spaceFlag)
				sbuf.append(" ");
			appendHex(sbuf, abyte0[i]);
		}
		String returnString = sbuf.toString();
		sbuf = null;
		return returnString;
	}

	public static String toHexString(byte abyte0[], int beginIndex, int endIndex) {
		if (null == abyte0)
			return null;
		return toHexString(abyte0, beginIndex, endIndex, true);
	}

	public static String toHexString(byte abyte0[], boolean spaceFlag) {
		return null == abyte0 ? null
			: spaceFlag ? toHexString(abyte0, 0, abyte0.length, spaceFlag)
			: ByteUtil.arrayToHexStr(null, abyte0, 0, abyte0.length); 
	}

	/**
	* Method convert byte[] to HexString
	*
	* @param The string to be format.
	*
	*/
	
	public static String toHexString(byte abyte0[]) {
		if (null == abyte0)
			return null;
		return toHexString(abyte0, 0, abyte0.length, false);
	}

	public static String toHexString(byte abyte0) {
		StringBuffer sbuf = new StringBuffer();
		appendHex(sbuf, abyte0);
		return sbuf.toString();
	}

	/**
	* Method Method convert byte[] to HexString and String
	*
	* @param The string to be format.
	*
	*/		
	public static String toFullString(byte abyte0[]) {
		return "(" + toHexString(abyte0) + ") " + toString(abyte0);
	}

	/**
	* Method Format string
	*
	* @param The string to be format.
	*
	*/
	public static String toBestString(byte abyte0[]) {
		boolean flag = false;
		if (abyte0 != null) {
			int byteLen = abyte0.length;	// "!flag"低效,宜改用"break;". DuanCS@[20140623]
			for(int i = 0; !flag && i < byteLen; i++)
				if(abyte0[i] != 32 && (abyte0[i] != 33) & (abyte0[i] != 63))
					flag = abyte0[i] < 48;
		}
		return flag ? toHexString(abyte0) : toString(abyte0);
	}

	/**
	* Method Check String 
	*
	* @param The string to be format.
	*
	*/  
	public static boolean isHexChar(String hexString, boolean trimSpaceFlag) {
		if (null == hexString || 0 == hexString.length())
			return false;

	// 为了判断而建新串? 低效. DuanCS@[20140623]
//    if (trimSpaceFlag)
//        hexString = trimSpace(hexString);
//        
//      if (hexString.length() % 2 != 0)
//        return false;
//      int hexLen = hexString.length();
//      for(int i = 0; i < hexLen; i++)
//      {
//        if (HexChars.indexOf(hexString.charAt(i)) < 0)
//          return false;
//      }
//
//      return true;

		// 无需创建对象, 且只遍历一次
		int hexChars = 0;
		for(int i = hexString.length(); -i >= 0; ) {
			char chr = hexString.charAt(i);
			if (chr == ' ') {
				if (!trimSpaceFlag) {
					return false;
				}
			} else if (HexChars.indexOf(chr) < 0) {
				return false;
			} else {
				++hexChars;
			}
		}
	
		return (hexChars & 0x01) == 0;
	}

	public static boolean isHexChar(String hexString) {
		return isHexChar(hexString, true);
	}
	
	/**
	* Return true if the string is alphanum.
	* <code>{letter digit }</code>
	* 
	**/
	public static boolean isLetterNumeric ( String s ) {
		int i = 0, len = s.length();
		while ( i < len
			&&	(	Character.isLowerCase(s.charAt(i))
				||	Character.isUpperCase(s.charAt(i))
				||	Character.isDigit(s.charAt(i))
				)
		) {
			i++;
		}
		return ( i >= len );
	}
	
	/**
	* 获取字符串的长度，如果有中文，则每个中文字符计为2位
	* 
	* @param value
	*            指定的字符串
	* @return 字符串的长度
	*/
	// 此法性能严重低下 -- 重复 "建 新串、调用matches"!!!	DuanCS@[20140623]
	//	完全可以只用一个正则搞定...
	public static int length(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}
	
	/**
	 *  几个轻量级的方法 --- 常规的  stringObject.trim().length() 可能会创建新对象!<br> 
	 * 	而 TextUtils.getTrimmedLength 不会. DuanCS@[20140703]<br>
	 *  非空白文本的长度<br>
	 */

	/**
	 * 去除两端Space后的长度
	 */
	public static int getTrimmedLength(CharSequence s) {
		return TextUtils.isEmpty(s) ? 0 : TextUtils.getTrimmedLength(s);
	}
	/**
	 *  去除两端Space后是空文本?
	 */
	public static boolean isTrimmedEmpty(CharSequence s) {
		return getTrimmedLength(s) == 0;
	}
	/**
	 *  参数列表中去除两端Space后至少有一个 为空文本?
	 */
	public static boolean hasTrimmedEmpty(CharSequence... ss) {
		for (CharSequence s : ss) {
			if (getTrimmedLength(s) == 0) {
				return true;
			}
		}
		return false;
	}
	public static boolean hasEmpty(CharSequence... ss) {
		for (CharSequence s : ss) {
			if (TextUtils.isEmpty(s)) {
				return true;
			}
		}
		return false;
	}
	/**
	 *  参数列表去除两端Space后全为空文本?
	 */
	public static boolean allTrimmedEmpty(CharSequence... ss) {
		for (CharSequence s : ss) {
			if (getTrimmedLength(s) > 0) {
				return false;
			}
		}
		return true;
	}
	public static boolean allEmpty(CharSequence... ss) {
		for (CharSequence s : ss) {
			if (!TextUtils.isEmpty(s)) {
				return false;
			}
		}
		return true;
	}
}
