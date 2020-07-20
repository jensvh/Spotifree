package me.jensvh.spotifree.api.spotify;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import me.jensvh.spotifree.spotify.SpotifyAPI;

@Getter
@Setter
public class Album extends SimplifiedAlbum {

	@SerializedName("tracks")
	private PagingObject<SimplifiedTrack> paging;
	
	private transient List<Track> tracks = new ArrayList<Track>();
	
	public void getNextTracks() {
		// Add current tracks
		for (SimplifiedTrack track : paging.getItems()) {
			tracks.add(track.getFullTrack());
		}
		
		// if there are more tracks get them
		if (paging.getNext() != null) {
			Album album = SpotifyAPI.get(paging.getNext(), Album.class);
			album.getNextTracks();
			tracks.addAll(album.getTracks());
		}
		
		// Clear some memory
		paging = null;
	}
	
	@Override
	public String toString() {
		return String.join("\n", 
				"id: " + id,
				"name: " + name,
				"track_count: " + tracks.size(),
				"tracks: " + tracks.toString()
			);
	}
}
