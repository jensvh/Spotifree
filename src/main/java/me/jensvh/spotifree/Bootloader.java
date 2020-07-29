package me.jensvh.spotifree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bootloader {
	
	private List<String> parameters;
	private Map<String, String> arguments;
	
	public Bootloader(String[] args) {
		this.parameters = new ArrayList<String>();
		this.arguments = new HashMap<String, String>();
		readInputArgs(args);
	}
	
	private void readInputArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i].toLowerCase().trim();
			if (arg.startsWith("--")) {
				String key = arg.substring(2).trim();
				String value = args[i+1];
				this.arguments.put(key, value);
				i++;
			} else if (arg.startsWith("-")) {
				String key = arg.substring(1).trim();
				this.parameters.add(key);
			}
		}
	}

	public boolean get(String key) {
		return this.parameters.contains(key);
	}
	
	public String getArg(String key) {
		return this.arguments.get(key);
	}
	
}
