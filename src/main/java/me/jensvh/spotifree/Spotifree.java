package me.jensvh.spotifree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jensvh.spotifree.api.spotify.Album;
import me.jensvh.spotifree.api.spotify.PagingPlaylistItem;
import me.jensvh.spotifree.api.spotify.Playlist;
import me.jensvh.spotifree.api.spotify.PlaylistItem;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.api.spotify.UrlType;
import me.jensvh.spotifree.api.ytmusic.Video;
import me.jensvh.spotifree.id3.Mp3agic;
import me.jensvh.spotifree.spotify.SpotifyAPI;
import me.jensvh.spotifree.utils.Console;
import me.jensvh.spotifree.utils.Utils;
import me.jensvh.spotifree.ytdl.YtDlApi;
import me.jensvh.spotifree.ytmusic.YTMusicAPI;

public class Spotifree {
	
	public static void downloadSong(String id) {
		Track track = SpotifyAPI.getTrack(id);
		downloadTrack(track, null);
		Console.print("Downloaded " + track.getName() + " by " + Arrays.toString(track.getArtists()));
	}
	
	public static void downloadPlaylist(String id) {
		Playlist playlist = SpotifyAPI.getPlaylist(id);
		Progressbar progress = new Progressbar("Playlist " + playlist.getName(), playlist.getTracks().size());
		File folder = Utils.createFolder(playlist.getName());
		
		List<String> existings = Utils.removeNoLongerExistingMp3s(folder, playlist);
		
		// Download non existing songs
		for (int i = 0; i < playlist.getTracks().size(); i++) {
			if (existings.contains(playlist.getTracks().get(i).getId())) {
				progress.step(playlist.getTracks().get(i).getName() + " already exists.");
				continue;
			}
			progress.setSuffix("Downloading " + playlist.getTracks().get(i).getName());
			downloadTrack(playlist.getTracks().get(i), playlist.getName());
			progress.step("Downloaded " + playlist.getTracks().get(i).getName());
		}
	}
	
	public static void downloadAlbum(String id) {
		Album album = SpotifyAPI.getAlbum(id);
		Progressbar progress = new Progressbar("Album " + album.getName(), album.getTracks().size());
		File folder = Utils.createFolder(album.getName());
		
		List<String> existings = Utils.removeNoLongerExistingMp3s(folder, album);
		
		// Download non existing songs
		for (int i = 0; i < album.getTracks().size(); i++) {
			if (existings.contains(album.getTracks().get(i).getId())) {
				progress.step(album.getTracks().get(i).getName() + " already exists.");
				continue;
			}
			progress.setSuffix("Downloading " + album.getTracks().get(i).getName());
			downloadTrack(album.getTracks().get(i), album.getName());
			progress.step("Downloaded " + album.getTracks().get(i).getName());
		}
		
	}

	private static List<SimplifiedTrack> createRecommendedPlaylist(Playlist playlist, int max, int diff) {
		List<SimplifiedTrack> tracks = new ArrayList<SimplifiedTrack>();
		
		while(tracks.size() < max) {
			Track[] sample_tracks = Utils.pick5RandomTracks(playlist.getTracks());
			SimplifiedTrack[] recommended_tracks = SpotifyAPI.getRecommendations(sample_tracks, (tracks.size() + diff > max) ? max - tracks.size() : diff);
			for (SimplifiedTrack recommended_track : recommended_tracks) 
				tracks.add(recommended_track);
		}
		
		return tracks;
	}
	
	public static void downloadAndCreateRecommendedPlaylist(String id, int max_songs, int max_per_song) {
		Playlist playlist = SpotifyAPI.getPlaylist(id);
		Progressbar progress = new Progressbar("Recommended of " + playlist.getName(), max_songs);
		List<SimplifiedTrack> recommended_tracks = createRecommendedPlaylist(playlist, max_songs, max_per_song);
		
		
		recommended_tracks.forEach((track) -> {
			progress.setSuffix("Downloading " + track.getName());
			downloadSimplifiedTrack(track, playlist.getName() + "_recommended");
			progress.step();
		});
	}
	
	private static void downloadTrack(Track track, String path) {
		try {
			Video video = YTMusicAPI.getMostSuitable(track);
			if (video == null)
				return;
			File file = YtDlApi.downloadMp3(video.getVideo_id(), path);
			Mp3agic.addID3Tag(file, track);
			//JAudioTagger.addID3Tag(file, track);
			//file.delete();
			String folder = ((path != null) ? Utils.removeInvalidPathChars(path) + File.separator : "");
			file.renameTo(new File(folder + Utils.removeInvalidPathChars(Utils.removeInBrackets(track.getName()) + " by " + Arrays.toString(track.getArtists())) + ".mp3"));
		} catch (Exception e) {
			if (Main.debugging) {
				e.printStackTrace();
			} else {
				System.out.println("Error while downloading song, proceeding to next song.");
			}
		}
	}
	
	private static void downloadSimplifiedTrack(SimplifiedTrack track, String path) {
		try {
			Video video = YTMusicAPI.getMostSuitable(track);
			if (video == null)
				return;
			File file = YtDlApi.downloadMp3(video.getVideo_id(), path);
			Mp3agic.addID3Tag(file, track);
			//JAudioTagger.addID3Tag(file, track);
			file.delete();
		} catch (Exception e) {
			if (Main.debugging) {
				e.printStackTrace();
			} else {
				System.out.println("Error while downloading song, proceeding to next song.");
			}
		}
	}
	
	public static List<String> getPlaylists() {
	    List<String> ids = new ArrayList<>();
        PagingPlaylistItem playlists = SpotifyAPI.getPlaylists();
        
        do {
            for (PlaylistItem playlist : playlists.getItems()) {
                if (playlist.getId() == null || playlist.getId().isEmpty()) 
                    continue;
                ids.add(playlist.getId());
            }
            
            // if there are more tracks get them
            if (playlists.getNext() != null) {
                playlists = SpotifyAPI.getPlaylistItemPaging(playlists.getNext());
            } else {
                playlists = null;
            }
        } while (playlists != null);
        
        return ids;
	}
	
	public static UrlType getUrlType(String url) {
		if (url.matches("https*\\:\\/\\/open\\.spotify\\.com\\/track\\/[a-zA-Z0-9]{22}")) {
			return UrlType.TRACK;
		} else if (url.matches("https*\\:\\/\\/open\\.spotify\\.com\\/album\\/[a-zA-Z0-9]{22}")) {
			return UrlType.ALBUM;
		} else if (url.matches("https*\\:\\/\\/open\\.spotify\\.com\\/playlist\\/[a-zA-Z0-9]{22}")) {
			return UrlType.PLAYLIST;
		}
		return UrlType.UNKNOWN;
	}

	public static String getId(String url) {
		Pattern pattern = Pattern.compile("https*\\:\\/\\/open\\.spotify\\.com\\/\\w+\\/([a-zA-Z0-9]{22}?)");
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
}

/** TODO:
 * Url to (TypeEnum(track,album,playlist,...), id)
 */