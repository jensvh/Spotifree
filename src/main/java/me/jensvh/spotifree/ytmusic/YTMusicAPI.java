package me.jensvh.spotifree.ytmusic;

import java.util.ArrayList;
import java.util.List;

import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.api.ytmusic.Filter;
import me.jensvh.spotifree.api.ytmusic.Video;
import me.jensvh.spotifree.utils.Console;

public class YTMusicAPI {

	private static List<Video> search(String query) {
		List<Video> results = new ArrayList<Video>();
		MusicRequest request = new MusicRequest("search")
				.setQuery(query)
				.setFilter(Filter.SONGS);
		results.addAll(request.send());
		request = new MusicRequest("search")
				.setQuery(query)
				.setFilter(Filter.VIDEO);
		results.addAll(request.send());
		return results;
	}
	
	public static Video getMostSuitable(Track track) {
		List<Video> results = search(String.join(" ", track.getName(), 
								track.getAlbum().getName(), track.getArtists()[0].getName()));
		for (Video video : results) {
			if (video.equals(track))
				return video;
		}
		Console.errPrint("Could not find a comparable track for " + track.getName() + " by " + track.getArtists()[0].getName() + ".");
		return null;
	}
	
	public static Video getMostSuitable(SimplifiedTrack track) {
		List<Video> results = search(String.join(" ", track.getName(), track.getArtists()[0].getName()));
		for (Video video : results) {
			if (video.equals(track))
				return video;
		}
		Console.errPrint("Could not find a comparable track for " + track.getName() + " by " + track.getArtists()[0].getName() + ".");
		return null;
	}
	
}
