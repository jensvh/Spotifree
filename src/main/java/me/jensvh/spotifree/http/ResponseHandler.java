package me.jensvh.spotifree.http;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;

import me.jensvh.spotifree.utils.Error;

public class ResponseHandler<T> implements org.apache.http.client.ResponseHandler<T> {

	private Handler<T> handler;
	
	public ResponseHandler(Handler<T> handler) {
		this.handler = handler;
	}
	
	@Override
	public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException, Error {
		int code = response.getStatusLine().getStatusCode();
		String entity = EntityUtils.toString(response.getEntity());
		String reason = response.getStatusLine().getReasonPhrase();
		
		if (code != 200) {
			throw new Error(code, new HttpException(), reason);
		}
		
		return handler.handle(entity);
	}

}
