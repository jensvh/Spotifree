package me.jensvh.spotifree.ytdl;

import java.io.File;

import me.jensvh.spotifree.Main;
import me.jensvh.spotifree.utils.FileUtils;
import me.jensvh.spotifree.utils.Utils;

public class YtDlApi {
	
	// Command: youtube-dl.exe --sub-lang en --write-sub --convert-subs lrc -x -f bestaudio --audio-format mp3 --audio-quality 0 --add-metadata --id "https://www.youtube.com/watch?v=CJzaYLc4pPY"
	
	private static final String youtubedl_file = "youtube-dl.exe";
	private static final String ffmpeg_file = "ffmpeg.exe";
	
	public static void unpack() {
		FileUtils.unpack(youtubedl_file);
		FileUtils.unpack(ffmpeg_file);
	}
	
	public static void update() {
		new Program(youtubedl_file)
				.addArgument("--update")
				.execute();
	}
	
	public static File downloadMp3(String id, String folder) {
		String file_name = ((folder != null) ? Utils.removeInvalidPathChars(folder) + File.separator : "") + Utils.removeInvalidPathChars(id);
		Program program = new Program(youtubedl_file)
				.addArgument("-x") // Only audio
				.addArgument("--audio-format", "mp3") // download mp3
				.addArgument("--audio-quality", "0") // FFmpeg best quality
				.addArgument("-f", "bestaudio") // download best audio
				.addArgument("-o", file_name + ".%(ext)s")
				.addArgument("\"https://www.youtube.com/watch?v=" + id + "\"");
		if (Main.downloadLrc) {
			program.addArgument("--write-sub")
					.addArgument("--convert-subs", "lrc")
					.addArgument("--sub-lang", "en");
		}
		program.execute();
		return new File(file_name + ".mp3");
	}
	
}
