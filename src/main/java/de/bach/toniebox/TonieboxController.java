package de.bach.toniebox;

import de.bach.toniebox.model.ChapterUpload;
import de.bach.toniebox.ytdlp.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import rocks.voss.toniebox.TonieHandler;
import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class TonieboxController {

    @CrossOrigin
    @GetMapping("/getCreativeTonie")
    public CreativeTonie getCreativeTonie(@RequestHeader("Authorization") String auth) {
        System.out.println("getCreativeTonie()");
        String email = auth.split(":")[0];
        String password = auth.split(":")[1];
        TonieHandler tonieHandler = new TonieHandler();
        CreativeTonie creativeTonie = null;
        try {
            tonieHandler.login(email, password);

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
    public void uploadChapter(
            @RequestHeader("Authorization") String auth,
            @RequestBody ChapterUpload chapterUpload,
            HttpServletResponse response) {
        System.out.println("uploadChapter()");
        try {
            Resource resource = new ClassPathResource("chapter/dummy.txt");
            InputStream input = resource.getInputStream();
            File file = resource.getFile();
            File folder =  file.getParentFile();

            YoutubeDLRequest request = new YoutubeDLRequest(chapterUpload.getChapterName(), folder.getAbsolutePath());
            request.setOption("extract-audio");
            request.setOption("audio-format", "mp3");
            request.setOption("default-search", "ytsearch");
            request.setOption("no-playlist");
            YoutubeDL.execute(request);

            if(folder.listFiles().length == 1) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }

            File chapterFile = folder.listFiles()[1];
            CreativeTonie creativeTonie = getCreativeTonie(auth);
            creativeTonie.uploadFile(chapterUpload.getChapterName(), chapterFile.getAbsolutePath());
            creativeTonie.commit();
            chapterFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (YoutubeDLException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @DeleteMapping("/deleteChapterFromCreativeTonie/{chapterName}")
    public void deleteChapterFromCreativeTonie(@RequestHeader("Authorization") String auth, @PathVariable String chapterName) {
        System.out.println("deleteChapterFromCreativeTonie()");
        CreativeTonie creativeTonie = getCreativeTonie(auth);
        try {
            creativeTonie.deleteChapter(creativeTonie.findChapterByTitle(chapterName));
            creativeTonie.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
