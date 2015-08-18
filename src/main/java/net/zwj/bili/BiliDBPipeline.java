package net.zwj.bili;

import java.util.Date;

import net.zwj.bili.db.DbUtils;
import net.zwj.bili.model.SpiderLog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public abstract class BiliDBPipeline implements Pipeline {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void process(ResultItems resultItems, Task task) {
		if (resultItems == null) {
			return;
		}
		createLog(resultItems.getRequest().getUrl());
		doPageDataDeal(resultItems, task);
	}

	protected abstract void doPageDataDeal(ResultItems resultItems, Task task);

	private void createLog(String url) {
		SpiderLog log = new SpiderLog();
		log.setUrl(url);
		log.setOpertime(new Date());
		DbUtils.insert(log);
	}
}
