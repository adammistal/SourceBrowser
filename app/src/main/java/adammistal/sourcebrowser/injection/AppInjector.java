package adammistal.sourcebrowser.injection;


import adammistal.sourcebrowser.SourceBrowserApplication;
import adammistal.sourcebrowser.injection.component.DaggerAppComponent;
import adammistal.sourcebrowser.injection.module.AppModule;
import adammistal.sourcebrowser.injection.module.DBModule;


public class AppInjector {

    public static void init(SourceBrowserApplication myApp) {

        DaggerAppComponent.builder()
                .application(myApp)
                .appModule(new AppModule(myApp))
                .dbModule(new DBModule(myApp))
                .build().inject(myApp);

    }
}
