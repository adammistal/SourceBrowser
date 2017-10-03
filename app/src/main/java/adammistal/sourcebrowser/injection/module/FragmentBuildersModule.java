package adammistal.sourcebrowser.injection.module;

import adammistal.sourcebrowser.ui.content.ContentFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ContentFragment contributeContentFragment();
}
