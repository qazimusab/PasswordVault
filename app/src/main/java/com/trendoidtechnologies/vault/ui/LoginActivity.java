package com.trendoidtechnologies.vault.ui;

import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Token;
import com.trendoidtechnologies.vault.datacontract.User;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.service.VaultService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {


    private Button mEmailSignInButton;
    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressBar mProgressView;
    private LinearLayout linearLayout;

    @Override
    protected void initializeView() {
        setTitle(getString(R.string.login_page_title));
        mEmailView = (EditText) findViewById(R.id.email);
        mProgressView = (ProgressBar)findViewById(R.id.login_progress);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutLoginContainer);
        mPasswordView = (EditText) findViewById(R.id.password);
        Session.cacheAssets(getApplicationContext());
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(signInOnClickListener);
    }

    View.OnClickListener signInOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            login();
        }
    };

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        setDrawerState(false);
    }


    @Override
    protected int activityToInflate() {
        return R.layout.activity_login;
    }

    private void toggleProgress(boolean loading) {
        linearLayout.setVisibility(loading ? View.GONE : View.VISIBLE);
        mEmailSignInButton.setVisibility(loading ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void login(){
        if(mEmailView.getText().toString().equals("") || mPasswordView.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_LONG).show();
        }
        else if(!isEmailValid(mEmailView.getText().toString())){
            Toast.makeText(getApplicationContext(), "Your email is invalid.", Toast.LENGTH_LONG).show();
        }
        else if(!isPasswordValid(mPasswordView.getText().toString())){
            Toast.makeText(getApplicationContext(), "You email or password is incorrect.", Toast.LENGTH_LONG).show();
        }
        else {
            hideSoftKeyboard();
            toggleProgress(true);
            final ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(LoginActivity.this, mEmailSignInButton, "button");
            VaultService.Factory.getInstance(getApplicationContext()).getAuthToken(mEmailView.getText().toString(), mPasswordView.getText().toString(), "password").enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.isSuccessful()) {
                        Session.token = response.body();
                        VaultService.Factory.getInstance(getApplicationContext()).getUser("Bearer " + Session.token.getAccessToken(), mEmailView.getText().toString()).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    Session.user = response.body();
                                    if(Session.user.isAdmin()){
                                        VaultService.Factory.getInstance(getApplicationContext()).getAllUsers("Bearer " + Session.token.getAccessToken()).enqueue(new Callback<List<User>>() {
                                            @Override
                                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                                if(response.isSuccessful()) {
                                                    Session.allUsers = response.body();
                                                    navigateToActivity(DepartmentsActivity.class, options.toBundle(), true);
                                                    finish();
                                                }
                                                else {
                                                    toggleProgress(false);
                                                    Toast.makeText(getApplicationContext(), "You were successfully authenticated but then something went wrong. :(", Toast.LENGTH_LONG).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<User>> call, Throwable t) {
                                                Toast.makeText(getApplicationContext(), "You were successfully authenticated but then something went wrong. :(", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    else {
                                        toggleProgress(false);
                                        navigateToActivity(DepartmentsActivity.class, options.toBundle(), true);
                                        finish();
                                    }
                                } else {
                                    toggleProgress(false);
                                    Toast.makeText(getApplicationContext(), "You were successfully authenticated but then something went wrong. :(", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                toggleProgress(false);
                                Toast.makeText(getApplicationContext(), "You were successfully authenticated but then something went wrong. :(", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        toggleProgress(false);
                        Session.clearSession();
                        Toast.makeText(getApplicationContext(), "You email or password is incorrect.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    toggleProgress(false);
                    Session.clearSession();
                    Toast.makeText(getApplicationContext(), "You email or password is incorrect.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7 && !isAlphaNumeric(password) && !password.equals(password.toLowerCase());
    }
    
    private boolean isAlphaNumeric(String s){
        String pattern= "^[a-zA-Z0-9]*$";
        if(s.matches(pattern)){
            return true;
        }
        return false;
    }
}

