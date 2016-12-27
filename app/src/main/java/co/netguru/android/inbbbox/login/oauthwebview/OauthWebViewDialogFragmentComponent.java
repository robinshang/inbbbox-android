package co.netguru.android.inbbbox.login.oauthwebview;


import co.netguru.android.commons.di.FragmentScope;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface OauthWebViewDialogFragmentComponent {
    void inject(OauthWebViewDialogFragment oauthWebViewDialogFragment);

    OauthWebViewDialogFragmentPresenter getOauthWebViewDialogFragmentPresenter();
}
