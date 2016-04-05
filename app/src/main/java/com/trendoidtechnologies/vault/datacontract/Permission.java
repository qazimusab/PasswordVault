
package com.trendoidtechnologies.vault.datacontract;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Permission {

    @SerializedName("DepartmentName")
    @Expose
    private String DepartmentName;
    @SerializedName("Computers")
    @Expose
    private List<Computer> Computers = new ArrayList<Computer>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Permission() {
    }

    /**
     * 
     * @param Computers
     * @param DepartmentName
     */
    public Permission(String DepartmentName, List<Computer> Computers) {
        this.DepartmentName = DepartmentName;
        this.Computers = Computers;
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
     *     The Computers
     */
    public List<Computer> getComputers() {
        return Computers;
    }

    /**
     * 
     * @param Computers
     *     The Computers
     */
    public void setComputers(List<Computer> Computers) {
        this.Computers = Computers;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
