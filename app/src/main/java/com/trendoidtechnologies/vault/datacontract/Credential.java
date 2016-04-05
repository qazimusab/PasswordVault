
package com.trendoidtechnologies.vault.datacontract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Credential {

    @SerializedName("UserName")
    @Expose
    private String UserName;
    @SerializedName("Password")
    @Expose
    private String Password;
    @SerializedName("Type")
    @Expose
    private String Type;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Credential() {
    }

    /**
     *
     * @param Password
     * @param UserName
     */
    public Credential(String UserName, String Password) {
        this.UserName = UserName;
        this.Password = Password;
    }

    /**
     *
     * @param Type
     * @param Password
     * @param UserName
     */
    public Credential(String UserName, String Password, String Type) {
        this.UserName = UserName;
        this.Password = Password;
        this.Type = Type;
    }

    /**
     * 
     * @return
     *     The UserName
     */
    public String getUserName() {
        return UserName;
    }

    /**
     * 
     * @param UserName
     *     The UserName
     */
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    /**
     * 
     * @return
     *     The Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * 
     * @param Password
     *     The Password
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     * 
     * @return
     *     The Type
     */
    public String getType() {
        return Type;
    }

    /**
     * 
     * @param Type
     *     The Type
     */
    public void setType(String Type) {
        this.Type = Type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
