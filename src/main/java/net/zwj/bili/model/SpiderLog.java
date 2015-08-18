package net.zwj.bili.model;

import java.util.Date;

public class SpiderLog extends DBModel<Long>{
	private String url;
	private Date opertime;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getOpertime() {
		return opertime;
	}
	public void setOpertime(Date opertime) {
		this.opertime = opertime;
	}
	
}
