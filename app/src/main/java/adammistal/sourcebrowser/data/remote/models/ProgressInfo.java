package adammistal.sourcebrowser.data.remote.models;


public class ProgressInfo {
    private long downloaded;
    private long total;

    public ProgressInfo(long downloaded, long total) {
        this.downloaded = downloaded;
        this.total = total;
    }

    public long getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(long downloaded) {
        this.downloaded = downloaded;
    }

    public long getTotal() {
        return total;
    }
}
