package me.jensvh.spotifree;

import java.util.List;
import java.util.Optional;

import me.jensvh.spotifree.api.spotify.UrlType;
import me.jensvh.spotifree.utils.Console;
import me.jensvh.spotifree.utils.Error;
import me.jensvh.spotifree.ytdl.YtDlApi;

public class Main {
	
	/**
	 * String test_track = "4lxlbCDntNIJyZSqgV0mzz";
		String test_album = "7bNCCK8eObScVeCpbW9jQx";
		String test_playlist = "0wtJlsdwSOif7QQ2IDcbAS";
		String test_long_playlist = "7hzTsxRtQOUiCfCJIXdWTx";
	 */
	
	public static boolean downloadLrc = false;
	public static boolean debugging = false;
	
	private static final String MAX_SONGS_RECOM_PLAYLIST = "20";
	private static final String SONGS_PER_GROUP_RECOM_PLAYLIST = "5";
	
	public static void main(String[] args) throws Error {
		Bootloader boot = new Bootloader(args);
		
		// options
		downloadLrc = boot.get("lrc");
		debugging = boot.get("debug");
		boolean sync = boot.get("sync");
		boolean recommendedPlaylist = boot.get("recom") || boot.get("recommended");
		int max_songs = Integer.valueOf(Optional.ofNullable(boot.getArg("max")).orElse(MAX_SONGS_RECOM_PLAYLIST));
		int recom_per_song = Integer.valueOf(Optional.ofNullable(boot.getArg("group")).orElse(SONGS_PER_GROUP_RECOM_PLAYLIST));
		String url = boot.getArg("url");
		if (url.indexOf('?') >= 0) {
		    url = url.substring(0, url.indexOf('?'));
		}
		
		if (sync) {
            List<String> ids = Spotifree.getPlaylists();
            
            for (String id : ids) {
                Spotifree.downloadPlaylist(id);
            }
            return;
        }
		
		if (url == null) {
			Console.println("No url found.");
			Console.println("Usage: java -jar spotifree.jar --url <url>");
			Console.println("-sync => for syncing all your playlists");
			Console.println("-lrc => for downloading lrc files with lyrics");
			Console.println("-recom or -recommended => for downloading recommended songs for a playlist");
			Console.println("--max <amount> => for the amount of recommended songs to download");
			Console.println("--group <amount> => for the amount of recommended songs per group of songs");
			return;
		}
		
		Console.print("Spotifree 0% initializing...");
		
		// Export libraries
		YtDlApi.unpack();
		
		// Update youtube-dl
		YtDlApi.update();

		if (sync) {
		    List<String> ids = Spotifree.getPlaylists();
		    
		    for (String id : ids) {
		        Spotifree.downloadPlaylist(id);
		    }
		    return;
		}
		
		UrlType type = Spotifree.getUrlType(url);
		String id = Spotifree.getId(url);

		if (type == UrlType.UNKNOWN || id == null) {
			Console.println("Wrong url, currently only Spotify url's are supported.");
			return;
		}
		
		if (recommendedPlaylist && type == UrlType.PLAYLIST) {
			Spotifree.downloadAndCreateRecommendedPlaylist(id, max_songs, recom_per_song);
		}
		else if (recommendedPlaylist) {
			Console.println("You can only download a recommended playlist from a playlist url.");
		}
		else {
			switch (type) {
			case TRACK:
				Spotifree.downloadSong(id);
				break;
			case ALBUM:
				Spotifree.downloadAlbum(id);
				break;
			case PLAYLIST:
				Spotifree.downloadPlaylist(id);
				break;
			default:
				Console.println("Something went wrongt, in theorie there is no way to get here.");
				break;
			}
		}
		
	}

}
