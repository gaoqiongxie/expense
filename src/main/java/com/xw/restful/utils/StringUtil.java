package com.xw.restful.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串工具类
 * 
 * @author sjj, </br> sjj@gaoxinyun.com
 * @see com.gaoxin.ocean.utils.StringUtils
 * @version V0.0.1-SNAPSHOT, 2014-12-29 上午11:09:30
 * @description
 * 
 */
public class StringUtil {
    private final static Logger LOG = LoggerFactory.getLogger(StringUtil.class);
	/**
	 * 默认的空值
	 */
	public static final String EMPTY = "";

	/**
	 * Example: subString("12345","1","4")=23
	 * 
	 * @param src
	 * @param start
	 * @param to
	 * @return
	 */
	public static Integer subStringToInteger(String src, String start, String to) {
		return stringToInteger(subString(src, start, to));
	}

	/**
	 * Example: subString("abcd","a","c")="b"
	 * 
	 * @param src
	 * @param start
	 *            null while start from index=0
	 * @param to
	 *            null while to index=src.length
	 * @return
	 */
	public static String subString(String src, String start, String to) {
		int indexFrom = start == null ? 0 : src.indexOf(start);
		int indexTo = to == null ? src.length() : src.indexOf(to);
		if (indexFrom < 0 || indexTo < 0 || indexFrom > indexTo) {
			return null;
		}
		if (null != start) {
			indexFrom += start.length();
		}
		return src.substring(indexFrom, indexTo);
	}
	
	/**
	 * Example: subString("abcd",1)="1"
	 * @return
	 */
	public static String subString(String src, int leg) {

		return src.substring(0, leg);
	}

	/**
	 * @param in
	 * @return
	 */
	public static Integer stringToInteger(String in) {
		if (in == null) {
			return null;
		}
		in = in.trim();
		if (in.length() == 0) {
			return null;
		}
		try {
			return Integer.parseInt(in);
		} catch (NumberFormatException e) {
			LOG.warn("stringToInteger fail,string=" + in, e);
			return null;
		}
	}

	/**
	 * @param in
	 * @return
	 */
	public static Float stringToFloat(String in) {
		if (in == null) {
			return null;
		}
		in = in.trim();
		if (in.length() == 0) {
			return null;
		}
		try {
			return Float.parseFloat(in);
		} catch (NumberFormatException e) {
			LOG.warn("stringToFloat fail,string=" + in, e);
			return null;
		}
	}
	/**
	 * 字符串转double
	 * @param in
	 * @return
	 */
	public static Double stringToDouble(String in) {
		if (in == null) {
			return null;
		}
		in = in.trim();
		if (in.length() == 0) {
			return null;
		}
		try {
			return Double.parseDouble(in);
		} catch (NumberFormatException e) {
			LOG.warn("stringToDouble fail,string=" + in, e);
			return null;
		}
	}
	/**
	 * @param in
	 * @return
	 */
	public static Long stringToLong(String in) {
		if (in == null) {
			return null;
		}
		in = in.trim();
		if (in.length() == 0) {
			return null;
		}
		try {
			return Long.parseLong(in);
		} catch (NumberFormatException e) {
			LOG.warn("stringToLong fail,string=" + in, e);
			return null;
		}
	}

