package com.trendoidtechnologies.vault.service;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.datacontract.Credential;
import com.trendoidtechnologies.vault.datacontract.Token;
import com.trendoidtechnologies.vault.datacontract.User;

import java.util.List;

import trendoidtechnologies.com.navigationdrawerlibrary.structure.DrawerProfile;

/**
 * Created by qazimusab on 4/7/16.
 */
public class Session {

    private static final String PROFILE_AVATAR = "PROFILE_AVATAR";
    private static final String NAV_DRAWER_HEADER = "NAV_DRAWER_HEADER";
    private static final String LIST_VIEW_HEADER = "LIST_VIEW_HEADER";
    public static List<User> allUsers;
    public static Token token;
    public static User user;
    public static String currentDepartment;
    public static Computer currentComputer;
    public static Credential currentCredential;
    public static User currentUser;
    private static DrawerProfile drawerProfile;
    private static LruCache<String, Drawable> lruCache;

    public static DrawerProfile getDrawerProfileInstance(boolean isAdmin) {
        if(drawerProfile == null) {
            drawerProfile = new DrawerProfile()
                    .setId(1)
                    .setRoundedAvatar((BitmapDrawable) lruCache.get(PROFILE_AVATAR))
                    .setBackground(lruCache.get(NAV_DRAWER_HEADER))
                    .setName(Session.user.getFirstName() + " " + Session.user.getLastName())
                    .setDescription(isAdmin ? "Administrator" : "User");
        }
        return drawerProfile;
    }

    public static Drawable getListViewHeader() {
        return lruCache.get(LIST_VIEW_HEADER);
    }

    public static void cacheAssets(Context context){

        if(lruCache == null) {
            lruCache = new LruCache<>(5);
            lruCache.put(PROFILE_AVATAR, ContextCompat.getDrawable(context, R.drawable.profile_avatar));
            lruCache.put(NAV_DRAWER_HEADER, ContextCompat.getDrawable(context, R.drawable.nav_drawer_header));
            lruCache.put(LIST_VIEW_HEADER, ContextCompat.getDrawable(context, R.drawable.list_view_header));
        }
    }

    public static void clearSession() {
        token = null;
        user = null;
        currentDepartment = null;
        currentComputer = null;
        currentCredential = null;
        allUsers = null;
        currentUser = null;
    }
}
