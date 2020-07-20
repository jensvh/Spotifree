package me.jensvh.spotifree.api.spotify;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth {
	
	@SerializedName("access_token")
	private String token;
	@SerializedName("token_type")
	private String type;
	@SerializedName("expires_in")
	private int expires_time;

	
	
}
