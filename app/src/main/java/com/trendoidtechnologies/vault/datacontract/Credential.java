
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
    @SerializedName("ComputerId")
    @Expose
    private int ComputerId;
    @SerializedName("Id")
    @Expose
    private int Id;

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
    public Credential(String UserName, String Password, String Type, int ComputerId) {
        this.UserName = UserName;
        this.Password = Password;
        this.Type = Type;
        this.ComputerId = ComputerId;
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

    /**
     *
     * @return
     *     The ComputerId
     */
    public int getComputerId() {
        return ComputerId;
    }

    /**
     *
     * @param ComputerId
     *     The ComputerId
     */
    public void setComputerId(int ComputerId) {
        this.ComputerId = ComputerId;
    }

    /**
     *
     * @return
     *     The Id
     */
    public int getId() {
        return Id;
    }

    /**
     *
     * @param Id
     *     The Id
     */
    public void setId(int Id) {
        this.Id = Id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
