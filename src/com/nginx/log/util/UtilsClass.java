package com.nginx.log.util;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 工具类
 * 
 * @author liuzj
 * 
 */
public class UtilsClass implements Serializable {

	/**
	 * ip 补零
	 * */
	public static String[] ip_add_zero(String[] ips) {
		try {
			for (int i = 0; i < ips.length; i++) {
				String[] valStrings = ips[i].split("\\.");
				for (int j = 0; j < valStrings.length; j++) {
					switch (valStrings[j].length()) {
					case 1:
						valStrings[j] = "00" + valStrings[j];
						break;
					case 2:
						valStrings[j] = "0" + valStrings[j];
						break;
					}
				}
				ips[i] = combo(valStrings, ".");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ips;
		}

		return ips;
	}

	public static String ip_add_zero(String ip) {
		
		try {
			String[] valStrings = ip.split("\\.");
			for (int j = 0; j < valStrings.length; j++) {
				switch (valStrings[j].length()) {
				case 1:
					valStrings[j] = "00" + valStrings[j];
					break;
				case 2:
					valStrings[j] = "0" + valStrings[j];
					break;
				}
			}
			return combo(valStrings, ".");
		} catch (Exception e) {
			e.printStackTrace();
			return ip;
		}

	}

	/**
	 * ip 排序
	 * */
	public static void bubble_ip_sort(String[] unsorted) {
		for (int i = 0; i < unsorted.length; i++) {
			for (int j = i; j < unsorted.length; j++) {
				if (compartTo(unsorted[i], unsorted[j]) == 1 ? true : false) {
					String temp = unsorted[i];
					unsorted[i] = unsorted[j];
					unsorted[j] = temp;
				}
			}
		}
	}

	private static long[] parseIp(String ip) {
		ip = ip.replace(".", "#");
		long result[] = new long[4];
		String[] ip1 = ip.split("#");
		if (ip != null) {
			result[0] = Long.parseLong(ip1[0]);
			result[1] = Long.parseLong(ip1[1]);
			result[2] = Long.parseLong(ip1[2]);
			result[3] = Long.parseLong(ip1[3]);
		}
		return result;
	}

	/**
	 * 对比两个ip大小 ip1 是否 大于 ip2
	 * */
	public static int compartTo(String ip1, String ip2) {
		// 以下方法不能判断的原因在于：
		// 例如10.4.120.5与10.50.0.0按理应该前者小，但将它们转化为数字组合后，后者位数少，所以反而变成后面一个数字更小。
		// String ip11=ip1.replace(".","");
		// String ip22=ip2.replace(".", "");
		// return new Long(ip11).compareTo(new Long(ip22));
		// 比较2个IP的顺序，按照数字顺序
		long[] ip11 = parseIp(ip1);
		long[] ip22 = parseIp(ip2);
		long ip1Result = 0, ip2Result = 0;
		for (int i = 0; i < 4; i++) {
			ip1Result += (ip11[i] << (24 - i * 8));
		}
		for (int i = 0; i < 4; i++) {
			ip2Result += (ip22[i] << (24 - i * 8));
		}
		if (ip1Result - ip2Result > 0) {
			return 1;
		} else if (ip1Result - ip2Result < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 传入数组，连接字符串
	 * */
	public static String combo(String[] range, String spli) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < range.length; i++) {
			stringBuilder.append(range[i]);
			if (i < range.length - 1) {
				stringBuilder.append(spli);
			}

		}
		return stringBuilder.toString();
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 将毫秒值转换成日期类型
	 * 
	 * @param millSec
	 * @return
	 */
	public static String FormatLongToDate(long millSec) {

		return sdf.format(millSec);
	}

	/**
	 * 将时间类型值转换成毫秒值
	 * 
	 * @param string
	 * @return
	 */
	public static long parseDateYMDHMSToMillis(String string) {
		try {
			// System.out.println(string);
			Date date = new Date(string);
			return sdf.parse(sdf.format(date).toString()).getTime();
			// return sdf.parse(string).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
		byte[][] splits = new byte[numRegions - 1][];
		BigInteger lowestKey = new BigInteger(startKey, 16);
		BigInteger highestKey = new BigInteger(endKey, 16);
		BigInteger range = highestKey.subtract(lowestKey);
		BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
		lowestKey = lowestKey.add(regionIncrement);
		for (int i = 0; i < numRegions - 1; i++) {
			BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
			byte[] b = String.format("%016x", key).getBytes();
			splits[i] = b;
		}
		return splits;
	}

	/**
	 * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 * 
	 * @param URL
	 *            url地址
	 * @return url请求参数部分
	 */
	public static Map<String, String> URLRequest(String URL) {
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit = null;

		String strUrlParam = TruncateUrlPage(URL);
		if (strUrlParam == null) {
			return mapRequest;
		}
		// 每个键值为一组 www.2cto.com
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");

			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}

	/**
	 * 去掉url中的路径，留下请求参数部分
	 * 
	 * @param strURL
	 *            url地址
	 * @return url请求参数部分
	 */
	private static String TruncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;

		strURL = strURL.trim().toLowerCase();

		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}

		return strAllParam;
	}
}
