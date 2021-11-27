package me.jensvh.spotifree.api.spotify;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth {
	
	@SerializedName("accessToken")
	private String token;

	
	
}
