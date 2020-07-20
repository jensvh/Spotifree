package me.jensvh.spotifree.api.ytmusic;

public enum Filter {

	SONGS("Eg-KAQwIARAAGAAgACgAMABqChADEAQQCRAFEAo%3D"),
	VIDEO("Eg-KAQwIABABGAAgACgAMABqChADEAQQCRAFEAo%3D"),
	NORMAL("");
	
	private String token;
	
	private Filter(String str) {
		this.token = str;
	}
	
	public String getToken() {
		return token;
	}
	
}
