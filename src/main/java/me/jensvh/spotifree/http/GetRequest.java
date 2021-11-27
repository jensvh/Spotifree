package me.jensvh.spotifree.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import me.jensvh.spotifree.utils.Error;

public class GetRequest {
	
	private final String base_url;
	private List<NameValuePair> parameters;
	private List<Header> headers;
	
	public GetRequest(String base_url) {
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
			HttpGet get = new HttpGet(uri);
			
			for (Header header : this.headers) {
			    get.addHeader(header.getName(), header.getValue());
			}
			
			return client.execute(get, new ResponseHandler<T>(handler));
		} catch(IOException | URISyntaxException ex) {
			throw new Error(2, ex, "Could be anything, it just happened while sending a get request.");
		} 
	}
	
	public GetRequest addHeader(String key, String value) {
		this.headers.add(new BasicHeader(key, value));
		return this;
	}
	
	public GetRequest addParameter(String key, String value) {
		this.parameters.add(new BasicNameValuePair(key, value));
		return this;
	}
	
}
