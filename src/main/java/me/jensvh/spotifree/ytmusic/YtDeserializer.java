package me.jensvh.spotifree.ytmusic;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.jensvh.spotifree.api.ytmusic.Filter;
import me.jensvh.spotifree.api.ytmusic.Song;
import me.jensvh.spotifree.api.ytmusic.Video;
import me.jensvh.spotifree.utils.Console;
import me.jensvh.spotifree.utils.Error;

public class YtDeserializer {
	
	public static List<Video> deserialize(JsonObject tree, Filter filter) {
		try {
			List<Video> list = new ArrayList<Video>();
			JsonArray contents = tree.getAsJsonObject("contents")
					.getAsJsonObject("sectionListRenderer")
					.getAsJsonArray("contents");
			JsonArray songs = get("musicShelfRenderer", contents)
					.getAsJsonArray("contents");
			for (int i = 0; i < songs.size(); i++) {
				JsonObject object = songs.get(i).getAsJsonObject()
						.getAsJsonObject("musicResponsiveListItemRenderer");
				Video song = new Video();
				if (filter.equals(Filter.SONGS)) {
					song = new Song();
					((Song)song).setAlbum(object.getAsJsonArray("flexColumns").get(2).getAsJsonObject()
							.getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
							.getAsJsonObject("text")
							.getAsJsonArray("runs").get(0).getAsJsonObject()
							.get("text").getAsString());
				}
				song.setVideo_id(object.getAsJsonObject("doubleTapCommand")
						.getAsJsonObject("watchEndpoint")
						.get("videoId").getAsString());
				song.setTitle(object.getAsJsonArray("flexColumns").get(0).getAsJsonObject()
						.getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
						.getAsJsonObject("text")
						.getAsJsonArray("runs").get(0).getAsJsonObject()
						.get("text").getAsString());
				song.setDuration(object.getAsJsonArray("flexColumns").get(3).getAsJsonObject()
						.getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
						.getAsJsonObject("text")
						.getAsJsonArray("runs").get(0).getAsJsonObject()
						.get("text").getAsString());
				JsonArray artist_array = object.getAsJsonArray("flexColumns").get(1).getAsJsonObject()
						.getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
						.getAsJsonObject("text")
						.getAsJsonArray("runs");
				String[] artists = new String[(artist_array.size() + 1) / 2];
				int k = 0;
				for (int j = 0; j < artist_array.size(); j += 2) {
					artists[k] = artist_array.get(j).getAsJsonObject()
									.get("text").getAsString();
					k++;
				}
				song.setArtists(artists);
				list.add(song);
			}
			return list;
		} catch (NullPointerException ex) {
			System.out.println(tree);
			Console.errPrint(tree.toString());
			Console.errPrint("");
			ex.printStackTrace();
			System.exit(-3);
		}
		return null;
	}
	
	private static JsonObject get(String name, JsonArray array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i).getAsJsonObject().has(name)) {
				return array.get(i).getAsJsonObject().getAsJsonObject(name);
			}
		}
		throw new Error(16, new NullPointerException(), "Youtube music sended us the wrong page");
	}

}
