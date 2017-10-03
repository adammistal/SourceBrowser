package adammistal.sourcebrowser.ui.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;


public abstract class MvvmFragment<BINDING extends ViewDataBinding, VIEW_MODEL extends ViewModel> extends BaseFragment implements HasSupportFragmentInjector {

    @Inject
    protected DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    protected CompositeDisposable compositeDisposable;
    @Inject
    protected ViewModelProvider.Factory viewModelFactory;
    @Inject
    @Named("main")
    Scheduler scheduler;
    private BINDING binding;
    private VIEW_MODEL viewModel;

    public BINDING getBinding() {
        return binding;
    }

    public VIEW_MODEL getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        compositeDisposable = new CompositeDisposable();
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Class<VIEW_MODEL> clazz = (Class<VIEW_MODEL>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(clazz);
        bindToViewModel(viewModel, binding);
    }

    protected <T> void subscribe(Observable<T> observable, DisposableObserver<T> disposableObserver) {
        compositeDisposable.add(observable
                .observeOn(scheduler)
                .subscribeOn(scheduler)
                .subscribeWith(disposableObserver));
    }

    @Override
    public void onDestroyView() {
        compositeDisposable.dispose();
        clearViewData(binding);
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        viewModel = null;
        super.onDetach();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager != null) {
            List<Fragment> nestedFragments = childFragmentManager.getFragments();
            if (nestedFragments == null || nestedFragments.size() == 0) return;
            for (Fragment childFragment : nestedFragments) {
                if (childFragment != null && !childFragment.isDetached() && !childFragment.isRemoving()) {
                    childFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    protected abstract void bindToViewModel(VIEW_MODEL viewmodel, BINDING binding);

    protected abstract int getLayoutId();

    protected abstract void clearViewData(BINDING binding);
}
