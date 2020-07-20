package me.jensvh.spotifree.spotify.recommended;

import lombok.Getter;

@Getter
public class RecommendedTrack {

	private String id;
	private String originalId;
	private String name;
	private RecommendedArtist[] artists;
	private RecommendedAlbum album;
	private int duration;
	private boolean explicit;
	private int popularity;
	private double score;
	
}
