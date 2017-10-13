package com.migudm.tang.code_generate.core;

import com.migudm.tang.code_generate.vo.GenCodeContext;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by lixiaoshenxian on 2017/10/12.
 * 文件生成
 */
public class GenerateFile {


	public static void genFile()  {
		try {
			Configuration configuration = new Configuration();
			URL url = ClassLoader. getSystemClassLoader().getResource("tmpl");
			File templateRootDir = new File(url.getPath()).getAbsoluteFile();
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
				Map params = GenCodeContext.getContext();
				String outPath = templateName;
				for(Iterator it = params.entrySet().iterator(); it.hasNext(); ) {
					Map.Entry entry = (Map.Entry)it.next();
					String key = (String)entry.getKey();
					Object value = entry.getValue();
					String strValue = value == null ? "" : value.toString();
					outPath = replace(outPath, "${"+key+"}", strValue);
				}

				File file = new File(GenCodeContext.get("locationPath").toString()+File.separator+outPath);
				if (file.exists()){
					System.out.println("");
				}
				System.out.println(file.getAbsolutePath());
				if (!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				try (FileWriter fileWriter = new FileWriter(file)) {
					template.setAutoFlush(true);
					template.process(GenCodeContext.getContext(), fileWriter);
					fileWriter.flush();
					fileWriter.close();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}



	 private static String replace(String inString, String oldPattern, String newPattern) {
		if (inString == null) {
			return null;
		}
		if (oldPattern == null || newPattern == null) {
			return inString;
		}

		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		// remember to append any characters to the right of a match
		return sbuf.toString();
	}

	private static void listFiles(File file,List collector) throws IOException {
		collector.add(file);
		if((!file.isHidden() && file.isDirectory()) ) {
			File[] subFiles = file.listFiles();
			for(int i = 0; i < subFiles.length; i++) {
				listFiles(subFiles[i],collector);
			}
		}
	}

	private static String getTemplateName(File baseDir, File file) {
		if(baseDir.equals(file))
			return "";
		if(baseDir.getParentFile() == null)
			return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
		return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length()+1);
	}
}
