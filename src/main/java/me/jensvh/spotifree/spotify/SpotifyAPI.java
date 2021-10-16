package me.jensvh.spotifree.spotify;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import me.jensvh.spotifree.api.spotify.Album;
import me.jensvh.spotifree.api.spotify.Auth;
import me.jensvh.spotifree.api.spotify.PagingPlaylist;
import me.jensvh.spotifree.api.spotify.Playlist;
import me.jensvh.spotifree.api.spotify.RecommendedTracks;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.http.GetRequest;
import me.jensvh.spotifree.http.GsonResponseHandler;
import me.jensvh.spotifree.http.PostRequest;
import me.jensvh.spotifree.utils.Console;
import me.jensvh.spotifree.utils.Utils;

public class SpotifyAPI {

	// Spotify data
	private static final String clientId = "5e518b7259f240eeb0d88586e6e10c53";
	private static final String clientSecret = "e839801084f94ffd893dc3b0bae4fd39";
	public static String token = null;
	
	public static void openconnection() {
		PostRequest post = new PostRequest("https://accounts.spotify.com/api/token")
				.addHeader("Authorization",
						"Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));
		List<NameValuePair> formData = new ArrayList<NameValuePair>();
		formData.add(new BasicNameValuePair("grant_type", "client_credentials"));
		post.setFormEntity(formData);
		Auth auth = post.send(new GsonResponseHandler<Auth>(Auth.class));
		token = auth.getToken();
	}
	
	public static Track getTrack(String id) {
		checkConnection();
		GetRequest get = new GetRequest("https://api.spotify.com/v1/tracks/" + id)
				.addHeader("Authorization", "Bearer " + token);
		return get.send(new GsonResponseHandler<Track>(Track.class));
	}
	
	public static Playlist getPlaylist(String id) {
		checkConnection();
		GetRequest get = new GetRequest("https://api.spotify.com/v1/playlists/" + id)
				.addHeader("Authorization", "Bearer " + token)
				.addParameter("additional_types", "track")
				.addParameter("fields", "id,name,description,tracks(items(track(id,name,album(id,name,artists,images,release_date),artists(id,name),track_number,duration_ms)),next)");
		Playlist playlist = get.send(new GsonResponseHandler<Playlist>(Playlist.class));
		playlist.getNextTracks();
		return playlist;
	}

	public static PagingPlaylist getPlaylistPaging(String url) {
		checkConnection();
		GetRequest get = new GetRequest(url)
				.addHeader("Authorization", "Bearer " + token)
				.addParameter("fields", "items(track(id,name,album(id,name,artists,images,release_date),artists(id,name),track_number,duration_ms))");
		PagingPlaylist playlist = get.send(new GsonResponseHandler<PagingPlaylist>(PagingPlaylist.class));
		return playlist;
	}
	
	public static Album getAlbum(String id) {
		checkConnection();
		GetRequest get = new GetRequest("https://api.spotify.com/v1/albums/" + id)
				.addHeader("Authorization", "Bearer " + token);
		Album album = get.send(new GsonResponseHandler<Album>(Album.class));
		album.getNextTracks();
		return album;
	}
	
	public static <T> T get(String url, Class<T> cls) {
		GetRequest get = new GetRequest(url)
				.addHeader("Authorization", "Bearer " + token);
		return get.send(new GsonResponseHandler<T>(cls));
	}
	
	public static SimplifiedTrack[] getRecommendations(Track[] tracks, int max) {
		if (tracks.length > 5) {
			Console.errPrint("Spotify track recommendation can only get 5 tracks, no more.");
			return null;
		}
		checkConnection();
		GetRequest get = new GetRequest("https://api.spotify.com/v1/recommendations")
				.addHeader("Authorization", "Bearer " + token)
				.addParameter("limit", String.valueOf(max))
				.addParameter("seed_tracks", Utils.commaSepperatedString(tracks));
		
		RecommendedTracks recommendedTracks = get.send(new GsonResponseHandler<RecommendedTracks>(RecommendedTracks.class));
		
		return recommendedTracks.getTracks();
	}
	
	private static void checkConnection() {
		if (token == null)
			openconnection();
	}
}
