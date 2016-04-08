package com.trendoidtechnologies.vault.ui.activities;

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
import com.trendoidtechnologies.vault.session.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;
import com.trendoidtechnologies.vault.ui.activities.base.BaseActivity;

public class EditCredentialActivity extends BaseActivity {

    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private EditText mTypeEt;
    private Button mUpdateCredentialBtn;
    private ProgressBar mProgress;

    @Override
    protected void initializeView() {
        toolbar.setTitle(getString(R.string.edit_credential_page_title));

        mUsernameEt = (EditText) findViewById(R.id.edit_credential_username);
        mPasswordEt = (EditText) findViewById(R.id.edit_credential_password);
        mTypeEt = (EditText) findViewById(R.id.edit_credential_type);
        mUpdateCredentialBtn = (Button) findViewById(R.id.edit_credential_btn);
        mProgress = (ProgressBar) findViewById(R.id.edit_credential_progress);

        mTypeEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.edit_credential || id == EditorInfo.IME_NULL) {
                    updateCredential();
                    return true;
                }
                return false;
            }
        });

        mUpdateCredentialBtn.setOnClickListener(updateCredentialListener);

        mUsernameEt.setText(Session.currentCredential.getUserName());
        mPasswordEt.setText(Session.currentCredential.getPassword());
        mTypeEt.setText(Session.currentCredential.getType());
    }

    private View.OnClickListener updateCredentialListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateCredential();
        }
    };

    private void updateCredential() {
        if(mUsernameEt.getText().toString().equals("") || mPasswordEt.getText().toString().equals("") || mTypeEt.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }
        else {
            hideSoftKeyboard();
            toggleProgress(true);
            Credential credentialToUpdate = new Credential(mUsernameEt.getText().toString(), mPasswordEt.getText().toString(), mTypeEt.getText().toString(), Session.currentComputer.getComputerId());
            credentialToUpdate.setId(Session.currentCredential.getId());
            vaultApiClient.updateCredential(credentialToUpdate, new VaultApiClient.OnCallCompleted() {
                @Override
                public void onSuccess() {
                    vaultApiClient.refreshUser(new VaultApiClient.OnCallCompleted() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), "Your credential was added successfully!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                            toggleProgress(false);
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
        mUpdateCredentialBtn.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mTypeEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mPasswordEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mUsernameEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int activityToInflate() {
        return R.layout.activity_edit_credential;
    }
}
