package me.jensvh.spotifree.mp3agic;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import me.jensvh.spotifree.api.spotify.Lyrics;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.canarado.LyricsApi;
import me.jensvh.spotifree.spotify.SpotifyAPI;
import me.jensvh.spotifree.utils.Console;
import me.jensvh.spotifree.utils.Error;
import me.jensvh.spotifree.utils.Utils;

public class Mp3agic {
	
	public static void addID3Tag(File file, Track track) {
		try {
			Mp3File mp3 = new Mp3File(file);
			
			// Remove current tags
			if (mp3.hasId3v1Tag())
				mp3.removeId3v1Tag();
			else if (mp3.hasCustomTag())
				mp3.removeId3v2Tag();
			else if (mp3.hasId3v2Tag())
				mp3.removeId3v2Tag();
			
			ID3v2 tag = new ID3v24Tag();
			mp3.setId3v2Tag(tag);
			
			// Add track data
			tag.setTitle(track.getName());
			tag.setArtist(track.getArtists()[0].getName());
			tag.setTrack(String.valueOf(track.getTrack_number()));
			tag.setAlbum(track.getAlbum().getName());
			tag.setAlbumArtist(track.getAlbum().getArtists()[0].getName());
			tag.setYear(track.getAlbum().getRelease_date());
			tag.setKey(track.getId());
			
			// Add album cover
			byte[] data = Utils.loadDateFromUrl(track.getAlbum().getImages()[0].getUrl());
			tag.setAlbumImage(data, "image/jpg");
			
			// Add lyrics
			// New spotify lyrics api
			try {
			    Lyrics lyrics = SpotifyAPI.getLyrics(track.getId());
			    if (lyrics.getLanguage().equalsIgnoreCase("en")) {
			        String fullLyrics = Utils.joinLyricsLines(lyrics.getLines());
			        tag.setLyrics(fullLyrics);
			    }
			} catch (Error err) {
			    err.printStackTrace();
			}
			
			/* Deprecated, old api
			 * try {
				String lyrics = LyricsApi.getLyrics(track);
				if (lyrics != null)
					tag.setLyrics(lyrics);
			
			} catch (Error err) {
				Console.print("Error with lyrics");
			}*/
			
			// Save
			String path = (file.getParent() == null) ? "" : file.getParent() + File.separator;
			mp3.save(path + Utils.removeInvalidPathChars(Utils.removeInBrackets(track.getName()) + " by " + Arrays.toString(track.getArtists())) + ".mp3");
		} catch (UnsupportedTagException | InvalidDataException | IOException | NotSupportedException e) {
			file.delete();
			throw new Error(4, e, "Could be anything, it just happened while adding metadata into the mp3 file.");
		}
	}
	
	public static void addID3Tag(File file, SimplifiedTrack track) {
		try {
			Mp3File mp3 = new Mp3File(file);
			
			// Remove current tags
			if (mp3.hasId3v1Tag())
				mp3.removeId3v1Tag();
			else if (mp3.hasCustomTag())
				mp3.removeId3v2Tag();
			else if (mp3.hasId3v2Tag())
				mp3.removeId3v2Tag();
			
			ID3v2 tag = new ID3v24Tag();
			mp3.setId3v2Tag(tag);
			
			// Add track data
			tag.setTitle(track.getName());
			tag.setArtist(track.getArtists()[0].getName());
			tag.setTrack(String.valueOf(track.getTrack_number()));
			tag.setKey(track.getId());
			
			// Add lyrics
			try {
				String lyrics = LyricsApi.getLyrics(track);
				if (lyrics != null)
					tag.setLyrics(lyrics);
			} catch(Error err) {
				Console.errPrint(err.getMessage());
			}
			
			// Save
			String path = (file.getParent() == null) ? "" : file.getParent() + File.separator;
			mp3.save(path + Utils.removeInvalidPathChars(Utils.removeInBrackets(Utils.removeInvalidPathChars(track.getName())) + " by " + Arrays.toString(track.getArtists())) + ".mp3");
		} catch (UnsupportedTagException e) {
			e.printStackTrace();
		} catch (InvalidDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		}
		
	}

	public static String getKey(File file) {
		try {
			Mp3File mp3 = new Mp3File(file);
			
			if (mp3.hasId3v2Tag())
				return mp3.getId3v2Tag().getKey();
			return null;
		} catch(IOException ex) {
			ex.printStackTrace();
			System.exit(9);
		} catch (UnsupportedTagException e) {
			e.printStackTrace();
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
