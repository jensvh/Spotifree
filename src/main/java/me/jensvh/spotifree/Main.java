package me.jensvh.spotifree;

import java.util.Optional;

import me.jensvh.spotifree.api.spotify.UrlType;
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
		Bootstrap bootstrap = new Bootstrap(args);
		
		// options
		downloadLrc = bootstrap.get("lrc");
		debugging = bootstrap.get("debug");
		boolean recommendedPlaylist = bootstrap.get("recom") || bootstrap.get("recommended");
		int max_songs = Integer.valueOf(Optional.ofNullable(bootstrap.getArg("max")).orElse(MAX_SONGS_RECOM_PLAYLIST));
		int recom_per_song = Integer.valueOf(Optional.ofNullable(bootstrap.getArg("group")).orElse(SONGS_PER_GROUP_RECOM_PLAYLIST));
		String url = bootstrap.getArg("url");
		
		if (url == null) {
			System.out.println("No url found.");
			System.out.println("Please use: java -jar spotifree.jar --url <url>");
			System.out.println("-lrc => for downloading lrc files with lyrics");
			System.out.println("-recom or -recommended => for downloading recommended songs for a playlist");
			System.out.println("--max <amount> => for the amount of recommended songs to download");
			System.out.println("--group <amount> => for the amount of recommended songs per group of songs");
			return;
		}
		
		System.out.print("Spotifree 0% initializing...\r");
		
		// Export libraries
		YtDlApi.unpack();
		
		// Update youtube-dl
		YtDlApi.update();
		
		UrlType type = Spotifree.getUrlType(url);
		String id = Spotifree.getId(url);

		if (type == UrlType.UNKNOWN || id == null) {
			System.out.println("Wrong url, currently only Spotify url's are supported.");
			return;
		}
		
		if (recommendedPlaylist && type == UrlType.PLAYLIST) {
			Spotifree.downloadAndCreateRecommendedPlaylist(id, max_songs, recom_per_song);
		}
		else if (recommendedPlaylist) {
			System.out.println("You can only download a recommended playlist from a playlist url.");
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
				System.out.println("Something went wrongt, in theorie there is no way to get here.");
				break;
			}
		}
		
	}

}
