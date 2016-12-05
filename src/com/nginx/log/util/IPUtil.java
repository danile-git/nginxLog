package com.nginx.log.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.nginx.log.bean.LogInfo_Nginx;

import net.sf.json.JSONObject;

public class IPUtil {

	public static final String OUTPUT_DATA_NAME = "data";
	public static final String OUTPUT_DATA_CODE = "code";
	public static final String OUTPUT_DATA_STATUS = "0";
	public static final String OUTPUT_DATA_COUNTRY = "country_id";
	public static final String OUTPUT_DATA_AREA = "area_id";
	public static final String OUTPUT_DATA_REGION = "region_id";
	public static final String OUTPUT_DATA_CITY = "city_id";
	public static final String URL = "http://ip.taobao.com/service/getIpInfo.php?ip=";
	/**
	 * 
	 * @param IP
	 * @return
	 */
	public static void GetAddressByIp(LogInfo_Nginx _LogInfo_Nginx) {
		
		try {
			String outString = getJsonContent(URL + _LogInfo_Nginx.getIP());
			JSONObject jsonObject = JSONObject.fromObject(outString);
			JSONObject jsonObject_out = (JSONObject) jsonObject.get(OUTPUT_DATA_NAME);
			String code = jsonObject.get(OUTPUT_DATA_CODE).toString();
			if (code.equals(OUTPUT_DATA_STATUS)) {
				String country_Code=jsonObject_out.get(OUTPUT_DATA_COUNTRY).toString();
				int area_Code=Integer.parseInt( jsonObject_out.get(OUTPUT_DATA_AREA).toString());
				int region_Code=Integer.parseInt( jsonObject_out.get(OUTPUT_DATA_REGION).toString());
				int city_Code=Integer.parseInt( jsonObject_out.get(OUTPUT_DATA_CITY).toString());
				_LogInfo_Nginx.setCountry_Code(country_Code);
				_LogInfo_Nginx.setArea_Code(area_Code);
				_LogInfo_Nginx.setRegion_Code(region_Code);
				_LogInfo_Nginx.setCity_Code(city_Code);
			} 
		} catch (Exception e) {

			e.printStackTrace();
		
		}

	}

	public static String getJsonContent(String urlStr) {
		try {// ��ȡHttpURLConnection���Ӷ���
			URL url = new URL(urlStr);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			// ������������
			httpConn.setConnectTimeout(3000);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("GET");
			// ��ȡ��Ӧ��
			int respCode = httpConn.getResponseCode();
			if (respCode == 200) {
				return ConvertStream2Json(httpConn.getInputStream());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String ConvertStream2Json(InputStream inputStream) {
		String jsonStr = "";
		// ByteArrayOutputStream�൱���ڴ������
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		// ��������ת�Ƶ��ڴ��������
		try {
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			// ���ڴ���ת��Ϊ�ַ���
			jsonStr = new String(out.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonStr;
	}
}
