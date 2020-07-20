package me.jensvh.spotifree.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonResponseHandler implements Handler<JsonObject> {
	
	@Override
	public JsonObject handle(String entity) {
		JsonObject tree = JsonParser.parseString(entity).getAsJsonObject();
		
		if (!tree.has("contents")) {
			return null;
		}
		
		return tree;
	}

}