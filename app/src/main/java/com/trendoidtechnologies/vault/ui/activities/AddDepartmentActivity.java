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
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.datacontract.User;
import com.trendoidtechnologies.vault.session.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;
import com.trendoidtechnologies.vault.ui.activities.base.BaseActivity;

public class AddDepartmentActivity extends BaseActivity {

    private EditText mDepartmentNameEt;
    private Button mAddDepartmentBtn;
    private ProgressBar mProgress;

    @Override
    protected void initializeView() {
        toolbar.setTitle(getString(R.string.add_department_page_title));
        mDepartmentNameEt = (EditText) findViewById(R.id.add_department_et);
        mAddDepartmentBtn = (Button) findViewById(R.id.add_department_btn);
        mProgress = (ProgressBar) findViewById(R.id.add_department_progress);

        mDepartmentNameEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.add_department || id == EditorInfo.IME_NULL) {
                    addDepartment();
                    return true;
                }
                return false;
            }
        });

        mAddDepartmentBtn.setOnClickListener(addDepartmentListener);
    }



    private View.OnClickListener addDepartmentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addDepartment();
        }
    };

    private void addDepartment() {
        if(mDepartmentNameEt.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
        }
        else {
            hideSoftKeyboard();
            toggleProgress(true);
            final Permission departmentToAdd = new Permission();
            departmentToAdd.setDepartmentName(mDepartmentNameEt.getText().toString());
            vaultApiClient.addDepartment(departmentToAdd, new VaultApiClient.OnCallCompleted() {
                @Override
                public void onSuccess() {
                    User localUpdatedUser = Session.user;
                    localUpdatedUser.getPermissions().add(departmentToAdd);
                    vaultApiClient.updateUser(localUpdatedUser, new VaultApiClient.OnCallCompleted() {
                        @Override
                        public void onSuccess() {
                            vaultApiClient.refreshUser(new VaultApiClient.OnCallCompleted() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(getApplicationContext(), "Your Department was added successfully!", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                    toggleProgress(false);
                                }

                                @Override
                                public void onUnSuccess() {
                                    toggleProgress(false);
                                    Toast.makeText(getApplicationContext(), "There was an error adding your Department.", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure() {
                                    toggleProgress(false);
                                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onUnSuccess() {
                            toggleProgress(false);
                            Toast.makeText(getApplicationContext(), "There was an error adding your Department.", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure() {
                            toggleProgress(false);
                            Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onUnSuccess() {
                    toggleProgress(false);
                    Toast.makeText(getApplicationContext(), "There was an error adding your Department.", Toast.LENGTH_LONG).show();
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
        mDepartmentNameEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mAddDepartmentBtn.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int activityToInflate() {
        return R.layout.activity_add_department;
    }
}
