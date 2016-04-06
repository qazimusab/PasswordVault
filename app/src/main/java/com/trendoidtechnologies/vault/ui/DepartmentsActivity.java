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
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.ui.adapter.DepartmentsRecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;

public class DepartmentsActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView departmentsListView;
    private LinearLayoutManager linearLayoutManager;
    private DepartmentsRecyclerViewAdapter myDepartmentsRecyclerViewAdapter;
    private TextView expandedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDepartmentsRecyclerViewAdapter.clear();

        for(Permission permission : Session.user.getPermissions()) {

            myDepartmentsRecyclerViewAdapter.add(permission.getDepartmentName());
        }

        departmentsListView.setAdapter(myDepartmentsRecyclerViewAdapter);
        departmentsListView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initializeView() {
        expandedImage = (TextView) findViewById(R.id.expandedImage);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DepartmentsActivity.this, AddDepartmentActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(DepartmentsActivity.this, expandedImage, "button");
                startActivity(intent, options.toBundle());
            }
        });

        if(!Session.user.isAdmin()) {
            fab.setVisibility(View.GONE);
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setCollapsingToolbarLayoutTitle(getString(R.string.departments_page_title));

        departmentsListView = (RecyclerView) findViewById(R.id.departments_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        departmentsListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        departmentsListView.setHasFixedSize(true);
        departmentsListView.setLayoutManager(linearLayoutManager);
        myDepartmentsRecyclerViewAdapter = new DepartmentsRecyclerViewAdapter(this);
        myDepartmentsRecyclerViewAdapter.setOnItemClickListener(onItemClickListener);
    }

    private void setCollapsingToolbarLayoutTitle(String title) {
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.DEFAULT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setTitle(title);
    }

    private DepartmentsRecyclerViewAdapter.OnItemClickListener onItemClickListener = new DepartmentsRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(DepartmentsRecyclerViewAdapter.ItemHolder item, int position) {
            String itemValue = myDepartmentsRecyclerViewAdapter.getItemAtPosition(position);
            Session.currentDepartment = itemValue;
            Intent intent = new Intent(DepartmentsActivity.this, ComputersActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(DepartmentsActivity.this, item.textItemName, itemValue);
            startActivity(intent, options.toBundle());
        }
    };

    @Override
    protected int activityToInflate() {
        return R.layout.activity_departments;
    }

}
