package adammistal.sourcebrowser.injection.module;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import adammistal.sourcebrowser.injection.ViewModelKey;
import adammistal.sourcebrowser.ui.base.ViewModelFactory;
import adammistal.sourcebrowser.ui.content.ContentViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
abstract public class ViewModelsModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(ContentViewModel.class)
    abstract ViewModel bindContentViewModel(ContentViewModel viewModel);
}
