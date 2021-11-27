package me.jensvh.spotifree.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import me.jensvh.spotifree.api.spotify.Album;
import me.jensvh.spotifree.api.spotify.LyricsLine;
import me.jensvh.spotifree.api.spotify.Playlist;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.mp3agic.Mp3agic;

public class Utils {
	
	public static int to_ms(String time) {
		String[] splitted = time.split(":");
		return ((Integer.parseInt(splitted[0]) * 60) + (Integer.parseInt(splitted[1]))) * 1000; 
	}
	
	public static String simplify(String str) {
		try {
			return str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
		} catch (NullPointerException e) {}
		return "";
	}
	
	public static String removeInBrackets(String str) {
		return str.replaceAll("\\(.*\\)", "").replaceAll("\\[.*\\]", "").trim();
	}
	
	public static String removeAfterDash(String str) {
		return str.replaceAll(" \\- .*", "");
	}
	
	public static String removeInvalidPathChars(String str) {
		return str.replaceAll("[\\\\/:*?\"<>|]", "");
	}
	
	public static String removeAsciiColors(String str) {
	    return str.replaceAll("\u001B\\[[;\\d]*m", "");
	}
	
	public static String joinLyricsLines(LyricsLine[] lines) {
	    StringBuilder builder = new StringBuilder();
	    builder.append(lines[0].getWords());
	    
	    for (int i = 1; i < lines.length; i++) {
	        builder.append("\n");
	        builder.append(lines[i].getWords());
	    }
	    
	    return builder.toString();
	}
	
	public static File createFolder(String name) {
		File folder = new File(name);
		folder.mkdirs();
		return folder;
	}


	public static byte[] loadDateFromUrl(String url) throws IOException {
	    URL url2 = new URL(url);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    InputStream is = url2.openStream();
	    byte[] byteChunk = new byte[2500];
	    int n;

	    while ((n = is.read(byteChunk)) > 0) {
	        baos.write(byteChunk, 0, n);
	    }

	    is.close();
	    baos.flush();
	    baos.close();

	    return baos.toByteArray();
	}
	
	public static Map<String, File> getMp3sInFolder(final File dir) {
		Map<String, File> files = new HashMap<String, File>();
		
		for (final File file : dir.listFiles()) {
			if (!FilenameUtils.isExtension(file.getName(), "mp3")) continue;
			String key = Mp3agic.getKey(file);
			files.put(key, file);
		}
		
		return files;
	}
	
	public static List<String> removeNoLongerExistingMp3s(File folder, Playlist playlist) {
		Map<String, File> mp3s = getMp3sInFolder(folder);
		List<String> existings = new ArrayList<String>();
		
		// Remove the still existing mp3s from the map
		for (Track track : playlist.getTracks()) {
			if (mp3s.containsKey(track.getId())) {
				existings.add(track.getId());
				mp3s.remove(track.getId());
			}
		}
		
		// Delete others
		for (File file : mp3s.values()) {
			file.delete();
		}
		
		return existings;
	}
	
	public static List<String> removeNoLongerExistingMp3s(File folder, Album album) {
		Map<String, File> mp3s = getMp3sInFolder(folder);
		List<String> existings = new ArrayList<String>();
		
		// Remove the still existing mp3s from the map
		for (Track track : album.getTracks()) {
			existings.add(track.getId());
			mp3s.remove(track.getId());
		}
		
		// Delete others
		for (File file : mp3s.values()) {
			file.delete();
		}
		
		return existings;
	}
	
	public static String commaSepperatedString(Track[] tracks) {
		String str = "";
		for (Track track : tracks) {
			if (str.isEmpty() || str == "") {
				str = track.getId();
				continue;
			}
			str = String.join(",", str, track.getId());
		}
		return str;
	}
	
	public static Track[] pick5RandomTracks(List<Track> tracks) {
		Track[] array = new Track[5];
		Random rnd = new Random();
		
		List<Integer> ints = new ArrayList<Integer>();
		int i = 0;
		while (i < 5 && i < tracks.size()) {
			int random = rnd.nextInt(tracks.size());
			if (!ints.contains(random)) {
				ints.add(random);
				array[i] = tracks.get(i);
				i++;
			}
		}
		return array;
	}
	
}
