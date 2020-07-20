package me.jensvh.spotifree.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	private static boolean exists(String name) {
		return new File(name).exists();
	}
	
	public static void unpack(String name) {
		if (!exists(name)) {
			InputStream srcStream = FileUtils.class.getClassLoader().getResourceAsStream("resources/" + name);
			if (srcStream == null) { throw new Error(10, new FileNotFoundException(), "File does not exists");}
			
			File dest = new File(name);
			try {
				org.apache.commons.io.FileUtils.copyInputStreamToFile(srcStream, dest);
			} catch (IOException e) {
				throw new Error(5, e, "Probably some file that no longer exists.");
			}
		}
	}
	
}
