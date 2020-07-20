package me.jensvh.spotifree.canarado;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import me.jensvh.spotifree.api.canarado.Lyrics;
import me.jensvh.spotifree.api.canarado.LyricsResponse;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.http.GetRequest;
import me.jensvh.spotifree.http.GsonResponseHandler;
import me.jensvh.spotifree.utils.Error;

public class LyricsApi {
	
	public static String getLyrics(SimplifiedTrack track) {
		try {
			String url = URLEncoder.encode(track.getName() + " " + Arrays.toString(track.getArtists()).replaceAll("[\\[\\]\\,]", ""), StandardCharsets.UTF_8.toString());
			GetRequest get = new GetRequest("https://api.canarado.xyz/lyrics/" + url);
			
			LyricsResponse response = get.send(new GsonResponseHandler<LyricsResponse>(LyricsResponse.class));
			
			for (Lyrics lyrics : response.getContent()) {
				if (lyrics.equels(track)) {
					return lyrics.getLyrics();
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new Error(1, e, "An incorrect url for retrieving the lyrics.");
		}
		return null;
		
	}

}
