package com.trendoidtechnologies.vault.datacontract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by qazimusab on 4/5/16.
 */

public class Token {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private int expiresIn;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName(".issued")
    @Expose
    private String Issued;
    @SerializedName(".expires")
    @Expose
    private String Expires;

    /**
     *
     * @return
     * The accessToken
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     * The access_token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     *
     * @return
     * The tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     *
     * @param tokenType
     * The token_type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     *
     * @return
     * The expiresIn
     */
    public int getExpiresIn() {
        return expiresIn;
    }

    /**
     *
     * @param expiresIn
     * The expires_in
     */
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     *
     * @return
     * The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     * The userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     * The Issued
     */
    public String getIssued() {
        return Issued;
    }

    /**
     *
     * @param Issued
     * The .issued
     */
    public void setIssued(String Issued) {
        this.Issued = Issued;
    }

    /**
     *
     * @return
     * The Expires
     */
    public String getExpires() {
        return Expires;
    }

    /**
     *
     * @param Expires
     * The .expires
     */
    public void setExpires(String Expires) {
        this.Expires = Expires;
    }

}