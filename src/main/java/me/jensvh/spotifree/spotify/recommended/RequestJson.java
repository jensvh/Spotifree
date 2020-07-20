package me.jensvh.spotifree.spotify.recommended;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestJson {
	
	private String playlistURI;
	private String[] trackSkipIDs;
	
	

}
