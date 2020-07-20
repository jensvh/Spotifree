package me.jensvh.spotifree.api.ytmusic;

import lombok.Getter;
import lombok.Setter;
import me.jensvh.spotifree.api.spotify.SimplifiedArtist;
import me.jensvh.spotifree.api.spotify.SimplifiedTrack;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.utils.Utils;

@Getter
@Setter
public class Video {
	// Base serialize: contents/sectionListRenderer/contents/musicShelfRenderer/contents

	//@SerializedName("musicResponsiveListItemRenderer/overlay/musicItemThumbnailOverlayRenderer/content/musicPlayButtonRenderer/playNavigationEndpoint/watchEndpoint/videoId")
	//@SerializedName("musicResponsiveListItemRenderer/doubleTapCommand/watchEndpoint/videoId")
	private String video_id;
	//@SerializedName("musicResponsiveListItemRenderer/flexColumns/0/musicResponsiveListItemFlexColumnRenderer/text/runs/0/text")
	private String title;
	//@SerializedName("musicResponsiveListItemRenderer/flexColumns/1/musicResponsiveListItemFlexColumnRenderer/text/runs/.../text")
	private String[] artists; // One artists, one sepperator
	//@SerializedName("musicResponsiveListItemRenderer/flexColumns/3/musicResponsiveListItemFlexColumnRenderer/text/runs/0/text")
	private String duration; // minutes:seconds
	
	public boolean equals(SimplifiedTrack track) {
		if (!Utils.simplify(title).contains(Utils.simplify(track.getName()))) {
			return false;
		}
		int delta_time = Math.abs(track.getDuration_ms() - Utils.to_ms(duration));
		if (delta_time > 2000) { // Max 2 seconds difference
			return false;
		}
		// artists, one should be the same
		for (String artist : artists) {
			for (SimplifiedArtist artist2 : track.getArtists()) {
				if (Utils.simplify(artist).contains(Utils.simplify(artist2.getName()))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean equals(Track track) {
		return equals((SimplifiedTrack)track);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		return String.join("\n", 
				"id: " + video_id,
				"title: " + title,
				"artists: " + String.join(", ", artists),
				"duration: " + duration);
	}
}