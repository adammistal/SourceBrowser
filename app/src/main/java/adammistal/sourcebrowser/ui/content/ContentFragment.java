package adammistal.sourcebrowser.ui.content;

import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import adammistal.sourcebrowser.R;
import adammistal.sourcebrowser.data.remote.models.ProgressInfo;
import adammistal.sourcebrowser.databinding.ContentRootBinding;
import adammistal.sourcebrowser.rx.SimpleDisposableObserver;
import adammistal.sourcebrowser.ui.base.MvvmFragment;


public class ContentFragment extends MvvmFragment<ContentRootBinding, ContentViewModel> {

    public static Fragment newInstance() {
        return new ContentFragment();
    }

    @Override
    protected void bindToViewModel(ContentViewModel viewModel, ContentRootBinding binding) {
        initWebView(binding.contentWebView);
        binding.setViewModel(viewModel);
        subscribe(viewModel.getState(), new SimpleDisposableObserver<ContentViewModel.State>() {
            @Override
            public void onNext(ContentViewModel.State state) {
                super.onNext(state);
                switch (state) {

                    case LOADING:
                        getBinding().urlEd.setError(null);
                        getBinding().contentWebView.loadData("", "text/plain", "UTF-8");
                        getBinding().progressPb.setProgress(0);
                        getBinding().downloadBtn.setEnabled(false);
                        getBinding().urlEd.setEnabled(false);
                        break;
                    case ERROR:
                        getBinding().urlEd.setError(getString(R.string.error_text));
                        break;
                }
            }
        });
        subscribe(viewModel.getProgress(), new SimpleDisposableObserver<ProgressInfo>() {
            @Override
            public void onNext(ProgressInfo progress) {
                super.onNext(progress);
                if (progress.getTotal() > 0) {
                    getBinding().progressPb.setProgress((int) ((progress.getDownloaded() / (progress.getTotal() * 1f)) * 100));
                    getBinding().progressLabelTv.setText(String.format(getString(R.string.downloading_form_internet_full), progress.getDownloaded(), progress.getTotal()));
                    if (getBinding().progressPb.getVisibility() != View.VISIBLE) {
                        getBinding().progressPb.setVisibility(View.VISIBLE);
                    }
                } else {
                    getBinding().progressLabelTv.setText(String.format(getString(R.string.downloading_form_internet), progress.getDownloaded()));
                    if (getBinding().progressPb.getVisibility() != View.INVISIBLE) {
                        getBinding().progressPb.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        subscribe(viewModel.getContent(), new SimpleDisposableObserver<String>() {
            @Override
            public void onNext(String content) {
                super.onNext(content);
                getBinding().contentWebView.loadData(content, "text/plain", "UTF-8");
            }
        });
        subscribe(viewModel.getMessage(), new SimpleDisposableObserver<String>() {
            @Override
            public void onNext(String message) {
                super.onNext(message);
                toast(message);
            }
        });
    }

    private void initWebView(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                updateWebViewProgress(newProgress);
            }
        });
    }

    private void updateWebViewProgress(int newProgress) {
        if (getBinding().progressPb.getVisibility() != View.VISIBLE) {
            getBinding().progressPb.setVisibility(View.VISIBLE);
        }
        getBinding().progressPb.setProgress(newProgress);
        getBinding().progressLabelTv.setText(String.format(getString(R.string.load_to_web_view),newProgress));
        if (newProgress == 100) {
            getBinding().downloadBtn.setEnabled(true);
            getBinding().urlEd.setEnabled(true);
            getBinding().progressLabelTv.setText("");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.content;
    }

    @Override
    protected void clearViewData(ContentRootBinding binding) {
        binding.contentWebView.setWebChromeClient(null);
        binding.setViewModel(null);
    }
}
