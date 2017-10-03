package adammistal.sourcebrowser.data.remote.interceptors;


import android.support.annotation.NonNull;

import java.io.IOException;

import adammistal.sourcebrowser.data.remote.ProgressListener;
import adammistal.sourcebrowser.data.remote.models.ProgressResponseBody;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ProgressInterceptor implements Interceptor {
    private ProgressListener progressListener;

    public void setProgressListener(ProgressListener progressListener){
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                .build();
    }
}
