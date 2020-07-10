package com.kitxiao.boot.jd_gpuprice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @类名称：StringHelper
 * @类描述：对字符串的一些处理
 * @创建人：白轲祥
 */
public class StringHelper {

	public static String getResultByReg(String content, String reg) {
		List<String> list = new ArrayList<String>();
		Pattern pa = Pattern.compile(reg, Pattern.DOTALL);
		Matcher ma1 = pa.matcher(content);
		if (ma1.find()) {
			list.add(ma1.group(1));
			return list.get(0);
		} else {
			return null;
		}
	}


	public static String convertNumToString(String s) {
		String result = "";
		String[] aa = s.replace("&nbsp;", "").split(";");
		for (int i = 0; i < aa.length; i++) {
			if (aa[i].indexOf("&#") >= 0) {
				String[] bb = aa[i].split("&#");
				for (int j = 0; j < bb.length; j++) {
					if (bb[j].matches("[0-9]+") && aa[i].indexOf("&#" + bb[j]) >= 0
							&& aa[i].indexOf(bb[j] + "&#" + bb[j]) < 0) {
						result = result + ((char) Integer.valueOf(bb[j].replace(";", "")).intValue());
					} else {
						result = result + bb[j];
					}
				}
			} else {
				result = result + aa[i];
			}
		}
		return result;
	}
	

	

	public static String[][] convertResult(ResultSet res) {
		String[][] result = null;
		int column = 0;
		Vector<String[]> vector = new Vector<String[]>();
		try {
			while (res.next()) {
				column = res.getMetaData().getColumnCount();
				String[] str = new String[column];
				for (int i = 1; i < column + 1; i++) {
					Object ob = res.getObject(i);
					if (ob == null)
						str[i - 1] = "";
					else
						str[i - 1] = ob.toString();
				}
				vector.addElement(str.clone());
			}
			result = new String[vector.size()][column];
			vector.copyInto(result);
			vector.clear();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getLastResultByReg(String content, String reg) {
		Pattern p = Pattern.compile(reg, Pattern.DOTALL);
		Matcher m = p.matcher(content);
		Stack<String> s = new Stack<String>();
		while (m.find()) {
			s.push(m.group(1));
		}
		return s.isEmpty() ? null : (String) s.pop();
	}

	/** 去除字符串前后的全角空格 并替换掉字符串中的全角空格 */
	public static String trimCHN(String para) {
		while (para.startsWith(" ")) {
			para = para.substring(1, para.length()).trim();
		}
		while (para.endsWith(" ")) {
			para = para.substring(0, para.length() - 1).trim();
		}
		para = para.replaceAll(" ", " ");
		return para;
	}

	/** 将html语言中的unicode转换成中文 */
	public static String escapeHtml(String unicodeStr) {
		if (unicodeStr == null) {
			return null;
		}
		StringBuffer retBuf = new StringBuffer();
		int maxLoop = unicodeStr.length();
		for (int i = 0; i < maxLoop; i++) {
			if (unicodeStr.charAt(i) == '\\') {
				if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
					try {
						retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
						i += 5;
					} catch (NumberFormatException localNumberFormatException) {
						retBuf.append(unicodeStr.charAt(i));
					}
				else
					retBuf.append(unicodeStr.charAt(i));
			} else {
				retBuf.append(unicodeStr.charAt(i));
			}
		}
		return retBuf.toString();
	}

	/** list转换成字符串 */
	public static String castListToString(List<String> list) {
		String resString = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				resString = list.get(i) + "&" + resString;
			}
			resString = resString.substring(0, resString.length() - 1);
		}
		return resString;
	}

	/** 字符串去空格换行 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	/** 特殊字符转换 */
	public static String fileEncode(String str) {
		if (str != null) {
			// 这里是专为文件写的转义方法，涉及文件操作
			return str.replaceAll("\\\\", "＼").replaceAll("/", "／").replaceAll(":", "：").replaceAll("[*]", "＊")
					.replaceAll("[?]", "").replaceAll("\"", "”").replaceAll(":", "：").replaceAll("<", "")
					.replaceAll(">", "").replaceAll("[|]", "｜").replaceAll("[(]", "").replaceAll("[)]", "")
					.replaceAll(" ", "").replaceAll("”", "").replaceAll("'", "''");
		} else {
			// 防止空，搞成空格
			return " ";
		}
	}
	
	/** 金额转换 */
	public static String moneyEncode(String str) {
		if (str != null) {
			str = str.replace("￥", "").replace("元", "").replace("¥", "");
			if(str.contains("万")){
				str = str.replace("万", "");
				str = String.valueOf(Double.valueOf(str) * 10000);
			}
			return str;
		} else {
			// 防止空，搞成空格
			return " ";
		}
	}
	
	/** 字符串转时间 */
	public static Date StrToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
