package me.jensvh.spotifree.ytmusic;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.jensvh.spotifree.api.ytmusic.Filter;
import me.jensvh.spotifree.api.ytmusic.Song;
import me.jensvh.spotifree.api.ytmusic.Video;

public class YtDeserializer {
	
	public static List<Video> deserialize(JsonObject tree, Filter filter) {
		List<Video> list = new ArrayList<Video>();
		JsonArray songs = tree.getAsJsonObject("contents")
				.getAsJsonObject("sectionListRenderer")
				.getAsJsonArray("contents").get(0).getAsJsonObject()
				.getAsJsonObject("musicShelfRenderer")
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
	}

}
