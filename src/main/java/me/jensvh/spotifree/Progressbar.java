package me.jensvh.spotifree;

public class Progressbar {

	private final String prefix;
	private int current;
	private final int max;
	private String suffix;
	
	public Progressbar(String prefix, int max) {
		this.max = max;
		this.current = 0;
		this.prefix = prefix;
		this.suffix = "Loading";
	}
	
	public void step() {
		this.current++;
		print();
	}
	
	public void step(String suffix) {
		this.current++;
		this.suffix = suffix;
		print();
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
		print();
	}
	
	private int greatest_length = 0;
	private void print() {
		// Example: Prefix 25% 5/20 Downloading Boulevard of broken dreams;
		String format;
		int percent = 100 * current / max;
		String message = String.format("%s %d%% %d/%d %s", prefix, percent, current, max, suffix);
		if (message.length() >= greatest_length) {
			greatest_length = message.length();
			format = "%s\r";
		} else {
			int diff = greatest_length - message.length();
			format = "%s%"+diff+"s\r";
		}
        System.out.print(String.format(format, message, " "));
        if (current >= max) {
        	System.out.println("\ndone");
		}
	}
	
}
