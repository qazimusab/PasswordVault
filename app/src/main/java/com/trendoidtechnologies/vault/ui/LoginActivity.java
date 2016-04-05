package com.trendoidtechnologies.vault.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Token;
import com.trendoidtechnologies.vault.datacontract.User;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.service.VaultService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {


    // UI references.
    Button mEmailSignInButton;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;

    @Override
    protected void initializeView() {
        setTitle(getString(R.string.login_page_title));
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mProgressView = findViewById(R.id.login_progress);

        mPasswordView = (EditText) findViewById(R.id.password);
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


    private void login(){
        final Intent intent = new Intent(LoginActivity.this, DepartmentsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        final ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(LoginActivity.this, mEmailSignInButton, "button");
        VaultService.Factory.getInstance(getApplicationContext()).getAuthToken(mEmailView.getText().toString(), mPasswordView.getText().toString(), "password").enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if(response.isSuccessful()) {
                    Session.token = response.body();
                    VaultService.Factory.getInstance(getApplicationContext()).getUser("Bearer " + Session.token.getAccessToken(), mEmailView.getText().toString()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()) {
                                Session.user = response.body();
                                startActivity(intent, options.toBundle());
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "You were successfully authenticated by then something went wrong. :(", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "You were successfully authenticated by then something went wrong. :(", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    Session.token = null;
                    Toast.makeText(getApplicationContext(), "You username or password is incorrect.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Session.token = null;
                Toast.makeText(getApplicationContext(), "You username or password is incorrect.", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        String special = "!@#$%^&*()_";
        String pattern = ".*[" + Pattern.quote(special) + "].*";
        return password.length() > 7 && password.matches(pattern) && !password.equals(password.toLowerCase());
    }
}

