package me.jensvh.spotifree.ytdl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jensvh.spotifree.utils.Console;
import me.jensvh.spotifree.utils.Error;

public class Program {
	
	private List<String> args;
	
	public Program(String name) {
		this.args = new ArrayList<String>();
		this.args.add(name);
	}
	
	public String execute() {
		try {
			Process process = new ProcessBuilder(args.stream().toArray(String[]::new)).start();
			
			StringBuffer stdOut = new StringBuffer();
			StringBuffer stdErr = new StringBuffer();
			
			BufferedReader outReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			
			String line;
			while ((line = outReader.readLine()) != null) {
				stdOut.append(line + "\n");
			}
			
			while ((line = errReader.readLine()) != null) {
				stdErr.append(line + "\n");
			}
			
			int exitCode = process.waitFor();
			
			if (exitCode != 0) {
				
				Console.errPrint("Command: " + Arrays.toString(args.stream().toArray(String[]::new)));
				Console.errPrint(stdErr.toString());
				throw new Error(exitCode, new IOException(), "The bridge to youtube-dl is gone");
			}
			
			return stdOut.toString();
		} catch (IOException | InterruptedException e) {
			throw new Error(6, e, "The bridge to youtube-dl is gone.");
		}
	}
	
	public Program addArgument(String arg) {
		this.args.add(arg);
		return this;
	}
	
	public Program addArgument(String key, String value) {
		this.args.add(key);
		this.args.add("\"" + value + "\"");
		return this;
	}
	
}
