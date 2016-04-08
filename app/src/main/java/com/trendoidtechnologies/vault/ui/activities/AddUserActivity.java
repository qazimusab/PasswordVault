package com.trendoidtechnologies.vault.ui.activities;

import android.util.Log;
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
import com.trendoidtechnologies.vault.ui.widgets.KeyPairBoolData;
import com.trendoidtechnologies.vault.ui.widgets.MultiSpinnerSearch;

import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends BaseActivity {

    private EditText mEmailEt;
    private EditText mPasswordEt;
    private EditText mConfirmPasswordEt;
    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private Button mAddUserBtn;
    private ProgressBar mProgress;
    private MultiSpinnerSearch permissionsSpinner;
    private List<KeyPairBoolData> listArray;

    @Override
    protected void initializeView() {
        toolbar.setTitle(getString(R.string.add_computer_page_title));

        permissionsSpinner = (MultiSpinnerSearch) findViewById(R.id.add_user_permissions_spinner);
        mEmailEt = (EditText) findViewById(R.id.add_user_email);
        mFirstNameEt = (EditText) findViewById(R.id.add_user_first_name);
        mLastNameEt = (EditText) findViewById(R.id.add_user_last_name);
        mPasswordEt = (EditText) findViewById(R.id.add_user_password);
        mConfirmPasswordEt = (EditText) findViewById(R.id.add_user_confirm_password);
        mProgress = (ProgressBar) findViewById(R.id.add_user_progress);
        mAddUserBtn = (Button) findViewById(R.id.add_user_btn);

        mConfirmPasswordEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.add_user || id == EditorInfo.IME_NULL) {
                    addUser();
                    return true;
                }
                return false;
            }
        });

        mAddUserBtn.setOnClickListener(addUserListener);

        listArray = new ArrayList<>();
        if (Session.user.getPermissions() != null && Session.user.getPermissions().size() > 0) {
            for (int i = 0; i < Session.user.getPermissions().size(); i++) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                keyPairBoolData.setId(i + 1);
                keyPairBoolData.setPermission(Session.user.getPermissions().get(i));
                keyPairBoolData.setSelected(false);
                listArray.add(keyPairBoolData);
            }
        }

        /***
         * -1 is no by default selection
         * 0 to length will select corresponding values
         */
        permissionsSpinner.setItems(listArray, "Permissions", -1, new MultiSpinnerSearch.MultiSpinnerSearchListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {

                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        Log.i("TAG", i + " : " + items.get(i).getPermission().getDepartmentName() + " : " + items.get(i).isSelected());
                    }
                }
            }
        });

    }

    private View.OnClickListener addUserListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addUser();
        }
    };

    public boolean areAllFieldsFilled(String firstName, String lastName, String email, String password, String confirmPassword) {
        return !firstName.equals("")
                && !lastName.equals("")
                && !email.equals("")
                && !password.equals("")
                && !confirmPassword.equals("");
    }

    private void addUser() {
        String firstName = mFirstNameEt.getText().toString();
        String lastName = mLastNameEt.getText().toString();
        String email = mEmailEt.getText().toString();
        String password = mPasswordEt.getText().toString();
        String confirmPassword = mConfirmPasswordEt.getText().toString();
        if (!areAllFieldsFilled(firstName, lastName, email, password, confirmPassword)) {
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
        } else if (!isEmailValid(email)) {
            Toast.makeText(getApplicationContext(), "Email is not valid", Toast.LENGTH_LONG).show();
        } else if (!isPasswordValid(password)) {
            Toast.makeText(getApplicationContext(), "Password must contain at least 8 characters, one capital letter, and one special character.", Toast.LENGTH_LONG).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_LONG).show();
        } else if (isEmailTaken(email)) {
            Toast.makeText(getApplicationContext(), "There is already an account associated with that email address.", Toast.LENGTH_LONG).show();
        } else {
            hideSoftKeyboard();
            toggleProgress(true);
            final List<Permission> permissionsToGive = new ArrayList<>();
            if (listArray != null && listArray.size() > 0) {
                for (KeyPairBoolData keyPairBoolData : listArray) {
                    if (keyPairBoolData.isSelected()) {
                        permissionsToGive.add(keyPairBoolData.getPermission());
                    }
                }
            }
            final User userToAdd = new User(firstName, lastName, password, confirmPassword, email, false);
            vaultApiClient.addUser(userToAdd, new VaultApiClient.OnCallCompleted() {
                @Override
                public void onSuccess() {
                    vaultApiClient.getAllUsers(new VaultApiClient.OnCallCompleted() {
                        @Override
                        public void onSuccess() {
                            if(permissionsToGive.size() > 0) {
                                User registeredUser = null;
                                for(User user : Session.allUsers) {
                                    if(user.getEmail().equals(userToAdd.getEmail())) {
                                        registeredUser = user;
                                    }
                                }
                                registeredUser.setPermissions(permissionsToGive);
                                vaultApiClient.updateUser(registeredUser, new VaultApiClient.OnCallCompleted() {
                                    @Override
                                    public void onSuccess() {
                                        vaultApiClient.getAllUsers(new VaultApiClient.OnCallCompleted() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(getApplicationContext(), "User was added successfully!", Toast.LENGTH_LONG).show();
                                                onBackPressed();
                                                toggleProgress(false);
                                            }

                                            @Override
                                            public void onUnSuccess() {

                                            }

                                            @Override
                                            public void onFailure() {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onUnSuccess() {

                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                });
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "User was added successfully!", Toast.LENGTH_LONG).show();
                                onBackPressed();
                                toggleProgress(false);
                            }
                        }

                        @Override
                        public void onUnSuccess() {
                            Toast.makeText(getApplicationContext(), "There was an error adding the user.", Toast.LENGTH_LONG).show();
                            toggleProgress(false);
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                            toggleProgress(false);
                        }
                    });

                }

                @Override
                public void onUnSuccess() {
                    toggleProgress(false);
                    Toast.makeText(getApplicationContext(), "There was an error adding the user.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure() {
                    toggleProgress(false);
                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean isEmailTaken(String email) {
        boolean isEmailTaken = false;
        for (User user : Session.allUsers) {
            if (email.equals(user.getEmail())) {
                isEmailTaken = true;
            }
        }
        return isEmailTaken;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7 && !isAlphaNumeric(password) && !password.equals(password.toLowerCase());
    }

    private boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        if (s.matches(pattern)) {
            return true;
        }
        return false;
    }

    public void toggleProgress(boolean isLoading) {
        mEmailEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mFirstNameEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mLastNameEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mPasswordEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mConfirmPasswordEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mAddUserBtn.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        permissionsSpinner.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int activityToInflate() {
        return R.layout.activity_add_user;
    }
}
