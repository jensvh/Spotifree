package me.jensvh.spotifree.id3;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.valuepair.ImageFormats;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.jaudiotagger.tag.reference.PictureTypes;

import me.jensvh.spotifree.api.spotify.Lyrics;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.spotify.SpotifyAPI;
import me.jensvh.spotifree.utils.Utils;

public class JAudioTagger {
    
    public static void addID3Tag(File file, Track track) {
        try {
            MP3File mp3 = (MP3File)AudioFileIO.read(file);
            mp3.setTag(new ID3v24Tag());
            
            ID3v24Tag tag = new ID3v24Tag();

            tag.setField(FieldKey.TITLE, track.getName());
            tag.setField(FieldKey.ARTIST, track.getArtists()[0].getName());
            tag.setField(FieldKey.TRACK, String.valueOf(track.getTrack_number()));
            tag.setField(FieldKey.ALBUM, track.getAlbum().getName());
            tag.setField(FieldKey.ALBUM_ARTIST, track.getAlbum().getArtists()[0].getName());
            tag.setField(FieldKey.YEAR, track.getAlbum().getRelease_date());
            tag.setField(FieldKey.KEY, track.getId());
            
            byte[] artData = Utils.loadDateFromUrl(track.getAlbum().getImages()[0].getUrl());
            Artwork art = ArtworkFactory.getNew();
            art.setBinaryData(artData);
            art.setMimeType(ImageFormats.getMimeTypeForBinarySignature(artData));
            art.setDescription("");
            art.setPictureType(PictureTypes.DEFAULT_ID);
            
            tag.deleteArtworkField();
            tag.addField(art);
            tag.setField(art);
            
            try {
                Lyrics lyrics = SpotifyAPI.getLyrics(track.getId());
                
                // Unsupported for now
                /*ID3v2SyncedLyrics sylt = new ID3v2SyncedLyrics();
                
                for (LyricsLine line : lyrics.getLines()) {
                    sylt.addLine(line.getWords(), Long.parseLong(line.getStartTimeMs()));
                }
                tag.setFrame(sylt.getFrame());
                */
                
                String text = Utils.joinLyricsLines(lyrics.getLines());
                
                tag.addField(FieldKey.LYRICS, text);
                
            } catch (Error err) {
                err.printStackTrace();
                System.out.println("You can only download lyrics when logged in to spotify on firefox.");
            }
            
            mp3.setID3v2Tag(tag);
            mp3.commit();
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException
                | InvalidAudioFrameException | CannotWriteException e) {
            e.printStackTrace();
        }
        
        
    }
    
    public static void addID3Tag(File file, SimplifiedTrack track) {
        try {
            MP3File mp3 = (MP3File)AudioFileIO.read(file);
            mp3.setTag(new ID3v24Tag());
            
            Tag tag = mp3.getTag();
            
            tag.setField(FieldKey.TITLE, track.getName());
            tag.setField(FieldKey.ARTIST, track.getArtists()[0].getName());
            tag.setField(FieldKey.TRACK, String.valueOf(track.getTrack_number()));
            tag.setField(FieldKey.KEY, track.getId());
            
            tag.deleteArtworkField();
            
            mp3.commit();
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException
                | InvalidAudioFrameException | CannotWriteException e) {
            e.printStackTrace();
        }
    }
    
    public static String getKey(File file) {
        try {
            MP3File mp3 = (MP3File)AudioFileIO.read(file);
            Tag tag = mp3.getTag();
            return tag.getFirstField(FieldKey.KEY).toString();
            
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException
                | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        return null;
    }

}
