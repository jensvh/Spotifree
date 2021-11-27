package me.jensvh.spotifree.api.spotify;

import lombok.Getter;
import lombok.Setter;

/**
 * Url: https://open.spotify.com/color-lyrics/v2/track/11Ojp7JniVvwd0gmgvyKkd?format=json&vocalRemoval=false&market=from_token
 * @author Ikke
 *
 */
@Getter
@Setter
public class Lyrics {
    
    private String syncType;
    
    private LyricsLine[] lines;
    
    private String language;

}
