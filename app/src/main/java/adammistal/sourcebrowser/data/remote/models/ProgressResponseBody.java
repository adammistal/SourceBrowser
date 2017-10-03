package adammistal.sourcebrowser.data.remote.models;


import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import adammistal.sourcebrowser.data.remote.ProgressListener;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final ProgressListener progressListener;
    private BufferedSource bufferedSource;
    private final long timeToUpdateProgress = 8;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            long time = System.currentTimeMillis();

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Log.d("DOWNLOADING", "progress:" + totalBytesRead + " / " + responseBody.contentLength()+" "+progressListener);
                if (System.currentTimeMillis() - time >= timeToUpdateProgress) {
                    time = System.currentTimeMillis();
                    if (progressListener != null) {
                        progressListener.update(100 * totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    }
                }
                return bytesRead;
            }
        };
    }
}
