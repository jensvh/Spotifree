package me.jensvh.spotifree.canarado;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import me.jensvh.spotifree.api.canarado.LyricsResponse;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.http.GetRequest;
import me.jensvh.spotifree.http.GsonResponseHandler;
import me.jensvh.spotifree.utils.Console;
import me.jensvh.spotifree.utils.Error;
import me.jensvh.spotifree.utils.Utils;

public class LyricsApi {
	
	public static String getLyrics(SimplifiedTrack track) {
		try {
			String url = URLEncoder.encode(Utils.removeInvalidPathChars(track.getName() + " " + Arrays.toString(track.getArtists()).replaceAll("[\\[\\]\\,]", "")), StandardCharsets.UTF_8.toString());
			GetRequest get = new GetRequest("https://some-random-api.ml/lyrics?title=" + url);
			try {
				LyricsResponse response = get.send(new GsonResponseHandler<LyricsResponse>(LyricsResponse.class));
				return response.getLyrics();
			} catch (Error ex) {
				if (ex.hashCode() == 404) {
					return null; // Lyrics not found
				}
				Console.println("Something went wrong while parsing the lyrics, proceeding.");
				throw new Error(ex.hashCode(), ex, "No idea");
			}
		} catch (UnsupportedEncodingException e) {
			throw new Error(1, e, "An incorrect url for retrieving the lyrics.");
		}
	}

}