	/**
	 * 判断两个字符串相等
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(String a, String b) {
		if (a == null) {
			return b == null;
		}
		return a.equals(b);
	}

	/**
	 * 判断两个字符串相等忽略大小写
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equalsIgnoreCase(String a, String b) {
		if (a == null) {
			return b == null;
		}
		return a.equalsIgnoreCase(b);
	}

	/**
	 * 检查字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		} else if (str.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 截取并保留标志位之前的字符串
	 * 
	 * @param str
	 *            字符串
	 * @param expr
	 *            分隔符
	 * @return
	 */
	public static String substringBefore(String str, String expr) {
		if (isEmpty(str) || expr == null) {
			return str;
		}
		if (expr.length() == 0) {
			return EMPTY;
		}
		int pos = str.indexOf(expr);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取并保留标志位之后的字符串
	 * 
	 * @param str
	 *            字符串
	 * @param expr
	 *            分隔符
	 * @return
	 */
	public static String substringAfter(String str, String expr) {
		if (isEmpty(str)) {
			return str;
		}
		if (expr == null) {
			return EMPTY;
		}
		int pos = str.indexOf(expr);
		if (pos == -1) {
			return EMPTY;
		}
		return str.substring(pos + expr.length());
	}

	public static int lowerHashCode(String text) {
		if (text == null) {
			return 0;
		}
		// return text.toLowerCase().hashCode();
		int h = 0;
		for (int i = 0; i < text.length(); ++i) {
			char ch = text.charAt(i);
			if (ch >= 'A' && ch <= 'Z') {
				ch = (char) (ch + 32);
			}

			h = 31 * h + ch;
		}
		return h;
	}

	/**
	 * 字符串去除头尾空格
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		return str == null ? "" : str.trim();
	}

	/**
	 * 空值转换成指定字符串 默认为空串
	 * 
	 * @param str
	 * @return
	 */
	public static String nvl(String str, String destStr) {
		String retStr = "";
		if (str != null && isNotEmpty(str)) {
			retStr = String.valueOf(str);
		} else {
			if (isNotEmpty(destStr)) {
				retStr = destStr;
			}
		}
		return retStr;
	}

	/**
	 * 将空值转换成空串
	 * 
	 * @param str
	 * @return
	 */
	public static String nvl(String str) {
		String retStr = "";
		if (str != null && isNotEmpty(str)) {
			retStr = str;
		}
		return retStr;
	}

	/**
	 * 把字符串按分隔符转换为数组
	 * 
	 * @param string
	 *            字符串
	 * @param expr
	 *            分隔符
	 * @return
	 */
	public static String[] split(String string, String expr) {
		return string.split(expr);
	}

	/**
	 * 删除字符串中html
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr) {
		if(htmlStr==null ){
			return null;
		}
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		String regEx_html_Code = "&nbsp;";// 去掉html标签中的code，如&nbsp;等
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签
		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_html_code = Pattern.compile(regEx_html_Code,
				Pattern.CASE_INSENSITIVE);
		Matcher m_html_code = p_html_code.matcher(htmlStr);
		htmlStr = m_html_code.replaceAll(""); // 过滤html字符

		return htmlStr.trim(); // 返回文本字符串
	}
	/**
	 * 将连续的多个回车|换行|制表位|空格处理成一个
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String muiltBlankDual(String origStr) {
		String regEx_r = "\r+"; //回车
		String regEx_n = "\n+"; //换行
		String regEx_t = "\t+"; //制表位
		String regEx_b = " +"; //空格
		if(origStr != null){
		 return origStr.replaceAll(regEx_b, " ")
							  .replaceAll(regEx_n, "\n")
							  .replaceAll(regEx_t, "\t")
							  .replaceAll(regEx_r, "\r");
		}
		return null; 
	}
	/**
	 * 删掉所有空白,包括空格、回车、换行、制表位
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String delBlank(String origStr) {
		if(origStr!=null){
			return origStr.replaceAll("\\s*", "");
		}
		return null; 
	}
	
	/**
	 * 删掉所有空格
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String delSpace(String origStr) {
		if(origStr!=null){
			return origStr.replaceAll(" +", "");
		}
		return null; 
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);
		return returnString;
	}
	
	/**
	 * 删除html注释
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String delHtmlComment(String input) {
		String regEx_o = "<\\!--.*-->";//html注释
		Pattern  p_o = Pattern.compile(regEx_o, Pattern.CASE_INSENSITIVE);
       Matcher  m_o = p_o.matcher(input);
       input = m_o.replaceAll("");
		return input;
	}
	/**
	 * 删除html中的nbsp;
	 * 
	 * @param input
	 *            String.
	 * @return 半角字符串
	 */
	public static String delHtmlNBSP(String input) {
		if(input!=null){
			input = input.replaceAll("(&nbsp;)+", "");
		}
		return input;
	}
	
	
	/**
     * 首字母转大写
     * @param text
     * @return 
     */
    public static String toFirstUpperCase(String text){
        if(text==null||text.length()==0)return text;
        char c=text.charAt(0);
        if(c<'a'&&c>'z')return text;//首字母不是小写,返回原字符串
        c=(char)(c&0xdf);
        char[] chr=text.toCharArray();
        chr[0]=c;
        return new String(chr);
    }
    
    /**
     * 首字母转小写
     * @param text
     * @return
     */
    public static String toFirstLowerCase(String text) {
        if(text==null||text.length()==0)return text;
        char c=text.charAt(0);
        if(c<'A'&&c>'Z')return text;//首字母不是大写,返回原字符串
        c=(char)(c|0x20);
        char[] chr=text.toCharArray();
        chr[0]=c;
        return new String(chr);
    }
}
