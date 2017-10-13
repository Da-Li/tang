package com.migudm.tang.code_generate.utils;

/**
 * Created by lixiaoshenxian on 2017/10/12.
 */
public class StringUtils {
	public static String camelName(String str,boolean isStartUpper){
		if (org.springframework.util.StringUtils.isEmpty(str)){
			return "";
		}

		str = str.toLowerCase();

		String[] strArray = str.toLowerCase().split("_");
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=0;i<strArray.length;i++){
			String currentStr = strArray[i];
			stringBuffer.append(currentStr.substring(0,1).toUpperCase());
			if (currentStr.length()>1){

				stringBuffer.append(currentStr.substring(1,currentStr.length()));
			}
		}
		//大驼峰 还是小驼峰
		if (!isStartUpper){
			stringBuffer.replace(0,1,stringBuffer.substring(0,1).toLowerCase());
		}
		return stringBuffer.toString();
	}

	public static void main(String[] args) {
		System.out.println(camelName("TD",true));
	}
}
