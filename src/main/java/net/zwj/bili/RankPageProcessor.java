package net.zwj.bili;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zwj.bili.model.RankingJsonVideo;
import net.zwj.bili.model.RankingVideoInfo;
import net.zwj.bili.util.BiliUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import com.alibaba.fastjson.JSON;

/**
 * 排行表页面处理
 * 
 */
public class RankPageProcessor implements PageProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(RankPageProcessor.class);

	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
			.setTimeOut(15 * 1000);

	private boolean inited = false;

	private List<String> kinds = new ArrayList<String>();
	private Map<String, String> typeMap = new HashMap<String, String>();

	private Map<String, String> categoryMap = new HashMap<String, String>();

	private Map<String, String> urlMap = new HashMap<String, String>();

	private void initRankUrls() {
		if (inited) {
			return;
		}
		kinds.add(RankingVideoInfo.KIND_ALL);
		kinds.add(RankingVideoInfo.KIND_ORIGIN);

		typeMap.put("1", RankingVideoInfo.TYPE_DAY);
		typeMap.put("3", RankingVideoInfo.TYPE_3DAY);
		typeMap.put("7", RankingVideoInfo.TYPE_WEEK);
		typeMap.put("30", RankingVideoInfo.TYPE_MONTH);

		categoryMap.put("0", "全站");
		categoryMap.put("1", "动画");
		categoryMap.put("3", "音乐");
		categoryMap.put("129", "舞蹈");
		categoryMap.put("4", "游戏");
		categoryMap.put("36", "科技");
		categoryMap.put("5", "娱乐");
		categoryMap.put("23", "电影");
		categoryMap.put("119", "鬼畜");
		categoryMap.put("11", "电视剧");
		// 新番独立的，暂不处理
		// categoryMap.put("33", "新番");

		String urlPrefix = "http://www.bilibili.com/index/rank/";
		String url = "";
		Set<String> keys1 = typeMap.keySet();
		Set<String> keys2 = categoryMap.keySet();
		for (String str : kinds) {
			for (String k1 : keys1) {
				for (String k2 : keys2) {
					url = str + "-" + k1 + "-" + k2;
					urlMap.put(urlPrefix + url + ".json", url);
				}
			}
		}
	}

	@Override
	public void process(Page page) {

		if (!inited) {
			initRankUrls();
			Set<String> urls = urlMap.keySet();
			for (String url : urls) {
				page.addTargetRequest(url);
			}
			inited = true;
			return;
		}

		String url = page.getUrl().toString();
		if (url.trim().isEmpty()) {
			page.setSkip(true);
			return;
		}
		if (url.endsWith(".json")) {
			String key = urlMap.get(url);
			if (key != null) {
				String[] ks = key.split("-");
				if (ks != null && ks.length == 3) {
					String kind = ks[0];
					String type = typeMap.get(ks[1]);
					String category = categoryMap.get(ks[2]);
					page.putField("kind", kind);
					page.putField("type", type);
					page.putField("category", category);

					List<RankingVideoInfo> rankvideos = extractVedioInfosFromJson(
							page, "$.rank.list", kind, type, category);
					page.putField("rankvideos", rankvideos);
				}
			}

		} else {

		}
	}

	private List<RankingVideoInfo> extractVedioInfosFromJson(Page page,
			String jsonpath, String kind, String type, String category) {
		List<RankingVideoInfo> list = new ArrayList<RankingVideoInfo>();
		try {
			List<String> jsons = page.getJson().jsonPath(jsonpath).all();
			RankingVideoInfo video = null;
			int len = jsons.size();
			for (int i = 0; i < len; i++) {
				video = extractVedioInfoFromJson(jsons.get(i));
				video.setRank(i + 1);
				video.setKind(kind);
				video.setType(type);
				video.setCategory(category);
				list.add(video);
			}

		} catch (RuntimeException e) {
			logger.warn("json error!", e);
		}
		return list;
	}

	private RankingVideoInfo extractVedioInfoFromJson(String json) {

		logger.debug("ranking json: " + json);
		RankingVideoInfo video = null;
		RankingJsonVideo jv = JSON.parseObject(json, RankingJsonVideo.class);
		video = new RankingVideoInfo();
		video.setPts(BiliUtils.parseInteger(jv.getPts()));
		video.setAvcode("av" + jv.getAid());
		video.setCoin(BiliUtils.parseInteger(jv.getCoins()));
		video.setDescription(jv.getDescription());
		video.setDm(BiliUtils.parseInteger(jv.getVideo_review()));
		video.setGk(BiliUtils.parseInteger(jv.getPlay()));
		video.setPl(BiliUtils.parseInteger(jv.getReview()));
		video.setSc(BiliUtils.parseInteger(jv.getFavorites()));
		video.setTgtime(BiliUtils.translateDate(jv.getCreate()));
		video.setTitle(jv.getTitle());
		video.setImg(jv.getPic());
		video.setUp(jv.getAuthor());
		video.setOpertime(new Date());

		return video;
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {

		Spider.create(new RankPageProcessor())
				.addUrl("http://www.bilibili.com/ranking")
				.addPipeline(new RankPagePipeline()).thread(1).run();
	}
}
