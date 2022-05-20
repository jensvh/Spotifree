package me.jensvh.spotifree.ytmusic;

import java.util.List;

import org.apache.http.HttpHeaders;

import com.google.gson.JsonObject;

import me.jensvh.spotifree.api.ytmusic.Filter;
import me.jensvh.spotifree.api.ytmusic.Video;
import me.jensvh.spotifree.http.JsonResponseHandler;
import me.jensvh.spotifree.http.PostRequest;

public class MusicRequest extends PostRequest {
	
    /**
     * TODO check video, because it doesn't download videos
     */
	private static final String base_url = "https://music.youtube.com/youtubei/v1/";
	private static final String params = "?alt=json&key=AIzaSyC9XL3ZjWddXya6X74dJoCTL-WEYFDNX30";
	
	private String query;
	private Filter filter;
	
	public MusicRequest(String endpoint) {
		super(base_url + endpoint + params);
	}
	
	public List<Video> send() {
		super.addHeader(HttpHeaders.REFERER, "https://music.youtube.com/search");
		super.addHeader("Origin", "https://music.youtube.com");
		super.setJsonEntity("{\"context\":{\"client\":{\"clientName\":\"WEB_REMIX\",\"clientVersion\":\"0.1\"}},\"query\":\"" + query + "\",\"params\":\"" + filter.getToken() + "\"}");
		JsonObject tree = super.send(new JsonResponseHandler());
		return YtDeserializer.deserialize(tree, filter);
	}
	
	public MusicRequest setQuery(String query) {
		this.query = query;
		return this;
	}
	
	public MusicRequest setFilter(Filter filter) {
		this.filter = filter;
		return this;
	}
	
}
