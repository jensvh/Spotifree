package me.jensvh.spotifree.api.spotify;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingObject<T> {

	private String next;
	private T[] items;
	
	@Override
	public String toString() {
		return "paging:"
				+ next;
	}
	
}
