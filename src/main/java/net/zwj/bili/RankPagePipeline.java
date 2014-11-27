package net.zwj.bili;

import java.util.List;

import net.zwj.bili.db.DbUtils;
import net.zwj.bili.model.RankingVideoInfo;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

public class RankPagePipeline extends BiliDBPipeline {

	@Override
	protected void doPageDataDeal(ResultItems resultItems, Task task) {
		String kind = resultItems.get("kind");
		if (kind == null) {
			kind = "";
		}
		String type = resultItems.get("type");
		if (type == null) {
			type = "";
		}
		String category = resultItems.get("category");
		if (category == null) {
			category = "";
		}

		Object obj = resultItems.get("rankvideos");
		if (obj != null) {
			@SuppressWarnings("unchecked")
			List<RankingVideoInfo> list = (List<RankingVideoInfo>) obj;
			DbUtils.insertBatch(list);
		}

	}

	//这里如果使用了全表扫描，数据量很大的时候，将会慢的不能直视
/*	private boolean isRankingChanged(List<RankingVideoInfo> list, String kind,
			String type, String category) {
		boolean changed = false;
		List<RankingVideoInfo> dbList = null;
		try {
			dbList = DbUtils
					.findBYSql(
							"select * from rankingvideoinfo where kind='"
									+ kind
									+ "' and type='"
									+ type
									+ "' and category = '"
									+ category
									+ "' "
									+ "and opertime = (select max(opertime) from rankingvideoinfo) "
									+ "order by rank", RankingVideoInfo.class);

		} catch (RuntimeException e) {
			logger.error("load ranking data from db error!", e);
		}
		changed = dbList.isEmpty() || dbList.size() != list.size();
		if (!changed) {
			int len = dbList.size();
			int count = 0;
			for (int i = 0; i < len; i++) {
				count = 0;
				for (int j = 0; j < len; j++) {
					if (list.get(i).equals(dbList.get(j))) {
						count++;
						break;
					}
				}
				if (count == 0) {
					changed = true;
					break;
				}
			}
		}
		return changed;
	}
*/
}
