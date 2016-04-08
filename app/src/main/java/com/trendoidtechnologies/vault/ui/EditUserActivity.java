package com.trendoidtechnologies.vault.ui;

import android.graphics.Color;
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
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;
import com.trendoidtechnologies.vault.ui.widgets.KeyPairBoolData;
import com.trendoidtechnologies.vault.ui.widgets.MultiSpinnerSearch;

import java.util.ArrayList;
import java.util.List;

public class EditUserActivity extends BaseActivity {

    private EditText mEmailEt;
    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private Button mUpdateUserBtn;
    private ProgressBar mProgress;
    private MultiSpinnerSearch permissionsSpinner;
    private List<KeyPairBoolData> listArray;

    @Override
    protected void initializeView() {
        toolbar.setTitle(getString(R.string.add_computer_page_title));

        permissionsSpinner = (MultiSpinnerSearch) findViewById(R.id.edit_user_permissions_spinner);
        mEmailEt = (EditText) findViewById(R.id.edit_user_email);
        mFirstNameEt = (EditText) findViewById(R.id.edit_user_first_name);
        mLastNameEt = (EditText) findViewById(R.id.edit_user_last_name);
        mProgress = (ProgressBar) findViewById(R.id.edit_user_progress);
        mUpdateUserBtn = (Button) findViewById(R.id.edit_user_btn);

        mLastNameEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.update_user || id == EditorInfo.IME_NULL) {
                    updateUser();
                    return true;
                }
                return false;
            }
        });

        mEmailEt.setText(Session.currentUser.getEmail());
        disableEditText(mEmailEt);
        mFirstNameEt.setText(Session.currentUser.getFirstName());
        mLastNameEt.setText(Session.currentUser.getLastName());

        mUpdateUserBtn.setOnClickListener(updateUserListener);

        listArray = new ArrayList<>();
        if (Session.user.getPermissions() != null && Session.user.getPermissions().size() > 0) {
            for (int i = 0; i < Session.user.getPermissions().size(); i++) {
                KeyPairBoolData keyPairBoolData = new KeyPairBoolData();
                keyPairBoolData.setId(i + 1);
                keyPairBoolData.setPermission(Session.user.getPermissions().get(i));
                keyPairBoolData.setSelected(Session.currentUser.hasPermission(keyPairBoolData.getPermission()));
                listArray.add(keyPairBoolData);
            }
        }

        /***
         * -1 is no by default selection
         * 0 to length will select corresponding values
         */
        String spinnerText = "Permissions";
        if(Session.currentUser.getPermissions() != null && Session.currentUser.getPermissions().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Permission permission : Session.currentUser.getPermissions()) {
                stringBuilder.append(permission.getDepartmentName()).append(", ");
            }
            spinnerText = stringBuilder.toString();
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        }
        permissionsSpinner.setItems(listArray, spinnerText, -1, new MultiSpinnerSearch.MultiSpinnerSearchListener() {
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

    private View.OnClickListener updateUserListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateUser();
        }
    };

    public boolean areAllFieldsFilled(String firstName, String lastName, String email) {
        return !firstName.equals("")
                && !lastName.equals("")
                && !email.equals("");
    }

    private void updateUser() {
        String firstName = mFirstNameEt.getText().toString();
        String lastName = mLastNameEt.getText().toString();
        String email = mEmailEt.getText().toString();
        if (!areAllFieldsFilled(firstName, lastName, email)) {
            Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
        } else if (!isEmailValid(email)) {
            Toast.makeText(getApplicationContext(), "Email is not valid", Toast.LENGTH_LONG).show();
        } else {
            hideSoftKeyboard();
            toggleProgress(true);
            final List<Permission> updatedPermissions = new ArrayList<>();
            if (listArray != null && listArray.size() > 0) {
                for (KeyPairBoolData keyPairBoolData : listArray) {
                    if (keyPairBoolData.isSelected()) {
                        updatedPermissions.add(keyPairBoolData.getPermission());
                    }
                }
            }
            final User userToUpdate = Session.currentUser;
            userToUpdate.setEmail(email);
            userToUpdate.setFirstName(firstName);
            userToUpdate.setLastName(lastName);
            userToUpdate.setPermissions(updatedPermissions);

            vaultApiClient.updateUser(userToUpdate, new VaultApiClient.OnCallCompleted() {
                @Override
                public void onSuccess() {
                    vaultApiClient.getAllUsers(new VaultApiClient.OnCallCompleted() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(), "User was updated successfully!", Toast.LENGTH_LONG).show();
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
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
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
        mFirstNameEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mLastNameEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mEmailEt.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mUpdateUserBtn.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        permissionsSpinner.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        mProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int activityToInflate() {
        return R.layout.activity_edit_user;
    }
}
