package net.zwj.bili;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.zwj.bili.db.DbUtils;
import net.zwj.bili.model.CategoryInfo;
import net.zwj.bili.model.JsonVideo;
import net.zwj.bili.model.PageVideoInfo;
import net.zwj.bili.util.BiliUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 分区页面处理
 * 
 * @author zhouwj
 * 
 */
public class CategoryPageProcessor implements PageProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(CategoryPageProcessor.class);

	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
			.setTimeOut(15 * 1000);

	private List<CategoryInfo> topPageInfos = new ArrayList<CategoryInfo>();

	private ConcurrentHashMap<String, String> areaMap = new ConcurrentHashMap<String, String>();

	boolean initInfos = false;

	@Override
	public void process(Page page) {

		if (!initInfos) {
			try {
				topPageInfos = DbUtils
						.findBYSql(
								"select * from categoryinfo where parentid is null and active = 1 "
										+ "and url like 'http://www.bilibili.com/video/%'",
								CategoryInfo.class);
				initInfos = true;
				for (CategoryInfo info : topPageInfos) {
					page.addTargetRequest(info.getUrl());
				}
			} catch (RuntimeException e) {
				logger.error("init pageinfos error!", e);
			}
		} else {

			String url = page.getUrl().toString();
			if (url.trim().isEmpty()) {
				page.setSkip(true);
				return;
			}
			if (url.endsWith(".json")) {

				List<PageVideoInfo> hotvideolist = new ArrayList<PageVideoInfo>();
				page.putField("hotvideolist", hotvideolist);
				String sarea = url.substring(url.lastIndexOf('/') + 1,
						url.lastIndexOf('-'));
				List<PageVideoInfo> infos = null;
				if (url.endsWith("3day.json")) {
					// 3天热门
					infos = extractHotVedioInfosFromJson(page, "$.hot.list",
							PageVideoInfo.TYPE_PAGERIGHTHOTALL3, sarea);

					hotvideolist.addAll(infos);

					infos = extractHotVedioInfosFromJson(page,
							"$.hot_original.list",
							PageVideoInfo.TYPE_PAGERIGHTHOTORIGIN3, sarea);

					hotvideolist.addAll(infos);

				} else if (url.endsWith("week.json")) {
					// 一周热门
					infos = extractHotVedioInfosFromJson(page, "$.hot.list",
							PageVideoInfo.TYPE_PAGERIGHTHOTALL7, sarea);

					hotvideolist.addAll(infos);

					infos = extractHotVedioInfosFromJson(page,
							"$.hot_original.list",
							PageVideoInfo.TYPE_PAGERIGHTHOTORIGIN7, sarea);

					hotvideolist.addAll(infos);
				}

			} else {
				List<Selectable> nodes = page.getHtml()
						.xpath("//div[@class='container-row']").nodes();
				//List<PageVideoInfo> videolist = new ArrayList<PageVideoInfo>();
				//page.putField("videolist", videolist);

				for (Selectable sel : nodes) {
					String sarea = sel.xpath("div/@area").toString();
					if ("".equals(sarea.trim())) {
						continue;
					}
					sarea = sarea.trim();
					Selectable ssel = sel
							.xpath("div[@class='b-l']//span[@class='b-head-t']/a/text()");
					// 多了一个全区动态的位置
					if (ssel.toString() == null) {
						continue;
					}
					String name = ssel.toString().trim();
					areaMap.putIfAbsent(sarea, name);
					// 处理頁面上的视频信息
					// 现在B站上面的分类动态，使用了一种类似轮询的方式来处理，实时性较高，每次刷新页面的结果都不尽相同,参考性不大
					/*List<Selectable> details = sel.xpath(
							"div[@class='b-l']/div[@class='b-body']/ul/li/div")
							.nodes();

					PageVideoInfo video = null;
					for (Selectable node : details) {
						try {

							video = extractPageVedioInfoFromDiv(node);
						} catch (RuntimeException e) {
							logger.error("extract div info error!", e);
						}
						if (video != null) {
							video.setAreaname(name);
							video.setBilicode(sarea);
							videolist.add(video);
						}
					}*/
					// 增加动态抓取的数据url
					page.addTargetRequest("http://www.bilibili.com/index/catalogy/"
							+ sarea + "-3day.json");
					page.addTargetRequest("http://www.bilibili.com/index/catalogy/"
							+ sarea + "-week.json");
				}
			}
		}

	}

	/*
	private PageVideoInfo extractPageVedioInfoFromDiv(Selectable node) {

		if (logger.isDebugEnabled()) {
			logger.debug("pageleft: " + node.toString());
		}
		PageVideoInfo video = new PageVideoInfo();
		String avcode = node.xpath("div/a/@href").toString();
		avcode = avcode.substring("http://www.bilibili.com/video/".length(),
				avcode.length() - 1);
		video.setAvcode(avcode);

		video.setCoin(BiliUtils.parseInteger(node.xpath("div/@yb").toString()));
		video.setDescription(node.xpath("div/@txt").toString());
		video.setDm(BiliUtils.parseInteger(node.xpath("div/@dm").toString()));
		video.setGk(BiliUtils.parseInteger(node.xpath("div/@gk").toString()));
		video.setLm(node.xpath("div/@lm").toString());
		video.setOpertime(new Date());
		video.setPl(BiliUtils.parseInteger(node.xpath("div/@pl").toString()));
		video.setSc(BiliUtils.parseInteger(node.xpath("div/@sc").toString()));
		video.setSubtitle(node.xpath("div/@subtitle").toString());
		String tg = node.xpath("div/@tg").toString();
		video.setTgtime(BiliUtils.translateDate(tg));
		video.setDuration(node.xpath(
				"div//div[@class='x']/b[@class='x2']/text()").toString());
		video.setTitle(node.xpath("div/a/div[@class='t']/text()").toString());
		video.setImg(node.xpath("div/a/div[@class='preview']/img/@src")
				.toString());
		video.setType(PageVideoInfo.TYPE_PAGELEFT);
		video.setUp(node.xpath("div/@up").toString());
		return video;
	}
*/
	
	private List<PageVideoInfo> extractHotVedioInfosFromJson(Page page,
			String jsonpath, String type, String area) {
		List<PageVideoInfo> list = new ArrayList<PageVideoInfo>();
		try {
			String[] parts = jsonpath.split("\\.");
			int plen = parts.length;

			Json json = page.getJson();
			JSONObject jobj = JSON.parseObject(json.toString());
			for (int i = 1; i < plen; i++) {
				Object obj = jobj.get(parts[i]);
				if (obj == null) {
					return list;
				} else if (obj instanceof JSONArray) {
					break;
				} else if (obj instanceof JSONObject) {
					jobj = (JSONObject) obj;
				}
			}
			List<String> jsons = json.jsonPath(jsonpath).all();

			PageVideoInfo video = null;
			int len = jsons.size();
			for (int i = 0; i < len; i++) {
				video = extractHotVedioInfoFromJson(jsons.get(i), type, area);
				video.setRank(i + 1);
				list.add(video);
			}
		} catch (RuntimeException e) {
			logger.warn("json error!", e);
		}
		return list;
	}

	private PageVideoInfo extractHotVedioInfoFromJson(String json, String type,
			String area) {

		logger.debug("pageright: " + json);
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
		video.setAreaname(areaMap.get(area));
		video.setBilicode(area);
		return video;
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {

		Spider.create(new CategoryPageProcessor())
				.addUrl("http://www.bilibili.com/index.html")
				.addPipeline(new CategoryPagePipeline()).thread(1).run();
	}
}
