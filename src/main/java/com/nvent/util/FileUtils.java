package com.nvent.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
	public static final Logger log = LoggerFactory.getLogger(FileUtils.class);
			
	public static void transFile(String filePath, OutputStream out) {
		File file = new File(filePath);
		if (file.exists()) {
			try (FileInputStream fis = new FileInputStream(file);) {
				byte[] buf = new byte[8192];
				int length;
				while ((length = fis.read(buf, 0, 8192)) > 0) {
					out.write(buf, 0, length);
				}
			} catch (Exception ex) {
				log.error("", ex);
			}
		}
	}
	
	public static void transFile(InputStream in, String filePath) {
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			byte[] buf = new byte[8192];
			int length;
			while ((length = in.read(buf, 0, 8192)) > 0) {
				fos.write(buf, 0, length);
			}
			fos.close();
		} catch(Exception ex) {
			log.error("", ex);
		}
	}
	
	public static void transContent(InputStream in, OutputStream out) {
		try {
			byte[] buf = new byte[8192];
			int length;
			while ((length = in.read(buf, 0, 8192)) > 0) {
				out.write(buf, 0, length);
			}
		} catch (Exception ex) {
			log.error("", ex);
		}
	}
	
	public static void copyFile(String src, String dest) {
		log.info("Begin to copy [{}] to [{}]", src, dest);
		try (FileInputStream fis = new FileInputStream(src);
				FileOutputStream fos = new FileOutputStream(dest);) {
			byte[] buf = new byte[8192];
			int length;
			while ((length = fis.read(buf, 0, 8192)) > 0) {
				fos.write(buf, 0, length);
			}
			fis.close();
			fos.close();
		} catch(Exception ex) {
			log.error("", ex);
		}
	}
	
	public static void copyFile(File src, File dest) {
		log.info("Begin to copy [{}] to [{}]", src, dest);
		try (FileInputStream fis = new FileInputStream(src);
				FileOutputStream fos = new FileOutputStream(dest);) {
			byte[] buf = new byte[8192];
			int length;
			while ((length = fis.read(buf, 0, 8192)) > 0) {
				fos.write(buf, 0, length);
			}
			fis.close();
			fos.close();
		} catch(Exception ex) {
			log.error("", ex);
		}
	}

	public static void deleteDir(String dir) {
		File f = new File(dir);
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for(File file : fs) {
				deleteDir(file);
			}
		}
		f.delete();
	}
	public static void deleteDir(File f) {
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for(File file : fs) {
				deleteDir(file);
			}
		}
		f.delete();
	}
	
	public static void zipFile(String srcFile, String zipFile) {
		try {
			CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
			ZipOutputStream zos = new ZipOutputStream(cos);
		    
			byte[] buffer=new byte[8192];
			int readLength = 0;
			
			File file = new File(srcFile);
			ZipEntry zipEntry = new ZipEntry(file.getName());
            zipEntry.setSize(file.length());
            zipEntry.setTime(file.lastModified());
            zos.putNextEntry(zipEntry);

            InputStream is = new BufferedInputStream(new FileInputStream(srcFile));

            while ((readLength = is.read(buffer,0,8192)) != -1){
                zos.write(buffer, 0, readLength);
            }
            is.close();
            
            zos.close();
		} catch (Exception e) {
			log.error("zipFile error", e);
		}
	}
	
	public static String readContentFromFile(String file) {
		try(FileInputStream fis = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fis, "UTF-8");) {
			char[] buf = new char[8192];
			int length = 0;
			StringBuilder content = new StringBuilder();
			while((length = reader.read(buf, 0, 8192)) > 0) {
				content.append(buf, 0, length);
			}
			return content.toString();
		}
		catch(Exception ex) {
			log.error("readContentFromFile error", ex);
		}
		return null;
	}
	
	public static void writeContentToFile(String content, String file) {
		try(FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");) {
			writer.write(content);
			writer.flush();
		}
		catch(Exception ex) {
			log.error("writeContentToFile error", ex);
		}
	}
}
