package net.zwj.bili;

import java.util.HashMap;
import java.util.Map;

public final class Constant {
	
	private Constant(){
		
	}
	
	public static final Map<String,String> BILI_CATEGORY_MAP = new HashMap<String,String>();
	
	//bilibili的分类首页中分类地址和对应页面
	static{
		BILI_CATEGORY_MAP.put("", "");
		
	}
	
}
