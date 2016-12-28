package com.nginx.log.bean;

import java.io.Serializable;

public enum YT_visitlogEnum  implements Serializable{

//	  `vid` int(10) NOT NULL AUTO_INCREMENT,
//	  `sid` int(10) DEFAULT NULL COMMENT '专题id',
//	  `goods_name` varchar(500) DEFAULT NULL COMMENT '专题名称',
//	  `special` varchar(500) DEFAULT NULL COMMENT '类型',
//	  `reachtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
//	  `leavetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '离开时间',
//	  `lastmodtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最后动作时间',
//	  `centerIp` varchar(500) DEFAULT NULL COMMENT 'ip',
//	  `urls` varchar(500) DEFAULT NULL COMMENT '访问url',
//	  `dop` int(10) DEFAULT '0' COMMENT '正向动作',
//	  `don` int(10) DEFAULT '0' COMMENT '负向动作',
//	  `vcnt` int(10) DEFAULT '0' COMMENT '访问次数',
//	  `idx` varchar(300) DEFAULT NULL COMMENT '备用',
	VID("vid"),SID("sid"),GOODS_NAME("goods_name")
	,SPECIAL("special"),REACHTIME("reachtime"),LEAVETIME("leavetime")
	,LASTMODTIME("lastmodtime"),CENTERIP("centerip"),URLS("urls")
	,DOP("dop"),DON("don"),VCNT("vcnt"),
	IDX("idx");
	private String value;
	private YT_visitlogEnum(String value){
		this.value=value;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.value;
	}
}
