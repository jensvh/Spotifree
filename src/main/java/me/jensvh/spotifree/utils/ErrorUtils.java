package me.jensvh.spotifree.utils;

import java.io.IOException;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.ParseException;

public class ErrorUtils {

	public static void trackAndTrace(int code, String reason, String body, Header[] headers) throws ParseException, IOException {
		Console.println("Error " + code + ": " + reason);
		Console.println("----- Headers -----");
		Arrays.stream(headers).forEach((header) -> {
			Console.println(header.getName() + ": " + header.getValue());
		});
		Console.println("----- Body -----");
		Console.println(body);
	}
	
}
