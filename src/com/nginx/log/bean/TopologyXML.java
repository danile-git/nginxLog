package com.nginx.log.bean;

public class TopologyXML {
//	<spouts>
//	 <spout id="logSpout">
//	 	<class>com.nginx.log.core.LogSpout</class>
//	 	<parallelism>2</parallelism>
//	 </spout>
//</spouts>
//	<bolts>
//	<bolt id="logBolt">
//		<class>com.nginx.log.core.LogBoltBackup</class>
//	 	<parallelism>10</parallelism>
//	 	<group>
//			<type>shuffleGrouping</type>
//			<rel-id>logSpout</rel-id>
//	 	</group>
//	</bolt>
//</bolts>
	private TopologyType type;
	private String id;
	private Class<?> clazz;
	private int parallelism;
	private Group group;
	
	public TopologyType getType() {
		return type;
	}
	public void setType(TopologyType type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public int getParallelism() {
		return parallelism;
	}
	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
}
