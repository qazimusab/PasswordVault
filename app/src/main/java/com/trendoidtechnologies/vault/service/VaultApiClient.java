package com.trendoidtechnologies.vault.service;

import android.content.Context;

import com.trendoidtechnologies.vault.datacontract.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by qazimusab on 4/5/16.
 */
public class VaultApiClient {

        VaultService vaultService;

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
}
