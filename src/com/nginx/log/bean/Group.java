package com.nginx.log.bean;

public class Group {
//	<group>
//	<type>shuffleGrouping</type>
//	<rel-id>logSpout</rel-id>
//	</group>
	
	private GroupType type;
	private String ref_id;
	
	public GroupType getType() {
		return type;
	}
	public void setType(GroupType type) {
		this.type = type;
	}
	public String getRef_id() {
		return ref_id;
	}
	public void setRef_id(String ref_id) {
		this.ref_id = ref_id;
	}
	
}
