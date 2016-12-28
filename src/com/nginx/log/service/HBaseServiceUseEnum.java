package com.nginx.log.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.hbase.client.Put;

import com.nginx.log.bean.PropertiesType;
import com.nginx.log.bean.RichClick;
import com.nginx.log.bean.RichClickEnum;
import com.nginx.log.util.HBaseUtil;
import com.nginx.log.util.UtilsClass;

public class HBaseServiceUseEnum implements Serializable {

	HBaseUtil hbaseUtil = new HBaseUtil();

	public static final String TABLE_NAME = "richclick2";
	public static final byte[] FAMILY = "info".getBytes();
	public static final String CONNECT_CHART = "-";

	public boolean insert(List<RichClick> richClicks) throws IOException {
		return HBaseUtil.Put(TABLE_NAME, getPuts(richClicks));

	}

	private List<Put> getPuts(List<RichClick> richClicks) {
		List<Put> puts = new ArrayList<Put>();
		for (int i = 0; i < richClicks.size(); i++) {
			RichClick richClick = richClicks.get(i);
			HashMap<RichClickEnum, String> richClickMap = richClick.getRichClick();
			Put put = new Put(granrateKey(richClick).getBytes());
			for (RichClickEnum rick : richClickMap.keySet()) {
				if (richClickMap.get(rick) != null)
					put.add(FAMILY, rick.toString().getBytes(), richClickMap.get(rick).toString().getBytes());
			}
			puts.add(put);
		}
		return puts;

	}

	private String granrateKey(RichClick richClick) {
		try {
			HashMap<RichClickEnum, String> richClickMap = richClick.getRichClick();
			String remote = null;
			String secondPart = null;
			switch (PropertiesType.SOURCE_TYPE) {
			case MYSQL:
				remote = richClickMap.get(RichClickEnum.CENTERIP);
				secondPart = richClickMap.get(RichClickEnum.VID);
				break;
			default:
				remote = richClickMap.get(RichClickEnum.REMOTE_ADDR);
				secondPart = richClickMap.get(RichClickEnum.REQUEST_MSEC);
				break;

			}
			if (remote != null && !remote.equals("-")) {
				String prefix = UtilsClass.ip_add_zero(remote);
				return String.format("%s%s%s", prefix, CONNECT_CHART, secondPart);
			} else {
				return newKey();
			}

		} catch (Exception e) {
			return newKey();
		}

	}

	private String newKey() {
		return UUID.randomUUID().toString();
	}
}

// switch (PropertiesType.SOURCE_TYPE) {
// case MYSQL:
// String remote = richClickMap.get(RichClickEnum.REMOTE_ADDR);
// if (remote != null && !remote.equals("-")) {
// String prefix = UtilsClass.ip_add_zero(remote);
// String secondPart = richClickMap.get(RichClickEnum.REQUEST_MSEC);
// return String.format("%s%s%s", prefix, CONNECT_CHART, secondPart);
// } else {
// return newKey();
// }
// break;
// default:
// String remote = richClickMap.get(RichClickEnum.REMOTE_ADDR);
// if (remote != null && !remote.equals("-")) {
// String prefix = UtilsClass.ip_add_zero(remote);
// String secondPart = richClickMap.get(RichClickEnum.REQUEST_MSEC);
// return String.format("%s%s%s", prefix, CONNECT_CHART, secondPart);
// } else {
// return newKey();
// }
