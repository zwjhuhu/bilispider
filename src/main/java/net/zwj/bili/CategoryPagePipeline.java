package net.zwj.bili;

import java.util.List;

import net.zwj.bili.db.DbUtils;
import net.zwj.bili.model.PageVideoInfo;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

public class CategoryPagePipeline extends BiliDBPipeline {

	@Override
	protected void doPageDataDeal(ResultItems resultItems, Task task) {
		Object obj = resultItems.get("videolist");
		if (obj != null) {
			@SuppressWarnings("unchecked")
			List<PageVideoInfo> list = (List<PageVideoInfo>) obj;
			for (PageVideoInfo info : list) {
				DbUtils.insert(info);
			}
		}

		obj = resultItems.get("hotvideolist");
		if (obj != null) {
			@SuppressWarnings("unchecked")
			List<PageVideoInfo> list = (List<PageVideoInfo>) obj;
			for (PageVideoInfo info : list) {
				DbUtils.insert(info);
			}
		}

	}

}
