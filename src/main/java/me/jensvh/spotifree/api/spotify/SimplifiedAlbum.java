package me.jensvh.spotifree.api.spotify;

import lombok.Getter;
import lombok.Setter;
import me.jensvh.spotifree.utils.Utils;

@Getter
@Setter
public class SimplifiedAlbum {

	protected String id;
	protected String name;
	protected SimplifiedArtist[] artists;
	
	protected Image[] images;
	protected String release_date;
	
	public String getName() {
		return Utils.removeInBrackets(name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
