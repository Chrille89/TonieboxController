package de.bach.toniebox;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import rocks.voss.toniebox.TonieHandler;
import rocks.voss.toniebox.beans.toniebox.Chapter;
import rocks.voss.toniebox.beans.toniebox.CreativeTonie;
import rocks.voss.toniebox.beans.toniebox.Household;
import rocks.voss.toniebox.beans.toniebox.Me;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class TonieboxController {

    @CrossOrigin
    @GetMapping("/getAllChapters")
    public List<Chapter> getAllChapters() {
        TonieHandler tonieHandler = new TonieHandler();
        Me me = null;
        try {
            tonieHandler.login(Constants.USERNAME, Constants.PASSWORD);
            me = tonieHandler.getMe();

            // get all households you're in & select first one
            List<Household> households = tonieHandler.getHouseholds();
            Household household = households.get(0);
            System.out.println(household.getId());

            // get all creative tonies & select first one
            List<CreativeTonie> creativeTonies = tonieHandler.getCreativeTonies(household);
            CreativeTonie creativeTonie = creativeTonies.get(0);
            tonieHandler.disconnect();
            return Arrays.asList(creativeTonie.getChapters());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
