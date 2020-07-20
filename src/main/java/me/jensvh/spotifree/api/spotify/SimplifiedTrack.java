package me.jensvh.spotifree.api.spotify;

import lombok.Getter;
import lombok.Setter;
import me.jensvh.spotifree.spotify.SpotifyAPI;
import me.jensvh.spotifree.utils.Utils;

@Getter
@Setter
public class SimplifiedTrack {

	protected String id;
	protected String name;
	
	protected SimplifiedArtist[] artists;
	
	protected int duration_ms;
	protected int track_number;
	
	public Track getFullTrack() {
		return SpotifyAPI.getTrack(id);
	}
	
	public String getName() {
		return Utils.removeInBrackets(Utils.removeAfterDash(name));
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
