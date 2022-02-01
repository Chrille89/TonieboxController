package de.bach.toniebox;

import de.bach.toniebox.model.ChapterUpload;
import de.bach.toniebox.ytdlp.*;
import org.springframework.web.bind.annotation.*;
import rocks.voss.toniebox.TonieHandler;
import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class TonieboxController {

    @CrossOrigin
    @GetMapping("/getCreativeTonie")
    public CreativeTonie getCreativeTonie() {
        TonieHandler tonieHandler = new TonieHandler();
        CreativeTonie creativeTonie = null;
        try {
            tonieHandler.login(Constants.USERNAME, Constants.PASSWORD);

            // get all households you're in & select first one
            List<Household> households = tonieHandler.getHouseholds();
            Household household = households.get(0);

            // get all creative tonies & select first oneddd
            List<CreativeTonie> creativeTonies = tonieHandler.getCreativeTonies(household);
            creativeTonie = creativeTonies.get(0);

            tonieHandler.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return creativeTonie;
    }

    @CrossOrigin
    @PostMapping("/uploadChapterToCreativeTonie")
    public void uploadChapter(@RequestBody ChapterUpload chapterUpload) {
        YoutubeDLRequest request = new YoutubeDLRequest(chapterUpload.getChapterName(), "./tmp");
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
        } catch (YoutubeDLException e) {
            e.printStackTrace();
        }

        CreativeTonie creativeTonie = getCreativeTonie();
        try {
            File folder = new File("tmp");
            File chapterFile = folder.listFiles()[0];
            creativeTonie.uploadFile(chapterUpload.getChapterName(), chapterFile.getAbsolutePath());
            creativeTonie.commit();
            chapterFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/deleteChapterFromCreativeTonie/{chapterName}")
    public void deleteChapterFromCreativeTonie(@PathVariable String chapterName) {
        CreativeTonie creativeTonie = getCreativeTonie();
        try {
            creativeTonie.deleteChapter(creativeTonie.findChapterByTitle(chapterName));
            creativeTonie.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
