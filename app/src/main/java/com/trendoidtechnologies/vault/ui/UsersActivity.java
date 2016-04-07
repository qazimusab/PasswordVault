package com.trendoidtechnologies.vault.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.User;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;
import com.trendoidtechnologies.vault.ui.adapter.UsersRecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;
import com.trendoidtechnologies.vault.ui.widgets.MyAppBarLayout;

public class UsersActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView departmentsListView;
    private LinearLayoutManager linearLayoutManager;
    private UsersRecyclerViewAdapter usersRecyclerViewAdapter;
    private TextView expandedImage;
    private MyAppBarLayout myAppBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeView() {
        expandedImage = (TextView) findViewById(R.id.expandedImage);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        myAppBarLayout = (MyAppBarLayout) findViewById(R.id.app_bar_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_users_swipe_to_refresh_layout);

        expandedImage.setBackground(Session.getListViewHeader());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersActivity.this, AddUserActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(UsersActivity.this, expandedImage, "button");
                startActivity(intent, options.toBundle());
            }
        });


        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vaultApiClient.getAllUsers(new VaultApiClient.OnCallCompleted() {
                    @Override
                    public void onSuccess() {
                        refreshPage();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onUnSuccess() {
                        refreshPage();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure() {
                        refreshPage();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setCollapsingToolbarLayoutTitle(getString(R.string.users_page_title));

        departmentsListView = (RecyclerView) findViewById(R.id.departments_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        departmentsListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        departmentsListView.setHasFixedSize(true);
        departmentsListView.setLayoutManager(linearLayoutManager);
        usersRecyclerViewAdapter = new UsersRecyclerViewAdapter(this);
        usersRecyclerViewAdapter.setOnItemClickListener(onItemClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage();
    }

    private void refreshPage() {
        usersRecyclerViewAdapter.clear();
        for(User user : Session.allUsers) {
            if(!user.isAdmin()) {
                usersRecyclerViewAdapter.add(user);
            }
        }

        departmentsListView.setAdapter(usersRecyclerViewAdapter);
        departmentsListView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        swipeRefreshLayout.setEnabled(false);

        if(myAppBarLayout.state == MyAppBarLayout.State.EXPANDED){
            swipeRefreshLayout.setEnabled(true);
        }
    }


    private void setCollapsingToolbarLayoutTitle(String title) {
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.DEFAULT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setTitle(title);
    }

    private UsersRecyclerViewAdapter.OnItemClickListener onItemClickListener = new UsersRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(UsersRecyclerViewAdapter.ItemHolder item, int position) {
            User itemValue = usersRecyclerViewAdapter.getUserAtPosition(position);
            Session.currentUser = itemValue;
        }
    };

    @Override
    protected int activityToInflate() {
        return R.layout.activity_users;
    }

}
