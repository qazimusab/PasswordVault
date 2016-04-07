package com.trendoidtechnologies.vault.service;

import android.content.Context;

import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.datacontract.Credential;
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.datacontract.Token;
import com.trendoidtechnologies.vault.datacontract.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qazimusab on 4/5/16.
 */
public class VaultApiClient {

    private VaultService vaultService;

    public VaultApiClient(Context context) {
        vaultService = VaultService.Factory.getInstance(context);
    }

    public void getAuthToken(String username, String password) {
        vaultService.getAuthToken(username, password, "password").enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Session.token = response.body();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Session.token = null;
            }
        });
    }

    public void addComputer(Computer computer, final OnCallCompleted onCallCompleted) {
        vaultService.addComputer("Bearer " + Session.token.getAccessToken(), computer).enqueue(new Callback<Computer>() {
            @Override
            public void onResponse(Call<Computer> call, Response<Computer> response) {
                if(response.isSuccessful()){
                    onCallCompleted.onSuccess();
                }
                else {
                    onCallCompleted.onUnSuccess();
                }
            }

            @Override
            public void onFailure(Call<Computer> call, Throwable t) {
                onCallCompleted.onFailure();
            }
        });
    }

    public void addDepartment(Permission department, final OnCallCompleted onCallCompleted) {
        vaultService.addDepartment("Bearer " + Session.token.getAccessToken(), department).enqueue(new Callback<Permission>() {
            @Override
            public void onResponse(Call<Permission> call, Response<Permission> response) {
                if(response.isSuccessful()){
                    onCallCompleted.onSuccess();
                }
                else {
                    onCallCompleted.onUnSuccess();
                }
            }

            @Override
            public void onFailure(Call<Permission> call, Throwable t) {
                onCallCompleted.onFailure();
            }
        });
    }

    public void addCredential(Credential credential, final OnCallCompleted onCallCompleted) {
        vaultService.addCredential("Bearer " + Session.token.getAccessToken(), credential).enqueue(new Callback<Credential>() {
            @Override
            public void onResponse(Call<Credential> call, Response<Credential> response) {
                if(response.isSuccessful()){
                    onCallCompleted.onSuccess();
                }
                else {
                    onCallCompleted.onUnSuccess();
                }
            }

            @Override
            public void onFailure(Call<Credential> call, Throwable t) {
                onCallCompleted.onFailure();
            }
        });
    }

    public void addUser(User user, final OnCallCompleted onCallCompleted) {
        user.setIsAdmin(false);
        vaultService.addUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    onCallCompleted.onSuccess();
                }
                else {
                    onCallCompleted.onUnSuccess();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                onCallCompleted.onFailure();
            }
        });
    }

    public void refreshUser(final OnCallCompleted onUserRefreshed) {
        vaultService.getUser("Bearer " + Session.token.getAccessToken(), Session.user.getEmail()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Session.user = response.body();
                    onUserRefreshed.onSuccess();
                }
                else {
                    onUserRefreshed.onUnSuccess();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onUserRefreshed.onFailure();
            }
        });
    }

    public void updateUser(User user, final OnCallCompleted onCallCompleted) {
        vaultService.updateUser("Bearer " + Session.token.getAccessToken(), user, user.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    onCallCompleted.onSuccess();
                }
                else {
                    onCallCompleted.onUnSuccess();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                onCallCompleted.onFailure();
            }
        });
    }

    public void getAllUsers(final OnCallCompleted onCallCompleted) {
        vaultService.getAllUsers("Bearer " + Session.token.getAccessToken()).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    Session.allUsers = response.body();
                    onCallCompleted.onSuccess();
                }
                else {
                    onCallCompleted.onUnSuccess();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                onCallCompleted.onFailure();
            }
        });
    }

    public void deleteDepartment(String departmentName, final OnCallCompleted onCallCompleted) {
        vaultService.deleteDepartment("Bearer " + Session.token.getAccessToken(), departmentName).enqueue(new Callback<Permission>() {
            @Override
            public void onResponse(Call<Permission> call, Response<Permission> response) {
                if(response.isSuccessful()) {
                    onCallCompleted.onSuccess();
                }
                else {
                    onCallCompleted.onUnSuccess();
                }
            }

            @Override
            public void onFailure(Call<Permission> call, Throwable t) {
                onCallCompleted.onFailure();
            }
        });
    }

    public interface OnCallCompleted {
        void onSuccess();
        void onUnSuccess();
        void onFailure();
    }
}
