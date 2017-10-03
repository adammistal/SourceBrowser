package adammistal.sourcebrowser.injection.component;

import android.app.Application;

import javax.inject.Singleton;

import adammistal.sourcebrowser.SourceBrowserApplication;
import adammistal.sourcebrowser.injection.module.AppModule;
import adammistal.sourcebrowser.injection.module.DBModule;
import adammistal.sourcebrowser.injection.module.MainActivityModule;
import adammistal.sourcebrowser.injection.module.NetworkingModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        MainActivityModule.class,
        NetworkingModule.class,
        DBModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        Builder appModule(AppModule application);

        Builder dbModule(DBModule application);

        AppComponent build();
    }

    void inject(SourceBrowserApplication myApp);
}
