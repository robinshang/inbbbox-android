package co.netguru.android.inbbbox.feature.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseActivity;
import co.netguru.android.inbbbox.feature.main.MainActivity;

public class LoginActivity extends BaseActivity {

    public static void startActivity(Context context) {
        final Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClick() {
        // TODO: 13.10.2016 Need to be changed
        MainActivity.startActivity(this);
        finish();
    }
}
