package me.jensvh.spotifree.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import me.jensvh.spotifree.utils.Error;

public class PostRequest {
	
	private final String base_url;
	private List<NameValuePair> parameters;
	private List<Header> headers;
	private HttpEntity entity;
	
	public PostRequest(String base_url) {
		this.base_url = base_url;
		this.parameters = new ArrayList<NameValuePair>();
		this.headers = new ArrayList<Header>();
	}
	
	public <T> T send(Handler<T> handler) {
		try {
		    HttpClient client = HttpClients.custom()
		            .setDefaultRequestConfig(RequestConfig.custom()
		                    .setCookieSpec(CookieSpecs.STANDARD).build())
		            .build();
			URI uri = new URIBuilder(base_url)
						.addParameters(parameters)
						.build();
			
			HttpPost post = new HttpPost(uri);
			
			post.setEntity(entity);
			
			headers.forEach((header) -> post.addHeader(header));
			
			return client.execute(post, new ResponseHandler<T>(handler));
		} catch(IOException | URISyntaxException ex) {
			throw new Error(3, ex, "Could be anything, it just happened while sending a post request.");
		}
	}
	
	public PostRequest addHeader(String key, String value) {
		this.headers.add(new BasicHeader(key, value));
		return this;
	}
	
	public PostRequest addParameter(String key, String value) {
		this.parameters.add(new BasicNameValuePair(key, value));
		return this;
	}
	
	public PostRequest setEntity(HttpEntity entity) {
		this.entity = entity;
		return this;
	}
	
	public PostRequest setJsonEntity(String json) {
		this.entity = new StringEntity(json, "UTF-8");
		this.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		return this;
	}
	
	public PostRequest setFormEntity(List<NameValuePair> data) {
		try {
			this.entity = new UrlEncodedFormEntity(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		return this;
	}

}
