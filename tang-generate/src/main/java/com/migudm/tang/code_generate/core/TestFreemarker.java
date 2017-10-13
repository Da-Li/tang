package com.migudm.tang.code_generate.core;

import com.migudm.tang.code_generate.vo.GenCodeContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.*;

/**
 * Created by lixiaoshenxian on 2017/10/12.
 */
public class TestFreemarker {

	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		try {
			File templateRootDir = new File("/Users/lixiaoshenxian/workspace/ciyuan/cy_admin_new/ciyuan/Server/Code/migu_ciyuan_operate_new/code/ciyuan-operate/cyoperate-support/tang-generate/src/main/resources/tmpl/").getAbsoluteFile();
			configuration.setDirectoryForTemplateLoading(templateRootDir);
			configuration.setNumberFormat("##############");
			configuration.setBooleanFormat("false,true");
			List<File> allTemplate = new ArrayList<>();
			listFiles(templateRootDir,allTemplate);
			for (int i = 0;i<allTemplate.size();i++){
				File c = allTemplate.get(i);
				if (c.isDirectory() || c.isHidden()){
					continue;
				}
				String templateName = getTemplateName(templateRootDir,c);
				Template template = configuration.getTemplate(templateName);
				Map dataMap = new HashMap<>();
				dataMap.put("basepackage", "basepackage");
				dataMap.put("module", "module");
				dataMap.put("className", "className");
				File file = new File("/Users/lixiaoshenxian/workspace/ciyuan/test_gencode/"+templateName);
				file.getParentFile().mkdir();
				FileWriter fileWriter = new FileWriter(file);
				template.process(dataMap,fileWriter);
//				configuration.
				fileWriter.flush();
				fileWriter.close();
				if (c.isHidden())
				System.out.println(allTemplate.get(i));
			}
//
//			Template template = configuration.getTemplate("mybatis.ftl");
//			FileWriter o = new FileWriter("/Users/lixiaoshenxian/workspace/ciyuan/test_gencode");
//			Map<String, Object> dataMap;
//			Writer out;
//			try (FileOutputStream fos = new FileOutputStream(o)) {
//				dataMap = new HashMap<>();
//				dataMap.put("basepackage", "basepackage");
//				dataMap.put("module", "module");
//				dataMap.put("className", "className");
//				out = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"), 10240);
//			}
//			template.process(dataMap,out);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

	public static void listFiles(File file,List collector) throws IOException {
		collector.add(file);
		if((!file.isHidden() && file.isDirectory()) ) {
			File[] subFiles = file.listFiles();
			for(int i = 0; i < subFiles.length; i++) {
				listFiles(subFiles[i],collector);
			}
		}
	}

	public static String getTemplateName(File baseDir, File file) {
		if(baseDir.equals(file))
			return "";
		if(baseDir.getParentFile() == null)
			return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
		return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length()+1);
	}
}
