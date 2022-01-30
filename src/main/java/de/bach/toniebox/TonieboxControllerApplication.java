package de.bach.toniebox;

import de.bach.toniebox.ytdlp.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TonieboxControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TonieboxControllerApplication.class, args);

		// Video url to download
		String videoUrl = "https://www.youtube.com/watch?v=7ylCMWkaGzM";

		String search = "benjamin blümchen bauernhof";

// Destination directory
		String directory = "./tmp";

// Build request
		YoutubeDLRequest request = new YoutubeDLRequest(search, directory);
		request.setOption("extract-audio");
		request.setOption("audio-format","mp3");
		request.setOption("default-search","ytsearch");
		request.setOption("no-playlist");
		//   request.setOption("ignore-errors");		// --ignore-errors
		//    request.setOption("output", "%(id)s.%(ext)s");	// --output "%(id)s"
		//   request.setOption("retries", 10);		// --retries 10

// Make request and return response

		try {
			YoutubeDLResponse response = YoutubeDL.execute(request, new DownloadProgressCallback() {
				@Override
				public void onProgressUpdate(float progress, long etaInSeconds) {
					System.out.println(String.valueOf(progress) + "%");
				}
			});
			// Response
			String stdOut = response.getOut(); // Executable output

			System.out.println("Output: "+stdOut);
		} catch (YoutubeDLException e) {
			e.printStackTrace();
		}
	}

}
