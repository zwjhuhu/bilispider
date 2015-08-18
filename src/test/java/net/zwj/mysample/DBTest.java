package net.zwj.mysample;

import java.util.List;

import net.zwj.bili.db.DbUtils;
import net.zwj.bili.model.PageVideoInfo;

import org.junit.Test;

public class DBTest {
	@Test
	public void insertTest() {
		PageVideoInfo pageTop = new PageVideoInfo();
		pageTop.setCoin(11);
		pageTop.setTitle("tes");
		pageTop.setDescription("谁发的");
		DbUtils.insert(pageTop);
	}

	@Test
	public void findByIdTest() {

		PageVideoInfo model = DbUtils.findById(4L, PageVideoInfo.class);
		System.out.println(model);
	}

	@Test
	public void findByQueryTest() {
		List<PageVideoInfo> list = DbUtils
				.findBYSql("select * from normalPageTop where id > 1",
						PageVideoInfo.class);
		System.out.println(list.size());
	}

	@Test
	public void deleteByIdTest() {
		DbUtils.deleteById(1L, PageVideoInfo.class);
	}
	
	@Test
	public void updateByIdTest() {
		PageVideoInfo pageTop = new PageVideoInfo();
		pageTop.setId(2L);
		pageTop.setCoin(0);
		pageTop.setTitle("asdasdads");
		pageTop.setDescription(null);
		DbUtils.updateById(pageTop);
	}
}
