# Spotifree
Download songs, playlist, albums and recommended songs for free.

## Info
Spotifree downloads songs from spotify by searching the same song on youtube. After downloading and converting the song, it adds an ID3v2.4 tag with some more info about the song as well as the lyrics and a cover.

## Usage
In a terminal enter: `java -jar spotifree.jar --url <url>`.

### possible arguments
* `-lrc` Save timed lyrics in a lrc file, it only works when the youtube video has subtitles.
* `-recom` Create a recommended playlist (Requires a playlist url).
   * `--max <amount>` The amount of songs to download.
   * `--group <amount>` Spotifree picks max 5 random songs from your playlist, how many songs do you want him to recommend per 5?