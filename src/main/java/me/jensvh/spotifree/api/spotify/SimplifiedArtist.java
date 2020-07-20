package me.jensvh.spotifree.api.spotify;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedArtist {

	private String id;
	private String name;
	
	@Override
	public String toString() {
		return name;
	}
	
}
