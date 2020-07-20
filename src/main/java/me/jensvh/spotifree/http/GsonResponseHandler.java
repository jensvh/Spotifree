package me.jensvh.spotifree.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonResponseHandler<T> implements Handler<T> {

	private Class<T> cls;
	
	public GsonResponseHandler(Class<T> cls) {
		this.cls = cls;
	}
	
	@Override
	public T handle(String entity) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(entity, cls);
	}

}
