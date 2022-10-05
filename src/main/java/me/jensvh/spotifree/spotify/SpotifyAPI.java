package me.jensvh.spotifree.spotify;

import me.jensvh.spotifree.api.spotify.Album;
import me.jensvh.spotifree.api.spotify.AudioFeature;
import me.jensvh.spotifree.api.spotify.Auth;
import me.jensvh.spotifree.api.spotify.ColoredLyricsHelper;
import me.jensvh.spotifree.api.spotify.Lyrics;
import me.jensvh.spotifree.api.spotify.PagingPlaylist;
import me.jensvh.spotifree.api.spotify.PagingPlaylistItem;
import me.jensvh.spotifree.api.spotify.Playlist;
import me.jensvh.spotifree.api.spotify.RecommendedTracks;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.chrome.ChromeImpl;
import me.jensvh.spotifree.firefox.FirefoxImpl;
import me.jensvh.spotifree.http.GetRequest;
import me.jensvh.spotifree.http.GsonResponseHandler;
import me.jensvh.spotifree.utils.Console;
import me.jensvh.spotifree.utils.Utils;

public class SpotifyAPI {

	// Spotify data
    public static String token = null;
	
	public static void openconnection() {
	    String cookies = "";
	    
        cookies = FirefoxImpl.getCookies();
        if (cookies == null) {
        	cookies = ChromeImpl.getCookies();
        	if (cookies == null) {
        		System.out.println("Make sure you have an active spotify connection on a chrome or firefox browser. (open.spotify.com)");
        		System.exit(27);
        	} else {
        		System.out.println("Found a spotify connection on Chrome");
        	}
        } else {
        	System.out.println("Found a spotify connection on Firefox.");
        }
	    
	    GetRequest post = new GetRequest("https://open.spotify.com/get_access_token?reason=transport&productType=web_player")
	            .addHeader("Cookie", cookies);
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
				.addHeader("Authorization", "Bearer " + token);
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
	
	public static Lyrics getLyrics(String trackId) {
	    checkConnection();
	    String url = "https://spclient.wg.spotify.com/color-lyrics/v2/track/" + trackId + "?format=json&vocalRemoval=false&market=from_token";

	    GetRequest get = new GetRequest(url)
	            .addHeader("app-platform", "WebPlayer")
	            .addHeader("Connection", "close")
	            .addHeader("Sec-Fetch-Dest", "empty")
	            .addHeader("Sec-Fetch-Mode", "cors")
	            .addHeader("Sec-Fetch-Site", "same-site")
	            .addHeader("authorization", "Bearer " + token);
	    
	    return get.send(new GsonResponseHandler<ColoredLyricsHelper>(ColoredLyricsHelper.class)).getLyrics();
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
	
	public static PagingPlaylistItem getPlaylists() {
	    checkConnection();
	    
	    GetRequest get = new GetRequest("https://api.spotify.com/v1/me/playlists")
	            .addHeader("Authorization", "Bearer " + token);
	    return get.send(new GsonResponseHandler<PagingPlaylistItem>(PagingPlaylistItem.class));
	}
	
	public static PagingPlaylistItem getPlaylistItemPaging(String url) {
        checkConnection();
        GetRequest get = new GetRequest(url)
                .addHeader("Authorization", "Bearer " + token);
        PagingPlaylistItem playlist = get.send(new GsonResponseHandler<PagingPlaylistItem>(PagingPlaylistItem.class));
        
        return playlist;
    }
	
	public static AudioFeature getAudiofeature(String id) {
		checkConnection();
		
		GetRequest get = new GetRequest("https://api.spotify.com/v1/audio-features/" + id)
	            .addHeader("Authorization", "Bearer " + token);
	    return get.send(new GsonResponseHandler<AudioFeature>(AudioFeature.class));
	}
	
	private static void checkConnection() {
		if (token == null)
			openconnection();
	}
}
