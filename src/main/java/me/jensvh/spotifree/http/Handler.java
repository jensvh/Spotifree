package me.jensvh.spotifree.http;

public interface Handler<T> {
	
	T handle(String entity);

}
