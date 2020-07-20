package me.jensvh.spotifree.api.spotify;

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Track extends SimplifiedTrack {

	private SimplifiedAlbum album;
	
	@Override
	public String toString() {
		return String.join("\n", 
					"id: " + id,
					"name: " + name,
					"artists: " + Arrays.toString(artists),
					"album_name: " + album.getName(),
					"album_artists: " + Arrays.toString(album.getArtists()),
					"duration: " + duration_ms,
					"track_number: " + track_number
				);
	}
}
