package net.zwj.bili.model;

import java.util.Date;

public class RankingVideoInfo extends DBModel<Long> {

	public static final String KIND_ALL = "all";
	public static final String KIND_ORIGIN = "origin";

	public static final String TYPE_DAY = "day";
	public static final String TYPE_3DAY = "3day";
	public static final String TYPE_WEEK = "week";
	public static final String TYPE_MONTH = "month";

	private String kind;
	private String type;
	private String category;
	private Integer rank;
	private String avcode;

	private String description;
	private Date tgtime;
	private String up;

	// 硬币
	private Integer coin;
	// 弹幕
	private Integer dm;
	// 评论
	private Integer pl;
	// 观看
	private Integer gk;
	// 收藏
	private Integer sc;

	private Integer pts;
	private String title;
	private String img;
	private Date opertime;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getAvcode() {
		return avcode;
	}

	public void setAvcode(String avcode) {
		this.avcode = avcode;
	}

	public Integer getPts() {
		return pts;
	}

	public void setPts(Integer pts) {
		this.pts = pts;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Date getOpertime() {
		return opertime;
	}

	public void setOpertime(Date opertime) {
		this.opertime = opertime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTgtime() {
		return tgtime;
	}

	public void setTgtime(Date tgtime) {
		this.tgtime = tgtime;
	}

	public String getUp() {
		return up;
	}

	public void setUp(String up) {
		this.up = up;
	}

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}

	public Integer getDm() {
		return dm;
	}

	public void setDm(Integer dm) {
		this.dm = dm;
	}

	public Integer getPl() {
		return pl;
	}

	public void setPl(Integer pl) {
		this.pl = pl;
	}

	public Integer getGk() {
		return gk;
	}

	public void setGk(Integer gk) {
		this.gk = gk;
	}

	public Integer getSc() {
		return sc;
	}

	public void setSc(Integer sc) {
		this.sc = sc;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RankingVideoInfo other = (RankingVideoInfo) obj;
		if (avcode == null) {
			if (other.avcode != null)
				return false;
		} else if (!avcode.equals(other.avcode))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (img == null) {
			if (other.img != null)
				return false;
		} else if (!img.equals(other.img))
			return false;
		if (pts == null) {
			if (other.pts != null)
				return false;
		} else if (!pts.equals(other.pts))
			return false;
		if (rank == null) {
			if (other.rank != null)
				return false;
		} else if (!rank.equals(other.rank))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
