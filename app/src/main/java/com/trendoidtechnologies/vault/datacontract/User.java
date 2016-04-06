
package com.trendoidtechnologies.vault.datacontract;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class User {

    @SerializedName("FirstName")
    @Expose
    private String FirstName;
    @SerializedName("LastName")
    @Expose
    private String LastName;
    @SerializedName("Password")
    @Expose
    private String Password;
    @SerializedName("ConfirmPassword")
    @Expose
    private String ConfirmPassword;
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("IsAdmin")
    @Expose
    private boolean IsAdmin;
    @SerializedName("Permissions")
    @Expose
    private List<Permission> Permissions = new ArrayList<Permission>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public User() {
    }

    /**
     * 
     * @param Email
     * @param Password
     * @param FirstName
     * @param Id
     * @param IsAdmin
     * @param LastName
     * @param Permissions
     */
    public User(String FirstName, String LastName, String Password, String Email, String Id, boolean IsAdmin, List<Permission> Permissions) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Password = Password;
        this.Email = Email;
        this.Id = Id;
        this.IsAdmin = IsAdmin;
        this.Permissions = Permissions;
    }

    /**
     *
     * @param Email
     * @param Password
     * @param FirstName
     * @param Id
     * @param IsAdmin
     * @param LastName
     * @param Permissions
     */
    public User(String FirstName, String LastName, String Password, String ConfirmPassword, String Email, String Id, boolean IsAdmin, List<Permission> Permissions) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Password = Password;
        this.ConfirmPassword = ConfirmPassword;
        this.Email = Email;
        this.Id = Id;
        this.IsAdmin = IsAdmin;
        this.Permissions = Permissions;
    }

    /**
     * 
     * @return
     *     The FirstName
     */
    public String getFirstName() {
        return FirstName;
    }

    /**
     * 
     * @param FirstName
     *     The FirstName
     */
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    /**
     * 
     * @return
     *     The LastName
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * 
     * @param LastName
     *     The LastName
     */
    public void setLastName(String LastName) {
        this.LastName = LastName;
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
     *     The ConfirmPassword
     */
    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    /**
     *
     * @param ConfirmPassword
     *     The ConfirmPassword
     */
    public void setConfirmPassword(String ConfirmPassword) {
        this.ConfirmPassword = ConfirmPassword;
    }

    /**
     * 
     * @return
     *     The Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     * 
     * @param Email
     *     The Email
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * 
     * @return
     *     The Id
     */
    public String getId() {
        return Id;
    }

    /**
     * 
     * @param Id
     *     The Id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * 
     * @return
     *     The IsAdmin
     */
    public boolean isAdmin() {
        return IsAdmin;
    }

    /**
     * 
     * @param IsAdmin
     *     The IsAdmin
     */
    public void setIsAdmin(boolean IsAdmin) {
        this.IsAdmin = IsAdmin;
    }

    /**
     * 
     * @return
     *     The Permissions
     */
    public List<Permission> getPermissions() {
        return Permissions;
    }

    /**
     * 
     * @param Permissions
     *     The Permissions
     */
    public void setPermissions(List<Permission> Permissions) {
        this.Permissions = Permissions;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
