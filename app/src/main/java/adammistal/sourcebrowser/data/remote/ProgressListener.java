package adammistal.sourcebrowser.data.remote;


public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
