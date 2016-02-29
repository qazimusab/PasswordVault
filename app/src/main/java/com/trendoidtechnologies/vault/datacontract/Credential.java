package com.trendoidtechnologies.vault.datacontract;

/**
 * Created by qazimusab on 2/26/16.
 */
public class Credential {
    private String username;
    private String password;

    public Credential(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
