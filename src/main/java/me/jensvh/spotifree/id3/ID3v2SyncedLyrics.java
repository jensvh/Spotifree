package me.jensvh.spotifree.id3;

import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.tag.id3.ID3v24Frame;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT;

public class ID3v2SyncedLyrics {
    
    private List<LyricsLine> lines;
    
    public ID3v2SyncedLyrics() {
        this.lines = new ArrayList<>();
    }
    
    public void addLine(String text, long timeStamp) {
        this.lines.add(new LyricsLine(text, timeStamp));
    }
    
    
    private byte[] getLyricsAsBytes() {
        int i = 0;
        byte[] arr = new byte[getSize()];

        for (LyricsLine line : lines) {
            for (int j = 0; j < line.text.length(); j++)
            {
                arr[i++] = (byte) line.text.charAt(j);
            }
    
            arr[i++] = 0;
            arr[i++] = (byte) ((line.timeStamp & 0xFF000000) >> 24);
            arr[i++] = (byte) ((line.timeStamp & 0x00FF0000) >> 16);
            arr[i++] = (byte) ((line.timeStamp & 0x0000FF00) >> 8);
            arr[i++] = (byte) (line.timeStamp & 0x000000FF);
        }
        return arr;
    }
    
    private int getSize() {
        int length = 0;
        for (LyricsLine line : lines) {
            length += line.text.length() + 1 + 4;
        }
        return length;
    }
    
    public ID3v24Frame getFrame() {
        byte[] lyrics = getLyricsAsBytes();
        
         FrameBodySYLT lyr = new FrameBodySYLT(
                     TextEncoding.ISO_8859_1.getByte(),
                     "eng",
                     TimeStampFormat.MILISECONDS.getByte(),
                     ContentType.LYRICS.getByte(),
                     "test",
                     lyrics
                 );
         
         ID3v24Frame frame = new ID3v24Frame(ID3v24Frames.FRAME_ID_SYNC_LYRIC);
         frame.setBody(lyr);
         
         return frame;
    }
    
    
    private class LyricsLine {
        
        private String text;
        private long timeStamp; // Ms
        
        public LyricsLine(String line, long timeStamp) {
            this.text = line;
            this.timeStamp = timeStamp;
        }
    }
    
    private enum TextEncoding {
        ISO_8859_1((byte) 0),
        UNICODE((byte) 1);
        
        private byte b;
        private TextEncoding(byte b) {
            this.b = b;
        }
        
        public byte getByte() {
            return b;
        }
    }
    
    private enum ContentType {
        OTHER((byte) 0),
        LYRICS((byte) 1),
        TEXT_TRANSCRIPTION((byte) 2),
        MOVEMENT_PART_NAME((byte) 3),
        EVENTS((byte) 4),
        CHORD((byte) 5),
        TRIVIA_POP_UP_INFO((byte) 6);
        
        private byte b;
        private ContentType(byte b) {
            this.b = b;
        }
        
        public byte getByte() {
            return b;
        }
    }
    
    private enum TimeStampFormat {
        MPEG_FRAMES((byte) 1),
        MILISECONDS((byte) 2);
        
        private byte b;
        private TimeStampFormat(byte b) {
            this.b = b;
        }
        
        public byte getByte() {
            return b;
        }
    }

}
