package me.jensvh.spotifree.api.ytmusic;

import lombok.Getter;
import lombok.Setter;
import me.jensvh.spotifree.api.spotify.Track;
import me.jensvh.spotifree.utils.Utils;

@Getter
@Setter
public class Song extends Video {
	// Base serialize: contents/sectionListRenderer/contents/musicShelfRenderer/contents
	
	//@SerializedName("musicResponsiveListItemRenderer/overlay/musicItemThumbnailOverlayRenderer/content/musicPlayButtonRenderer/playNavigationEndpoint/watchEndpoint/videoId")
	//@SerializedName("musicResponsiveListItemRenderer/doubleTapCommand/watchEndpoint/videoId")
	//private String video_id;
	//@SerializedName("musicResponsiveListItemRenderer/flexColumns/0/musicResponsiveListItemFlexColumnRenderer/text/runs/0/text")
	//private String title;
	//@SerializedName("musicResponsiveListItemRenderer/flexColumns/1/musicResponsiveListItemFlexColumnRenderer/text/runs/.../text")
	//private String[] artists; // One artists, one sepperator
	//@SerializedName("musicResponsiveListItemRenderer/flexColumns/2/musicResponsiveListItemFlexColumnRenderer/text/runs/0/text")
	private String album;
	//@SerializedName("musicResponsiveListItemRenderer/flexColumns/3/musicResponsiveListItemFlexColumnRenderer/text/runs/0/text")
	//private String duration; // minutes:seconds
	
	@Override
	public boolean equals(Track track) {
		if (!super.equals(track)) {
			return false;
		}
		return (Utils.simplify(album).contains(Utils.simplify(track.getAlbum().getName()))
				|| Utils.simplify(album).contains(Utils.simplify(track.getName())));
	}
	
	@Override
	public String toString() {
		return String.join("\n", 
				"id: " + getVideo_id(),
				"title: " + getTitle(),
				"artists: " + String.join(", ", getArtists()),
				"album: " + album,
				"duration: " + getDuration());
	}
}
