package com.trendoidtechnologies.vault.ui;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Credential;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;

public class AddCredentialActivity extends BaseActivity {

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private EditText mTypeEt;
    private Button mAddCredentialBtn;
    private ProgressBar mProgress;

    @Override
    protected void initializeView() {
        toolbar.setTitle(getString(R.string.add_computer_page_title));

        mUsernameEt = (EditText) findViewById(R.id.add_credential_username);
        mPasswordEt = (EditText) findViewById(R.id.add_credential_password);
        mTypeEt = (EditText) findViewById(R.id.add_credential_type);
        mAddCredentialBtn = (Button) findViewById(R.id.add_credential_btn);
        mProgress = (ProgressBar) findViewById(R.id.add_credential_progress);

        mTypeEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.add_credential || id == EditorInfo.IME_NULL) {
                    addCredential();
                    return true;
                }
                return false;
            }
        });

        mAddCredentialBtn.setOnClickListener(addComputerListener);
    }

    private View.OnClickListener addComputerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addCredential();
        }
    };

    private void addCredential() {
        if(mUsernameEt.getText().toString().equals("") || mPasswordEt.getText().toString().equals("") || mTypeEt.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }
        else {
            hideSoftKeyboard();
            toggleProgress(true);
            Credential credentialToAdd = new Credential(mUsernameEt.getText().toString(), mPasswordEt.getText().toString(), mTypeEt.getText().toString(), Session.currentComputer.getComputerId());
            vaultApiClient.addCredential(credentialToAdd, new VaultApiClient.OnCallCompleted() {
                @Override
                public void onSuccess() {
                    vaultApiClient.refreshUser(new VaultApiClient.OnCallCompleted() {
                        @Override
                        public void onSuccess() {
                            toggleProgress(false);
                            Toast.makeText(getApplicationContext(), "Your credential was added successfully!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }

                        @Override
                        public void onUnSuccess() {
                            toggleProgress(false);
                        }

                        @Override
                        public void onFailure() {
                            toggleProgress(false);
                        }
                    });
                }

                @Override
                public void onUnSuccess() {
                    toggleProgress(false);
                    Toast.makeText(getApplicationContext(), "There was an error adding your credential.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure() {
                    toggleProgress(false);
                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void toggleProgress(boolean isLoading) {
        mAddCredentialBtn.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mTypeEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mPasswordEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mUsernameEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int activityToInflate() {
        return R.layout.activity_add_credential;
    }
}
