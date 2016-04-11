package com.trendoidtechnologies.vault.ui.activities.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.session.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;
import com.trendoidtechnologies.vault.ui.activities.DepartmentsActivity;
import com.trendoidtechnologies.vault.ui.activities.LoginActivity;
import com.trendoidtechnologies.vault.ui.activities.UsersActivity;

import trendoidtechnologies.com.navigationdrawerlibrary.DrawerActivity;
import trendoidtechnologies.com.navigationdrawerlibrary.structure.DrawerItem;
import trendoidtechnologies.com.navigationdrawerlibrary.theme.DrawerTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qazi Ahmed on 7/10/2015.
 * 2014 NCR Corporation
 */
public abstract class BaseActivity extends DrawerActivity {

    protected VaultApiClient vaultApiClient;
    private Bundle extras;
    protected Toolbar toolbar;
    private List<DrawerItem> navigationItems;
    private static final long DISCONNECT_TIMEOUT = 60000;

    public enum NavigationItem {
        DEPARTMENTS,
        USERS,
        LOGOUT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(activityToInflate());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vaultApiClient = new VaultApiClient(getApplicationContext());
        initializeView();
        onViewCreated();
    }

    protected void onViewCreated() {
        if (this instanceof LoginActivity) {

        } else {
            setDrawer();
        }
    }

    private Handler disconnectHandler = new Handler(){
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            Log.d("TIMER_HIT", "Logging out");
            if(!(BaseActivity.this instanceof LoginActivity)) {
                navigateToItem(NavigationItem.LOGOUT);
            }
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    protected void setDrawer() {
        if (Session.user.isAdmin()) {
            navigationItems = new ArrayList<>();
            navigationItems.add(new DrawerItem()
                    .setTextPrimary(getString(R.string.navigation_item_my_departments))
                    .setImage(ContextCompat.getDrawable(this, R.drawable.departments)));
            navigationItems.add(new DrawerItem()
                    .setTextPrimary(getString(R.string.navigation_item_users))
                    .setImage(ContextCompat.getDrawable(this, R.drawable.users)));
            navigationItems.add(new DrawerItem()
                    .setTextPrimary(getString(R.string.navigation_item_log_out))
                    .setImage(ContextCompat.getDrawable(this, R.drawable.logout)));
            setDrawerTheme(
                    new DrawerTheme(this)
                            .setBackgroundColorRes(R.color.colorPrimary)
                            .setTextColorPrimaryRes(R.color.white)
                            .setTextColorSecondaryRes(R.color.lightGray)
            );

            addItems(navigationItems);
            setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                @Override
                public void onClick(DrawerItem item, long id, int position) {
                    selectItem(position);
                    switch (position) {
                        case 0:
                            navigateToItem(NavigationItem.DEPARTMENTS);
                            break;
                        case 1:
                            navigateToItem(NavigationItem.USERS);
                            break;
                        case 2:
                            navigateToItem(NavigationItem.LOGOUT);
                            break;
                    }
                }
            });

            addProfile(Session.getDrawerProfileInstance(true));
        } else {
            navigationItems = new ArrayList<>();
            navigationItems.add(new DrawerItem()
                    .setTextPrimary(getString(R.string.navigation_item_my_departments))
                    .setImage(ContextCompat.getDrawable(this, R.drawable.departments)));
            navigationItems.add(new DrawerItem()
                    .setTextPrimary(getString(R.string.navigation_item_log_out))
                    .setImage(ContextCompat.getDrawable(this, R.drawable.logout)));
            setDrawerTheme(
                    new DrawerTheme(this)
                            .setBackgroundColorRes(R.color.colorPrimary)
                            .setTextColorPrimaryRes(R.color.white)
                            .setTextColorSecondaryRes(R.color.lightGray)
            );

            addItems(navigationItems);
            setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                @Override
                public void onClick(DrawerItem item, long id, int position) {
                    selectItem(position);
                    switch (position) {
                        case 0:
                            navigateToItem(NavigationItem.DEPARTMENTS);
                            break;
                        case 1:
                            navigateToItem(NavigationItem.LOGOUT);
                            break;
                    }
                }
            });

            addProfile(Session.getDrawerProfileInstance(false));
        }

    }

    protected void navigateToItem(NavigationItem navigationItem) {
        closeDrawer();
        switch (navigationItem) {
            case DEPARTMENTS:
                if (!(this instanceof DepartmentsActivity)) {
                    navigateToActivity(DepartmentsActivity.class);
                }
                break;
            case USERS:
                if (!(this instanceof UsersActivity)) {
                    navigateToActivity(UsersActivity.class);
                }
                break;
            case LOGOUT:
                Session.clearSession();
                navigateToActivity(LoginActivity.class, true);
                finish();
                break;
        }
    }

    protected boolean isNavDrawerEnabled() {
        return true;
    }

    protected abstract void initializeView();

    protected abstract @LayoutRes int activityToInflate();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected void navigateToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected void navigateToActivity(Class activity, Bundle extraBundle) {
        Intent intent = new Intent(this, activity);
        intent.putExtras(extraBundle);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected void navigateToActivity(Class activity, Bundle extraBundle, boolean clearTop) {
        Intent intent = new Intent(this, activity);
        if (clearTop) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        intent.putExtras(extraBundle);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    protected void navigateToActivity(Class activity, boolean clearTop) {
        Intent intent = new Intent(this, activity);
        if (clearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if(clearTop) {
            finish();
        }
    }

    public Bundle getExtras() {
        return extras;
    }


    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }

    public void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
