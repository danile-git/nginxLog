package com.nginx.log.bean;

import java.io.Serializable;
import java.util.HashMap;

public class RichClick implements Serializable{
	private HashMap<RichClickEnum, String> hashMap;

	public RichClick() {
		hashMap = new HashMap<RichClickEnum, String>();
	}

	public void add(RichClickEnum richClickEnum, String value) {
		hashMap.put(richClickEnum, value);
	}
	public String get(RichClickEnum richClickEnum) {
		return this.hashMap.get(richClickEnum);
	}
	public HashMap<RichClickEnum, String> getRichClick() {
		return this.hashMap;
	}

	public void setRichClick(HashMap<RichClickEnum, String> hashMap) {
		this.hashMap = hashMap;
	}

	public int getRichClickSize() {
		return this.hashMap.size();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		int num = 0;
		for (RichClickEnum rick : this.hashMap.keySet()) {
			stringBuilder.append(hashMap.get(rick));
			if (num < this.hashMap.size() - 1)
				stringBuilder.append("#");
			num++;
		}
		return stringBuilder.toString();
	}

	public String toString(String split) {
		StringBuilder stringBuilder = new StringBuilder();
		int num = 0;
		for (RichClickEnum rick : this.hashMap.keySet()) {
			stringBuilder.append(hashMap.get(rick));
			if (num < this.hashMap.size() - 1)
				stringBuilder.append(split);
			num++;
		}
		return stringBuilder.toString();
	}

	public String toKeyValue() {
		StringBuilder stringBuilder = new StringBuilder();
		for (RichClickEnum rick : this.hashMap.keySet()) {
			stringBuilder.append("key :" + rick + " value : " + hashMap.get(rick) + "\n");
		}
		return stringBuilder.toString();
	}
}
