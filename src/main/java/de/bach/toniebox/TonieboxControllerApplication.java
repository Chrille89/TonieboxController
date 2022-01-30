package de.bach.toniebox;

import de.bach.toniebox.ytdlp.*;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rocks.voss.toniebox.TonieHandler;
import rocks.voss.toniebox.beans.toniebox.Chapter;
import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;
import rocks.voss.toniebox.beans.toniebox.Me;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class TonieboxControllerApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(TonieboxControllerApplication.class, args);

		String search = "benjamin blümchen bauernhof";
		String directory = "./tmp";

		YoutubeDLRequest request = new YoutubeDLRequest(search, directory);
		request.setOption("extract-audio");
		request.setOption("audio-format","mp3");
		request.setOption("default-search","ytsearch");
		request.setOption("no-playlist");

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

		/*
		TonieHandler tonieHandler = new TonieHandler();
		tonieHandler.login(Constants.USERNAME, Constants.PASSWORD);

		// get what is stored about you as a person
		Me me = tonieHandler.getMe();
		System.out.println(me);

		// get all households you're in & select first one
		List<Household> households = tonieHandler.getHouseholds();
		Household household = households.get(0);
		System.out.println(household.getId());

		// get all creative tonies & select first one
		List<CreativeTonie> creativeTonies = tonieHandler.getCreativeTonies(household);
		CreativeTonie creativeTonie = creativeTonies.get(0);
		System.out.println(creativeTonie.getChapters());

		for(Chapter c : creativeTonie.getChapters()){
			System.out.println(c.getTitle());
		}
		tonieHandler.disconnect();*/

	}

}
