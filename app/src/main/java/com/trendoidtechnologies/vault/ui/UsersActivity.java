package com.trendoidtechnologies.vault.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.User;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.ui.adapter.UsersRecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;

public class UsersActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView departmentsListView;
    private LinearLayoutManager linearLayoutManager;
    private UsersRecyclerViewAdapter usersRecyclerViewAdapter;
    private TextView expandedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeView() {
        expandedImage = (TextView) findViewById(R.id.expandedImage);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsersActivity.this, AddUserActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(UsersActivity.this, expandedImage, "button");
                startActivity(intent, options.toBundle());
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

        usersRecyclerViewAdapter.clear();
        for(User user : Session.allUsers) {
            if(!user.isAdmin()) {
                usersRecyclerViewAdapter.add(user);
            }
        }

        departmentsListView.setAdapter(usersRecyclerViewAdapter);
        departmentsListView.setItemAnimator(new DefaultItemAnimator());
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
