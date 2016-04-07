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
import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;

public class AddComputerActivity extends BaseActivity {

    private EditText mComputerEt;
    private Button mAddComputerBtn;
    private ProgressBar mProgress;

    @Override
    protected void initializeView() {
        toolbar.setTitle(getString(R.string.add_computer_page_title));

        mComputerEt = (EditText) findViewById(R.id.add_computer_computer_name);
        mAddComputerBtn = (Button) findViewById(R.id.add_computer_btn);
        mProgress = (ProgressBar) findViewById(R.id.add_computer_progress);

        mComputerEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.add_computer || id == EditorInfo.IME_NULL) {
                    addComputer();
                    return true;
                }
                return false;
            }
        });

        mAddComputerBtn.setOnClickListener(addComputerListener);
    }

    private View.OnClickListener addComputerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addComputer();
        }
    };

    private void addComputer() {
        if(mComputerEt.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }
        else {
            hideSoftKeyboard();
            toggleProgress(true);
            Computer computerToAdd = new Computer();
            computerToAdd.setComputerName(mComputerEt.getText().toString());
            computerToAdd.setDepartmentName(Session.currentDepartment);
            vaultApiClient.addComputer(computerToAdd, new VaultApiClient.OnCallCompleted() {
                @Override
                public void onSuccess() {
                    vaultApiClient.refreshUser(new VaultApiClient.OnCallCompleted() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), "Your computer was added successfully!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "There was an error adding your computer.", Toast.LENGTH_LONG).show();
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
        mAddComputerBtn.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mComputerEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int activityToInflate() {
        return R.layout.activity_add_computer;
    }
}
