package adammistal.sourcebrowser.injection.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import adammistal.sourcebrowser.data.remote.Downloader;
import adammistal.sourcebrowser.data.remote.interceptors.ProgressInterceptor;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class NetworkingModule {

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @Singleton
    public ProgressInterceptor provideProgressInterceptor() {
        return new ProgressInterceptor();
    }

    @Provides
    @Singleton
    public Downloader provideDownloader(OkHttpClient client, ProgressInterceptor progressInterceptor) {
        return new Downloader(client, progressInterceptor);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor, ProgressInterceptor progressInterceptor) {
        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(progressInterceptor)
                .build();
    }
}
