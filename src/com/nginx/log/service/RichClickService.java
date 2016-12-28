package com.nginx.log.service;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cloudera.com.fasterxml.jackson.core.type.TypeReference;
import com.cloudera.com.fasterxml.jackson.databind.ObjectMapper;
import com.nginx.log.bean.RichClick;
import com.nginx.log.bean.RichClickEnum;
import com.nginx.log.util.UtilsClass;

public class RichClickService implements Serializable {
	static final String LOG_REGEX = "\"(.*?)\"|\\[(.*?)\\]";
	public static final String REPLACE_TO_NOTHING = "";
	public static final String REPLACE_ORIGINAL = " ";
	static final String REGEX = "\\[|\\]";
	static final String REGEX_ = "\"";
	static final String REGEX__ = "/";
	static final String REGEX___ = "-";
	static final String REGEX____ = "\\.";
	static final String TOKEN_FORMAT = "yyyyMMddHHmmss";
	public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec" };

	static Pattern pattern = Pattern.compile(LOG_REGEX);
	static RichClickEnum[] cells = new RichClickEnum[] { RichClickEnum.REMOTE_ADDR, RichClickEnum.REQUEST_STATUS,
			RichClickEnum.RESPONSE_BODY_BYTES_SENT, RichClickEnum.RESPONSE_TIME, RichClickEnum.UPSTREAM_ADDR,
			RichClickEnum.REQUEST_TIME, RichClickEnum.REQUEST_MSEC, RichClickEnum.REQUEST_SOURCE,
			RichClickEnum.HTTP_REFERER, RichClickEnum.HTTP_USER_AGENT, RichClickEnum.TIME_LOCAL, RichClickEnum.COVERED };

	static RichClickEnum[] parameters = new RichClickEnum[] { RichClickEnum.URL_ELEMENT, RichClickEnum.URL_SCREEN,
			RichClickEnum.URL_TIME, RichClickEnum.URL_SYSTEM, RichClickEnum.URL_TAG, RichClickEnum.URL_BROWSER,
			RichClickEnum.URL_CALLBACK, RichClickEnum.URL_X, RichClickEnum.URL_Y };

	static RichClickEnum[] all = new RichClickEnum[] { RichClickEnum.REMOTE_ADDR, RichClickEnum.REQUEST_STATUS,
			RichClickEnum.RESPONSE_BODY_BYTES_SENT, RichClickEnum.RESPONSE_TIME, RichClickEnum.UPSTREAM_ADDR,
			RichClickEnum.REQUEST_TIME, RichClickEnum.REQUEST_MSEC, RichClickEnum.REQUEST_SOURCE,
			RichClickEnum.HTTP_REFERER, RichClickEnum.HTTP_USER_AGENT, RichClickEnum.TIME_LOCAL, RichClickEnum.COVERED,
			RichClickEnum.URL_ELEMENT, RichClickEnum.URL_SCREEN, RichClickEnum.URL_TIME, RichClickEnum.URL_SYSTEM,
			RichClickEnum.URL_TAG, RichClickEnum.URL_BROWSER, RichClickEnum.URL_CALLBACK, RichClickEnum.URL_X,
			RichClickEnum.URL_Y };

	static RichClickEnum[] mysql = new RichClickEnum[] { RichClickEnum.VID, RichClickEnum.SID,
			RichClickEnum.GOODS_NAME, RichClickEnum.SPECIAL, RichClickEnum.REACHTIME, RichClickEnum.LEAVETIME,
			RichClickEnum.LASTMODTIME, RichClickEnum.CENTERIP, RichClickEnum.URLS, RichClickEnum.DOP,
			RichClickEnum.DON, RichClickEnum.VCNT, RichClickEnum.IDX };

	/**
	 * log 文件的每一行
	 * */
	public static RichClick convert(String value) {
		RichClick richClick = new RichClick();
		String logString = value.toString();
		Matcher matcher = pattern.matcher(logString);
		String splStrings[] = logString.replaceAll(LOG_REGEX, REPLACE_TO_NOTHING).toString().split(REPLACE_ORIGINAL);
		List<String> richClickList = new ArrayList<String>();
		for (String str : splStrings) {
			if (str.hashCode() != REPLACE_TO_NOTHING.hashCode() && str.hashCode() != REGEX___.hashCode()) {
				richClickList.add(str);
			}
		}
		if (splStrings[splStrings.length - 1].hashCode() == REGEX___.hashCode()) {
			richClickList.add(REGEX___);
		}
		while (matcher.find()) {
			richClickList.add(matcher.group());
		}
		if (richClickList.size() >= 10) {
			for (int i = 0; i < cells.length; i++) {
				richClick.add(cells[i], richClickList.get(i));
			}
		}
		splitInfo(richClick);
		return richClick;
	}

