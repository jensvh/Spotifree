package me.jensvh.spotifree.utils;

public class Console {
	
	private static int greatest_length = 0;
	
	public static void print(String line) {
		int spaces = Math.max(greatest_length - line.length(), 1);
		System.out.print(line + String.format("%" + spaces + "s", " ") + "\r");
		if (line.length() > greatest_length) {
			greatest_length = line.length();
		}
	}
	
	public static void println(String line) {
		System.out.println("\n" + line);
	}
	
	public static void errPrint(String line) {
		System.err.println("\n" + line);
	}
	
}
