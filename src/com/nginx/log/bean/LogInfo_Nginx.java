package com.nginx.log.bean;
	import java.io.Serializable;

	public class LogInfo_Nginx implements Serializable {
		private String _SPLIT;

		public LogInfo_Nginx(String _SPLITwith) {
			this._SPLIT = _SPLITwith;
		}

		public LogInfo_Nginx() {
			this._SPLIT = "";
		}

		private String HBaseId;
		private String HBaseIdTime;

		public String getHBaseIdTime() {
			return HBaseIdTime;
		}

		public void setHBaseIdTime(String hBaseIdTime) {
			HBaseIdTime = hBaseIdTime;
		}
		
		private String Country_Code;
		private int Area_Code;
		private int Region_Code;
		private int City_Code;

		private String IP;
		private String Time;
		private String TimeZone;
		
		private String Type;
		private String Request_source;
		private int Status;
		private String HTTP_Version;
	

		private long Size;
		private String URL;
		private String Broswer;
		private double MS;

		public String getCountry_Code() {
			return Country_Code;
		}

		public void setCountry_Code(String country_Code) {
			Country_Code = country_Code;
		}

		public int getArea_Code() {
			return Area_Code;
		}

		public void setArea_Code(int area_Code) {
			Area_Code = area_Code;
		}

		public int getRegion_Code() {
			return Region_Code;
		}

		public void setRegion_Code(int region_Code) {
			Region_Code = region_Code;
		}

		public int getCity_Code() {
			return City_Code;
		}

		public void setCity_Code(int city_Code) {
			City_Code = city_Code;
		}

		
		public String getHTTP_Version() {
			return HTTP_Version;
		}

		public void setHTTP_Version(String hTTP_Version) {
			HTTP_Version = hTTP_Version;
		}
		public String getTimeZone() {
			return TimeZone;
		}

		public void setTimeZone(String timeZone) {
			TimeZone = timeZone;
		}

		public String getIP() {
			return IP;
		}

		public void setIP(String iP) {
			IP = iP;
		}

		public String getTime() {
			return Time;
		}

		public void setTime(String time) {
			Time = time;
		}

		public String getType() {
			return Type;
		}

		public void setType(String type) {
			Type = type;
		}

		public String getRequest_source() {
			return Request_source;
		}

		public void setRequest_source(String request_source) {
			Request_source = request_source;
		}

		public int getStatus() {
			return Status;
		}

		public void setStatus(int status) {
			Status = status;
		}

		public long getSize() {
			return Size;
		}

		public void setSize(long size) {
			Size = size;
		}

		public String getURL() {
			return URL;
		}

		public void setURL(String uRL) {
			URL = uRL;
		}

		public String getBroswer() {
			return Broswer;
		}

		public void setBroswer(String broswer) {
			Broswer = broswer;
		}

		public double getMS() {
			return MS;
		}

		public void setMS(double mS) {
			MS = mS;
		}
		public String getHBaseId() {
			return HBaseId;
		}

		public void setHBaseId(String hBaseId) {
			HBaseId = hBaseId;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			sb.append(getHBaseId()).append(_SPLIT);
			sb.append(getIP()).append(_SPLIT);
			sb.append(getTime()).append(_SPLIT);
			sb.append(getTimeZone()).append(_SPLIT);
			sb.append(getType()).append(_SPLIT);
			sb.append(getRequest_source()).append(_SPLIT);
			sb.append(getHTTP_Version()).append(_SPLIT);
			sb.append(getStatus()).append(_SPLIT);
			sb.append(getSize()).append(_SPLIT);
			
			sb.append(getURL()).append(_SPLIT);
			sb.append(getBroswer()).append(_SPLIT);
			sb.append(getMS());//.append(_SPLIT);
			
//			sb.append(getCountry_Code()).append(_SPLIT);
//			sb.append(getArea_Code()).append(_SPLIT);
//			sb.append(getRegion_Code()).append(_SPLIT);
//			sb.append(getCity_Code());
			return sb.toString();
		}

	}


