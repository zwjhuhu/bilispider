package net.zwj.bili.model;

/**
 * bilibili的页面分类
 * 
 * 
 */
public class CategoryInfo extends DBModel<Long> {

	private String code;

	private String bcode;

	private String title;

	private String url;

	private Integer parentid;
	
	private boolean active;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBcode() {
		return bcode;
	}

	public void setBcode(String bcode) {
		this.bcode = bcode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
