package com.trendoidtechnologies.vault.ui.widgets;

import com.trendoidtechnologies.vault.datacontract.Permission;

/**
 * Created by qazimusab on 4/7/16.
 */
public class KeyPairBoolData {

    int id;
    Permission permission;
    boolean isSelected;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the Permission
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * @param permission the name to set
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}