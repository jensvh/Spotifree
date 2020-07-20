package me.jensvh.spotifree.api.spotify;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import me.jensvh.spotifree.spotify.SpotifyAPI;

@Getter
@Setter
public class Playlist {

	private String id;
	private String name;
	private String description;
	
	@SerializedName("tracks")
	private PagingObject<PlaylistTrack> paging;
	
	private transient List<Track> tracks = new ArrayList<Track>();
	
	public void getNextTracks() {
		// Add current tracks
		for (PlaylistTrack track : paging.getItems()) {
			tracks.add(track.getTrack());
		}
		
		// if there are more tracks get them
		if (paging.getNext() != null) {
			Playlist playlist = SpotifyAPI.get(paging.getNext(), Playlist.class);
			playlist.getNextTracks();
			tracks.addAll(playlist.getTracks());
		}
		
		// Clear some memory
		paging = null;
	}
	
	@Override
	public String toString() {
		return String.join("\n", 
				"id: " + id,
				"name: " + name,
				"description: " + description,
				"track_count: " + tracks.size(),
				"tracks: " + tracks.toString()
			);
	}
	
}
