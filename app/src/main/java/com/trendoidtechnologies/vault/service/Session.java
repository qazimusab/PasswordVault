package com.trendoidtechnologies.vault.service;

import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.datacontract.Token;
import com.trendoidtechnologies.vault.datacontract.User;

/**
 * Created by qazimusab on 4/5/16.
 */
public class Session {
    public static Token token;
    public static User user;
    public static String currentDepartment;
    public static Computer currentComputer;

    public static void clearSession() {
        token = null;
        user = null;
        currentDepartment = null;
        currentComputer = null;
    }
}
