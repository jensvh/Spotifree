package me.jensvh.spotifree.yttomp3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import me.jensvh.spotifree.utils.Error;
import me.jensvh.spotifree.utils.Utils;

/**
 * Huge thanks to Cardinal - Reinstate Monica
 * https://stackoverflow.com/a/60854321
 * @author Cardinal - Reinstate Monica
 *
 */
public class YoutubeToMp3 {

	private static final Pattern VID_ID_PATTERN = Pattern.compile("(?<=v\\=|youtu\\.be\\/)\\w+"),
	        MP3_URL_PATTERN = Pattern
	                .compile("(?<=href=\\\")https{0,1}:\\/\\/(\\w|\\d){3}\\.ytapivmp3\\.com\\/.+(?=\\\" )");
	// href="https://s01.ytapivmp3.com/download/mrukuuMUu_s/mp3/320/1593857742/5a6e67f7fe27736a1b481cf117e80d540ea567ea00e3f0e1dd398f4c08a0daeb"
	public static File download(String id, String folder) {
		try {
			byte[] data = youtubeToMP3(id);
			
			File file = new File((folder != null ? folder + File.separator : "") + id + ".mp3");
			FileOutputStream output = new FileOutputStream(file);
			IOUtils.write(data, output);
			output.close();
			return file;
		} catch (IOException e) {
			throw new Error(7, e, "This could be either be a network issue or a file issue.");
		}
	}
	
	public static byte[] youtubeToMP3(String id) throws IOException {
	    //String id = getID(youtubeUrl);
	    String converter = loadConverter(id);
	    String mp3url = getMP3URL(converter);
	    byte[] mp3 = Utils.loadDateFromUrl(mp3url);
	    return mp3;
	}

	private static String getMP3URL(String html) {
	    Matcher m = MP3_URL_PATTERN.matcher(html);
	    m.find();
	    try {
	    	return m.group();
	    } catch(IllegalStateException ex) {
	    	throw new Error(8, ex, "Youtube320 is probably unavailiable, retry in a few minutes.");
	    }
	}

	private static String loadConverter(String id) throws IOException {
	    String url = "https://www.320youtube.com/watch?v=" + id;
	    byte[] bytes = Utils.loadDateFromUrl(url);
	    return new String(bytes);
	}

	@SuppressWarnings("unused")
	private static String getID(String youtubeUrl) {
	    Matcher m = VID_ID_PATTERN.matcher(youtubeUrl);
	    if (!m.find()) {
	        throw new Error(9, new IllegalArgumentException(), "Invalid YouTube URL.");
	    }
	    return m.group();
	}
	
}
