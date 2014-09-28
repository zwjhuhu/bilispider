package net.zwj.bili.model;

import java.util.Date;

public class RankingVideoInfo extends DBModel<Long> {
	public static final String TYPE_DAY = "day";
	public static final String TYPE_WEEK = "week";
	public static final String TYPE_MONTH = "month";

	private String type;
	private String category;
	private Integer rank;
	private String avcode;
	private Integer pts;
	private String title;
	private String img;
	private Date opertime;

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
