package de.bach.toniebox.ytdlp;

public interface DownloadProgressCallback {
    void onProgressUpdate(float progress, long etaInSeconds);
}
