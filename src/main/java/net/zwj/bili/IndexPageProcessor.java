package net.zwj.bili;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zwj.bili.model.JsonVideo;
import net.zwj.bili.model.PageVideoInfo;
import net.zwj.bili.util.BiliUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 主页处理
 * 
 * @author zhouwj
 * 
 */
public class IndexPageProcessor implements PageProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(IndexPageProcessor.class);

	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
			.setTimeOut(15 * 1000);

	private Map<String, String> areaMap = new HashMap<String, String>();

	public IndexPageProcessor() {
		areaMap.put("bangumi", "番组");
		areaMap.put("dance", "舞蹈");
		areaMap.put("douga", "动画");
		areaMap.put("ent", "娱乐");
		areaMap.put("game", "游戏");
		areaMap.put("kichiku", "鬼畜");
		areaMap.put("music", "音乐");
		areaMap.put("part", "电视剧");
		areaMap.put("technology", "科技");
		areaMap.put("1", "动画");
		areaMap.put("3", "音乐");
		areaMap.put("129", "舞蹈");
		areaMap.put("4", "游戏");
		areaMap.put("36", "科学·技术");
		areaMap.put("5", "娱乐");
		areaMap.put("119", "鬼畜");

	}

	@Override
	public void process(Page page) {

		String url = page.getUrl().toString();
		if (url.trim().isEmpty()) {
			page.setSkip(true);
			return;
		}
		if (url.endsWith(".json")) {
			dealJsonInfo(url, page);

		} else {
			// 首页上面的东西基本上都是ajax的,简单起见直接写死地址
			// 1.首页排行
			page.addTargetRequest("http://www.bilibili.com/index/ranking.json");
			page.addTargetRequest("http://www.bilibili.com/index/ranking-3day.json");
			page.addTargetRequest("http://www.bilibili.com/index/ranking-week.json");

			// 2.推广
			page.addTargetRequest("http://www.bilibili.com/index/promote.json");

			// 3.分区动态
			page.addTargetRequest("http://www.bilibili.com/index/ding.json");

			// 4.分区排行
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/1-3day.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/1-week.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/3-3day.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/3-week.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/129-3day.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/129-week.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/4-3day.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/4-week.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/36-3day.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/36-week.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/5-3day.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/5-week.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/119-3day.json");
			page.addTargetRequest("http://www.bilibili.com/index/catalogy/119-week.json");

			// 5.特别推荐
			page.addTargetRequest("http://www.bilibili.com/index/recommend.json");

		}
	}

	private void dealJsonInfo(String url, Page page) {
		List<PageVideoInfo> infos = null;
		String type = "";
		String jsonpath = "";
		boolean withrank = false;
		boolean specialDealed = false;
		boolean parseArray = false;
		String sarea = "";
		if (url.equals("http://www.bilibili.com/index/ranking.json")) {
			type = PageVideoInfo.TYPE_INDEX_RANKING_DAY;
			jsonpath = "$.recommend.list";
			withrank = true;
		} else if (url
				.equals("http://www.bilibili.com/index/ranking-3day.json")) {
			type = PageVideoInfo.TYPE_INDEX_RANKING_3DAY;
			jsonpath = "$.recommend.list";
			withrank = true;
		} else if (url
				.equals("http://www.bilibili.com/index/ranking-week.json")) {
			type = PageVideoInfo.TYPE_INDEX_RANKING_WEEK;
			jsonpath = "$.recommend.list";
			withrank = true;
		} else if (url.equals("http://www.bilibili.com/index/promote.json")) {
			type = PageVideoInfo.TYPE_INDEX_PREMOTE;
			jsonpath = "$.list";
			withrank = false;
		} /*else if (url.equals("http://www.bilibili.com/index/ding.json")) {
			infos = extractVedioInfosDing(page);
			specialDealed = true;
		} */else if (url
				.matches("http://www.bilibili.com/index/catalogy/.+3day.json")) {
			sarea = url.substring(url.lastIndexOf('/') + 1, url.indexOf('-'));
			withrank = true;
			type = PageVideoInfo.TYPE_INDEX_PAGERIGHTHOTALL3;
			jsonpath = "$.hot.list";
			infos = extractVedioInfosFromJson(page, jsonpath, type, sarea,
					withrank, true);
			type = PageVideoInfo.TYPE_INDEX_PAGERIGHTHOTORIGIN3;
			jsonpath = "$.hot_original.list";
			infos.addAll(extractVedioInfosFromJson(page, jsonpath, type, sarea,
					withrank, true));
			specialDealed = true;
		} else if (url
				.matches("http://www.bilibili.com/index/catalogy/.+week.json")) {
			sarea = url.substring(url.lastIndexOf('/') + 1, url.indexOf('-'));
			withrank = true;
			type = PageVideoInfo.TYPE_INDEX_PAGERIGHTHOTALL7;
			jsonpath = "$.hot.list";
			infos = extractVedioInfosFromJson(page, jsonpath, type, sarea,
					withrank, true);
			type = PageVideoInfo.TYPE_INDEX_PAGERIGHTHOTORIGIN7;
			jsonpath = "$.hot_original.list";
			infos.addAll(extractVedioInfosFromJson(page, jsonpath, type, sarea,
					withrank, true));
			specialDealed = true;
		} else if (url.equals("http://www.bilibili.com/index/recommend.json")) {
			withrank = false;
			type = PageVideoInfo.TYPE_INDEX_RECOMMEND;
			jsonpath = "$.list";
			parseArray = true;
		} else {
			specialDealed = true;
			logger.error("index data url not config! url: " + url);
		}

		if (!specialDealed) {
			infos = extractVedioInfosFromJson(page, jsonpath, type, sarea,
					withrank, parseArray);
		}
		if (infos != null) {
			page.putField("videolist", infos);
		}
	}

	/*
	private List<PageVideoInfo> extractVedioInfosDing(Page page) {
		List<PageVideoInfo> list = new ArrayList<PageVideoInfo>();
		try {

			String jsonStr = page.getJson().toString();

			@SuppressWarnings("unchecked")
			Map<String, JSONObject> map = (Map<String, JSONObject>) JSON
					.parse(jsonStr);
			Set<String> areas = map.keySet();
			PageVideoInfo video = null;
			JSONObject jo = null;
			for (String area : areas) {
				if (area.equals("list") || area.equals("pages")
						|| area.equals("results")) {
					continue;
				}
				jo = map.get(area);
				if (jo != null) {
					@SuppressWarnings("unchecked")
					Map<String, JSONObject> contentMap = (Map<String, JSONObject>) JSON
							.parse(jo.toJSONString());
					Set<String> keys = contentMap.keySet();

					for (String key : keys) {
						jo = contentMap.get(key);
						if (jo != null) {
							video = extractVedioInfoFromJson(jo.toString(),
									PageVideoInfo.TYPE_INDEX_PAGELEFT, area);
							list.add(video);
						}
					}
				}

			}
		} catch (RuntimeException e) {
			logger.warn("json error!", e);
		}

		return list;
	}
*/
	
	private List<PageVideoInfo> extractVedioInfosFromJson(Page page,
			String jsonpath, String type, String area, boolean withRank,
			boolean parseArray) {
		List<PageVideoInfo> list = new ArrayList<PageVideoInfo>();
		try {

			String jsonStr = page.getJson().jsonPath(jsonpath).toString();
			if (!parseArray) {

				@SuppressWarnings("unchecked")
				Map<String, JSONObject> map = (Map<String, JSONObject>) JSON
						.parse(jsonStr);
				Set<String> keys = map.keySet();
				PageVideoInfo video = null;
				Integer rank = null;
				JSONObject jo = null;
				for (String key : keys) {
					jo = map.get(key);
					if (jo != null) {
						video = extractVedioInfoFromJson(jo.toString(), type,
								area);

						if (withRank) {

							try {
								rank = Integer.parseInt(key);
								video.setRank(rank + 1);
							} catch (RuntimeException e) {
								// ignore
							}
						}
						list.add(video);
					}

				}
			} else {
				List<String> jsons = page.getJson().jsonPath(jsonpath).all();
				PageVideoInfo video = null;
				int len = jsons.size();
				for (int i = 0; i < len; i++) {
					video = extractVedioInfoFromJson(jsons.get(i), type, area);
					if (withRank) {
						video.setRank(i + 1);
					}
					list.add(video);
				}
			}

		} catch (RuntimeException e) {
			logger.warn("json error!", e);
		}
		return list;
	}

	private PageVideoInfo extractVedioInfoFromJson(String json, String type,
			String areacode) {

		logger.debug("index json: " + json);
		PageVideoInfo video = null;
		JsonVideo jv = JSON.parseObject(json, JsonVideo.class);
		video = new PageVideoInfo();
		video.setAvcode("av" + jv.getAid());
		video.setCoin(BiliUtils.parseInteger(jv.getCoins()));
		video.setDescription(jv.getDescription());
		video.setDm(BiliUtils.parseInteger(jv.getVideo_review()));
		video.setGk(BiliUtils.parseInteger(jv.getPlay()));
		video.setLm("");
		video.setOpertime(new Date());
		video.setPl(BiliUtils.parseInteger(jv.getReview()));
		video.setSc(BiliUtils.parseInteger(jv.getFavorites()));
		video.setSubtitle("");
		video.setTgtime(BiliUtils.translateDate(jv.getCreate()));
		video.setDuration(jv.getDuration());
		video.setTitle(jv.getTitle());
		video.setImg(jv.getPic());
		video.setUp(jv.getAuthor());

		video.setType(type);
		video.setAreaname(areaMap.get(areacode));
		video.setBilicode(areacode);

		return video;
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {

		Spider.create(new IndexPageProcessor())
				.addUrl("http://www.bilibili.com/index.html")
				.addPipeline(new IndexPagePipeline()).thread(1).run();
	}
}
