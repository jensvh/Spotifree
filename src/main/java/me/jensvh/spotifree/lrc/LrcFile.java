package me.jensvh.spotifree.lrc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jensvh.spotifree.api.spotify.Lyrics;
import me.jensvh.spotifree.api.spotify.LyricsLine;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.spotify.SpotifyAPI;
import me.jensvh.spotifree.utils.Utils;

public class LrcFile {
    
    public static void createLrcFile(File file, Track track) {
        List<String> lines = new ArrayList<>();
        
        // Create lines of lrcFile
        lines.add("[ar: " + track.getArtists()[0].getName() + "]");
        lines.add("[al: " + track.getAlbum().getName() + "]");
        lines.add("[ti: " + track.getName() + "]");
        lines.add("");
        
        Lyrics lyrics = SpotifyAPI.getLyrics(track.getId());
        for (LyricsLine line : lyrics.getLines()) {
            lines.add("[" + toTimeTag(line.getStartTimeMs()) + "] " + line.getWords());
        }
        
        // Save file
        String path = (file.getParent() == null) ? "" : file.getParent() + File.separator;
        String file_name = (path + Utils.removeInvalidPathChars(Utils.removeInBrackets(track.getName()) + " by " + Arrays.toString(track.getArtists())) + ".lrc");
        
        try (PrintWriter out = new PrintWriter(file_name)) {
            for (String line : lines) {
                out.println(line);
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void createLrcFile(File file, SimplifiedTrack track) {
        List<String> lines = new ArrayList<>();
        
        // Create lines of lrcFile
        lines.add("[ar: " + track.getArtists()[0].getName() + "]");
        lines.add("[ti: " + track.getName() + "]");
        lines.add("");
        
        Lyrics lyrics = SpotifyAPI.getLyrics(track.getId());
        for (LyricsLine line : lyrics.getLines()) {
            lines.add("[" + toTimeTag(line.getStartTimeMs()) + "] " + line.getWords());
        }
        
        // Save file
        String path = (file.getParent() == null) ? "" : file.getParent() + File.separator;
        String file_name = (path + Utils.removeInvalidPathChars(Utils.removeInBrackets(track.getName()) + " by " + Arrays.toString(track.getArtists())) + ".lrc");
        
        try (PrintWriter out = new PrintWriter(file_name)) {
            for (String line : lines) {
                out.println(line);
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static String toTimeTag(String str) {
        // Format: mm:ss.xx
        int ms = Integer.parseInt(str);
        int xx = (ms % 1000) / 10;
        int ss = (ms / 1000) % 60;
        int mm = (ms / 1000) / 60;
        
        return format2Digits(mm) + ":" + format2Digits(ss) + ":" + format2Digits(xx);
    }
    
    private static String format2Digits(int i) {
        return (i < 10 ? "0" : "") + i;
    }
}
