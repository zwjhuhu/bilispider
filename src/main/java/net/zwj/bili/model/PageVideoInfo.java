package net.zwj.bili.model;

import java.util.Date;

/**
 * 在主页面上显示的视频信息
 * 
 * @author zhouwj
 * 
 */
public class PageVideoInfo extends DBModel<Long> implements
		Comparable<PageVideoInfo> {
	/**
	 * 页面左侧动态
	 */
	public static final String TYPE_PAGELEFT = "pageleft";

	/**
	 * 页面热门全部3天
	 */
	public static final String TYPE_PAGERIGHTHOTALL3 = "pagehot3";

	/**
	 * 页面热门全部一周
	 */
	public static final String TYPE_PAGERIGHTHOTALL7 = "pagehot7";

	/**
	 * 页面热门原创3天
	 */
	public static final String TYPE_PAGERIGHTHOTORIGIN3 = "pagehotorigin3";

	/**
	 * 页面热门原创一周
	 */
	public static final String TYPE_PAGERIGHTHOTORIGIN7 = "pagehotorigin7";

	/**
	 * 主页的排行-天
	 */
	public static final String TYPE_INDEX_RANKING_DAY = "index_rank_day";

	/**
	 * 主页的排行-三天
	 */
	public static final String TYPE_INDEX_RANKING_3DAY = "index_rank_3day";

	/**
	 * 主页的排行-周
	 */
	public static final String TYPE_INDEX_RANKING_WEEK = "index_rank_week";

	/**
	 * 主页左侧动态
	 */
	public static final String TYPE_INDEX_PAGELEFT = "index_pageleft";

	/**
	 * 主页热门全部3天
	 */
	public static final String TYPE_INDEX_PAGERIGHTHOTALL3 = "index_pagehot3";

	/**
	 * 主页热门全部一周
	 */
	public static final String TYPE_INDEX_PAGERIGHTHOTALL7 = "index_pagehot7";

	/**
	 * 主页热门原创3天
	 */
	public static final String TYPE_INDEX_PAGERIGHTHOTORIGIN3 = "index_pagehotorigin3";

	/**
	 * 主页热门原创一周
	 */
	public static final String TYPE_INDEX_PAGERIGHTHOTORIGIN7 = "index_pagehotorigin7";

	/**
	 * 主页热门推广
	 */
	public static final String TYPE_INDEX_PREMOTE = "index_premote";

	/**
	 * 主页特别推荐
	 */
	public static final String TYPE_INDEX_RECOMMEND = "index_recommend";

	private String avcode;

	private String title;

	private String subtitle;

	private String description;

	private Date tgtime;

	private String up;

	// 硬币
	private Integer coin;

	// TODO 不知道含义
	private String lm;

	// 弹幕
	private Integer dm;

	// 评论
	private Integer pl;

	// 观看
	private Integer gk;

	// 收藏
	private Integer sc;

	// 预览图
	private String img;

	// 时长
	private String duration;

	// 分区名称
	private String areaname;

	// 分区的bilibili的代码
	private String bilicode;

	// 类别
	private String type;

	// 排名
	private Integer rank;

	// 处理时间
	private Date opertime;

	public String getAvcode() {
		return avcode;
	}

	public void setAvcode(String avcode) {
		this.avcode = avcode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
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

	public String getLm() {
		return lm;
	}

	public void setLm(String lm) {
		this.lm = lm;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAreaname() {
		return areaname;
	}

	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}

	public String getBilicode() {
		return bilicode;
	}

	public void setBilicode(String bilicode) {
		this.bilicode = bilicode;
	}

	public Date getOpertime() {
		return opertime;
	}

	public void setOpertime(Date opertime) {
		this.opertime = opertime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Override
	public int compareTo(PageVideoInfo o) {
		if (o == null || o.getRank() == null) {
			return 1;
		} else {
			if (this.getRank() == null) {
				return -1;
			} else {
				return this.getRank() - o.getRank();
			}
		}
	}

}
