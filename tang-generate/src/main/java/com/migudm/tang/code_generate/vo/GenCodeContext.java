package com.migudm.tang.code_generate.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lixiaoshenxian on 2017/10/12.
 */
public class GenCodeContext {

	 static  ThreadLocal<Map> context = new ThreadLocal<>();

	public static void redo(){
		Map m = context.get();
		if (m != null){
			m.clear();
		}
	}
	public static Map getContext() {
		Map map = context.get();
		if (map == null) {
			setContext(new HashMap());
		}
		return context.get();
	}


	public static void put(String key,Object oValue){
		getContext().put(key,oValue);
	}

	public static Object get(String key){
		return getContext().get(key);
	}


	private static void setContext(Map map){
		context.set(map);
	}
}
