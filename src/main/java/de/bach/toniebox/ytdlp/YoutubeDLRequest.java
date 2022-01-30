package de.bach.toniebox.ytdlp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * YoutubeDL request
 */
public class YoutubeDLRequest {

    /**
     * Executable working directory
     */
    private String directory;

    /**
     * Video Url
     */
    private String searchStr;

    /**
     * List of executable options
     */
    private Map<String, String> options = new HashMap<String, String>();

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public Map<String, String> getOption() {
        return options;
    }

    public void setOption(String key) {
        options.put(key, null);
    }

    public void setOption(String key, String value) {
        options.put(key, value);
    }

    public void setOption(String key, int value) {
        options.put(key, String.valueOf(value));
    }

    /**
     * Constructor
     */
    public YoutubeDLRequest() {

    }

    /**
     * Construct a request with a videoUrl
     * @param searchStr
     */
    public YoutubeDLRequest(String searchStr) {
        this.searchStr = searchStr;
    }

    /**
     * Construct a request with a videoUrl and working directory
     * @param searchStr
     * @param directory
     */
    public YoutubeDLRequest(String searchStr, String directory) {
        this.searchStr = searchStr;
        this.directory = directory;
    }

    /**
     * Transform options to a string that the executable will execute
     * @return Command string
     */
    protected String buildOptions() {

        StringBuilder builder = new StringBuilder();

        // Set Url
        if(this.searchStr != null)
            builder.append(this.searchStr.replaceAll(" ","")+"\" ");

        // Build options strings
        Iterator it = options.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry option = (Map.Entry) it.next();

            String name = (String) option.getKey();
            String value = (String) option.getValue();

            if(value == null) value = "";

            String optionFormatted = String.format("--%s %s", name, value).trim();
            builder.append(optionFormatted + " ");

            it.remove();
        }
        builder.append("-f bestaudio");
        return builder.toString().trim();
    }
}
