package com.nginx.log.bean;

import java.io.Serializable;

public enum YT_visitlogEnum  implements Serializable{

//	  `vid` int(10) NOT NULL AUTO_INCREMENT,
//	  `sid` int(10) DEFAULT NULL COMMENT 'ר��id',
//	  `goods_name` varchar(500) DEFAULT NULL COMMENT 'ר������',
//	  `special` varchar(500) DEFAULT NULL COMMENT '����',
//	  `reachtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
//	  `leavetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '�뿪ʱ��',
//	  `lastmodtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '�����ʱ��',
//	  `centerIp` varchar(500) DEFAULT NULL COMMENT 'ip',
//	  `urls` varchar(500) DEFAULT NULL COMMENT '����url',
//	  `dop` int(10) DEFAULT '0' COMMENT '������',
//	  `don` int(10) DEFAULT '0' COMMENT '������',
//	  `vcnt` int(10) DEFAULT '0' COMMENT '���ʴ���',
//	  `idx` varchar(300) DEFAULT NULL COMMENT '����',
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
