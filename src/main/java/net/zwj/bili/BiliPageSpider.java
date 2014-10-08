/**
 * 
 */
package net.zwj.bili;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Spider;

/**
 * bilibili新版页面主要页面上的视频信息视频抓取
 * 
 */
public class BiliPageSpider {

	private static final Logger logger = LoggerFactory
			.getLogger(BiliPageSpider.class);

	public static void main(String[] args) {
		ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(5);
		executor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					
					dealSpider();
				} catch (RuntimeException e) {
					logger.error("execute error!", e);
				}

			}
		}, 0,60, TimeUnit.MINUTES);
	}

	private static void dealSpider() {
		long time = System.currentTimeMillis();
		// 首页处理
		logger.info("begin index page ");
		Spider.create(new IndexPageProcessor())
				.addUrl("http://www.bilibili.com/index.html")
				.addPipeline(new IndexPagePipeline()).thread(1).run();
		logger.info("end index page time ["
				+ (System.currentTimeMillis() - time) + "ms]");

		// 普通页面上的处理
		time = System.currentTimeMillis();
		logger.info("begin category page ");
		Spider.create(new CategoryPageProcessor())
				.addUrl("http://www.bilibili.com/index.html")
				.addPipeline(new CategoryPagePipeline()).thread(1).run();
		logger.info("end category page [" + (System.currentTimeMillis() - time)
				+ "ms]");

		// 排行榜页面处理
		time = System.currentTimeMillis();
		logger.info("begin ranking pages ");
		Spider.create(new RankPageProcessor())
		.addUrl("http://www.bilibili.com/ranking")
		.addPipeline(new RankPagePipeline()).thread(1).run();
		logger.info("end ranking page [" + (System.currentTimeMillis() - time)
				+ "ms]");
	}

}