	/**
	 * nginx 发送过来的json 数据，json ,首先转换为map，然后赋值枚举数组
	 * */
	public static RichClick convertJson(String json) {

		RichClick richClick = new RichClick();
		Map<String, String> map = new HashMap<String, String>();
		json.replace("\\", "/");
		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(json.getBytes("utf-8"), new TypeReference<HashMap<String, String>>() {
			});
			for (int i = 0; i < all.length; i++) {
				RichClickEnum richclickEnum = all[i];
				String value = map.get(richclickEnum.toString());
				if (value != null) {
					richClick.add(richclickEnum, value);
				}
			}
			// 说明未转换
			if (richClick.get(RichClickEnum.COVERED) == null) {
				splitInfo(richClick);
			}
		} catch (Exception e) {
			System.out.println("-xx错误-----------" + json);
			e.printStackTrace();
			return null;
		}

		return richClick;
	}

	/**
	 * nginx 发送过来的json 数据，json ,首先转换为map，然后赋值枚举数组
	 * */
	public static RichClick convertMySQL(ResultSet result) {
		RichClick richClick = new RichClick();
		for (int i = 0; i < mysql.length; i++) {
			RichClickEnum key = mysql[i];
			try {
				String value = result.getString(key.toString());

				if (value != null) {
					richClick.add(key, value);
				} else {
					switch (key) {
					case LEAVETIME:
					case REACHTIME:
					case LASTMODTIME:
						richClick.add(key, "0000-00-00 00:00:00");
						break;
					default:
						break;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return richClick;
	}

	@SuppressWarnings({ "unchecked", "incomplete-switch" })
	private static void splitInfo(RichClick richClick) {
		HashMap<RichClickEnum, String> richClickMap = richClick.getRichClick();
		HashMap<RichClickEnum, String> copyRichClickMap = (HashMap<RichClickEnum, String>) richClickMap.clone();
		for (RichClickEnum richClickEnum : richClickMap.keySet()) {
			String value = richClickMap.get(richClickEnum);
			switch (richClickEnum) {
			case HTTP_REFERER:
				copyRichClickMap.put(richClickEnum, value.replace(REGEX_, REPLACE_TO_NOTHING));
				break;
			case TIME_LOCAL:
				String[] times = splitTime(value.replaceAll(REGEX, REPLACE_TO_NOTHING));
				copyRichClickMap.put(richClickEnum, times[0]);
				copyRichClickMap.put(RichClickEnum.REQUEST_TIMEZONE, times[1]);
				break;
			case REQUEST_MSEC:
				copyRichClickMap.put(richClickEnum, value.replaceAll(REGEX, REPLACE_TO_NOTHING));
				break;
			case HTTP_USER_AGENT:
				copyRichClickMap.put(richClickEnum, value.replace(REGEX_, REPLACE_TO_NOTHING));
				break;
			case REQUEST_SOURCE:
				String sourseInfo = value.replace(REGEX_, REPLACE_TO_NOTHING);
				String[] sources = sourseInfo.split(REPLACE_ORIGINAL);
				copyRichClickMap.put(RichClickEnum.REQUEST_TYPE, sources[0]);
				copyRichClickMap.put(richClickEnum, sources[1]);
				splitUrl(copyRichClickMap, sources[1]);
				copyRichClickMap.put(RichClickEnum.REQUEST_HTTPVERSION, sources[2]);
				break;
			}
		}
		richClick.setRichClick(copyRichClickMap);

	}

	public static void splitUrl(HashMap<RichClickEnum, String> copyRichClickMap, String url) {
		Map<String, String> parmerMap = UtilsClass.URLRequest(url);
		if (parmerMap.size() == 0)
			return;
		for (int i = 0; i < parameters.length; i++) {
			String key = parameters[i].toString();
			String value = parmerMap.get(key);
			if (value != null) {
				copyRichClickMap.put(parameters[i], value);
			}
		}
	}

	public static void TestsplitUrl(String url) {
		Map<String, String> parmerMap = UtilsClass.URLRequest(url);
		for (int i = 0; i < parameters.length; i++) {
			String key = parameters[i].toString();
			String value = parmerMap.get(key);
			if (value != null) {
				System.out.println(key + "  " + value);
			}
		}
	}

	/** 返回时间(MM/dd/yyyy) 和时区 */
	public static String[] splitTime(String timeString) {

		String _dateStrings[] = new String[3];
		String[] tStrings = timeString.split(REPLACE_ORIGINAL);
		_dateStrings[0] = tStrings[0];
		_dateStrings[1] = tStrings[1];
		String time = _dateStrings[0];
		_dateStrings[0] = time.replaceFirst(":", REPLACE_ORIGINAL);
		for (int i = 0; i < MONTHS.length; i++) {
			_dateStrings[0] = _dateStrings[0].replaceFirst(MONTHS[i], String.valueOf((i + 1)));
		}
		String[] timeStrings = _dateStrings[0].split(REPLACE_ORIGINAL);
		String[] dmyStrings = timeStrings[0].split(REGEX__);

		String ddString = dmyStrings[0];
		String MMString = dmyStrings[1];
		StringBuilder _stringBuilder = new StringBuilder();
		_stringBuilder.append(dmyStrings[2]);
		_stringBuilder.append(REGEX___);
		_stringBuilder.append(MMString);
		_stringBuilder.append(REGEX___);
		_stringBuilder.append(ddString);
		_stringBuilder.append(REPLACE_ORIGINAL);
		_stringBuilder.append(timeStrings[1]);
		_dateStrings[0] = _stringBuilder.toString();

		return _dateStrings;
	}
}
