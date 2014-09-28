package net.zwj.bili;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zwj.bili.model.RankingVideoInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 排行表页面处理
 * 
 */
public class RankPageProcessor implements PageProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(RankPageProcessor.class);

	private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
			.setTimeOut(15 * 1000);

	@Override
	public void process(Page page) {

		String url = page.getUrl().toString();
		String type = null;
		if (url.equals("http://www.bilibili.com/ranking-day")) {
			type = RankingVideoInfo.TYPE_DAY;
		} else if (url.equals("http://www.bilibili.com/ranking-week")) {
			type = RankingVideoInfo.TYPE_WEEK;
		} else if (url.equals("http://www.bilibili.com/ranking")) {
			type = RankingVideoInfo.TYPE_MONTH;
		} else {
			logger.error("ranking page: " + url + "not config!");
		}
		if (type != null) {
			Map<String, List<RankingVideoInfo>> rankvideos = new HashMap<String, List<RankingVideoInfo>>();
			rankvideos = extractRankingVedioInfosFromDiv(
					page.getHtml().xpath("//div[@class='ranking_list']"), type);
			page.putField("type", type);
			page.putField("rankvideos", rankvideos);
		}

	}

	private RankingVideoInfo extractRankingVedioInfo(Selectable contnode) {
		RankingVideoInfo video = new RankingVideoInfo();
		String avcode = contnode.xpath("div/a/@href").toString();
		if (avcode.endsWith("/")) {
			avcode = avcode.substring(0, avcode.length() - 1);
		}
		avcode = avcode.substring(avcode.lastIndexOf('/') + 1);
		video.setAvcode(avcode);
		Integer pts = 0;
		String spts = contnode.xpath("div/div[@class='pts']/text()").toString();
		spts = spts.replaceAll("\\s+", "").replace("pts", "");
		try {
			pts = Integer.parseInt(spts);
		} catch (RuntimeException e) {
			logger.warn("pts parse error!", e);
		}
		video.setPts(pts);

		video.setImg(contnode.xpath("div/a/div[@class='preview']/img/@src")
				.toString());
		video.setTitle(contnode.xpath("div/a/div[@class='title']/@title")
				.toString());
		return video;
	}

	private Map<String, List<RankingVideoInfo>> extractRankingVedioInfosFromDiv(
			Selectable node, String type) {

		if (logger.isDebugEnabled()) {
			logger.debug("rankingpage: " + node.toString());
		}
		List<Selectable> heads = node.xpath(
				"div/div[@class='r_header']/div[@class='t']").nodes();
		List<Selectable> videols = node.xpath(
				"div/ul[@class='list']/li[@class='l']").nodes();

		Map<String, List<RankingVideoInfo>> rankvideos = new HashMap<String, List<RankingVideoInfo>>();
		if (!heads.isEmpty()) {
			List<RankingVideoInfo> ranklist = null;
			int size = heads.size();
			int index = 0;
			String category = "";

			RankingVideoInfo info;
			Date opertime = new Date();
			Selectable li = null;
			List<Selectable> vnodes = null;
			int vlen = 0;
			while (index < size) {
				li = videols.get(index);
				category = heads.get(index).xpath("div/text()").toString()
						.trim();
				vnodes = li.xpath("li//div[@class='rankv']").nodes();
				vlen = vnodes.size();
				ranklist = new ArrayList<RankingVideoInfo>(vlen);

				for (int i = 0; i < vlen; i++) {
					info = extractRankingVedioInfo(vnodes.get(i));
					info.setCategory(category);
					info.setRank(i + 1);
					info.setOpertime(opertime);
					info.setType(type);
					ranklist.add(info);
				}
				rankvideos.put(category, ranklist);
				index++;
			}
		}

		return rankvideos;

	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {

		Spider.create(new RankPageProcessor())
				.addUrl("http://www.bilibili.com/ranking-day")
				.addUrl("http://www.bilibili.com/ranking-week")
				.addUrl("http://www.bilibili.com/ranking")
				.addPipeline(new RankPagePipeline()).thread(1).run();
	}
}
