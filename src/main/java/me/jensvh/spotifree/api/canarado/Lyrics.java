package me.jensvh.spotifree.api.canarado;

import lombok.Getter;
import lombok.Setter;
import me.jensvh.spotifree.api.spotify.SimplifiedArtist;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.utils.Utils;

@Getter
@Setter
public class Lyrics {

	private String title;
	private String lyrics;
	private String artist;
	
	public boolean equels(SimplifiedTrack track) {
		if (!Utils.simplify(title).contains(Utils.simplify(track.getName()))) {
			return false;
		}
		
		// artists, one should be the same
		for (SimplifiedArtist simlpyartist : track.getArtists()) {
			if (Utils.simplify(artist).contains(Utils.simplify(simlpyartist.getName()))) {
				return true;
			}
		}
		return false;
	}
	
}
