
package com.trendoidtechnologies.vault.datacontract;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Computer {

    @SerializedName("ComputerName")
    @Expose
    private String ComputerName;
    @SerializedName("DepartmentName")
    @Expose
    private String DepartmentName;
    @SerializedName("ComputerId")
    @Expose
    private int ComputerId;
    @SerializedName("Credentials")
    @Expose
    private List<Credential> Credentials = new ArrayList<Credential>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Computer() {
    }

    /**
     * 
     * @param ComputerId
     * @param ComputerName
     * @param Credentials
     * @param DepartmentName
     */
    public Computer(String ComputerName, String DepartmentName, int ComputerId, List<Credential> Credentials) {
        this.ComputerName = ComputerName;
        this.DepartmentName = DepartmentName;
        this.ComputerId = ComputerId;
        this.Credentials = Credentials;
    }

    /**
     * 
     * @return
     *     The ComputerName
     */
    public String getComputerName() {
        return ComputerName;
    }

    /**
     * 
     * @param ComputerName
     *     The ComputerName
     */
    public void setComputerName(String ComputerName) {
        this.ComputerName = ComputerName;
    }

    /**
     * 
     * @return
     *     The DepartmentName
     */
    public String getDepartmentName() {
        return DepartmentName;
    }

    /**
     * 
     * @param DepartmentName
     *     The DepartmentName
     */
    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
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
     *     The Credentials
     */
    public List<Credential> getCredentials() {
        return Credentials;
    }

    /**
     * 
     * @param Credentials
     *     The Credentials
     */
    public void setCredentials(List<Credential> Credentials) {
        this.Credentials = Credentials;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
