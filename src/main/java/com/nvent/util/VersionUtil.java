package com.nvent.util;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VersionUtil {

	public static void writeToFile(String content, String filePath) {
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Date now = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
		String version = df.format(now);
		writeToFile("nvent.qops.version=" + version, "./src/main/resources/version.properties");
	}

}
