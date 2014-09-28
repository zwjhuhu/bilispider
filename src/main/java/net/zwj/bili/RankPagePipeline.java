package net.zwj.bili;

import java.util.List;
import java.util.Map;

import net.zwj.bili.db.DbUtils;
import net.zwj.bili.model.RankingVideoInfo;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

public class RankPagePipeline extends BiliDBPipeline {

	@Override
	protected void doPageDataDeal(ResultItems resultItems, Task task) {
		String type = resultItems.get("type").toString();
		if (type == null || type.trim().isEmpty()) {
			return;
		}
		Object obj = resultItems.get("rankvideos");
		if (obj != null) {
			@SuppressWarnings("unchecked")
			Map<String, List<RankingVideoInfo>> rankvideos = (Map<String, List<RankingVideoInfo>>) obj;
			List<RankingVideoInfo> list;
			for(String category:rankvideos.keySet()){
				list = rankvideos.get(category);
				if (isRankingChanged(list, category,type)) {
					for (RankingVideoInfo info : list) {
						DbUtils.insert(info);
					}
				}
			}
		}

	}

	private boolean isRankingChanged(List<RankingVideoInfo> list, String category,String type) {
		boolean changed = false;
		List<RankingVideoInfo> dbList = null;
		try {
			dbList = DbUtils.findBYSql(
					"select * from rankingvideoinfo where type='" + type
							+ "' and category = '"+category+"' "
									+ "and opertime = (select max(opertime) from rankingvideoinfo) "
									+ "order by rank",
					RankingVideoInfo.class);
			
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

}
