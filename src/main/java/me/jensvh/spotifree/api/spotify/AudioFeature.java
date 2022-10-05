package me.jensvh.spotifree.api.spotify;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AudioFeature {
	
	private float danceability;
	private float energy;
	private int key;
	private float loudness;
	private int mode;
	private float speechiness;
	private float acousticness;
	private float instrumentalness;
	private float liveness;
	private float valence;
	private float tempo;
	private String type;
	private String id;
	private String uri;
	private String track_href;
	private String analysis_url;
	private int duration_ms;
	private int time_signature;

}
