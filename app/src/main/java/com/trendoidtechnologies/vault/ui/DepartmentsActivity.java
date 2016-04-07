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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;
import com.trendoidtechnologies.vault.ui.adapter.DepartmentsRecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;
import com.trendoidtechnologies.vault.ui.widgets.MyAppBarLayout;

public class DepartmentsActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView departmentsListView;
    private LinearLayoutManager linearLayoutManager;
    private DepartmentsRecyclerViewAdapter myDepartmentsRecyclerViewAdapter;
    private TextView expandedImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyAppBarLayout myAppBarLayout;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage();
    }

    public void refreshPage(){
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        myAppBarLayout = (MyAppBarLayout) findViewById(R.id.app_bar_layout);
        departmentsListView = (RecyclerView) findViewById(R.id.departments_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_departments_swipe_to_refresh_layout);
        myDepartmentsRecyclerViewAdapter = new DepartmentsRecyclerViewAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        expandedImage.setBackground(Session.getListViewHeader());

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

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vaultApiClient.refreshUser(new VaultApiClient.OnCallCompleted() {
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

        setCollapsingToolbarLayoutTitle(getString(R.string.departments_page_title));

        departmentsListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        departmentsListView.setHasFixedSize(true);
        departmentsListView.setLayoutManager(linearLayoutManager);

        final ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                swipeRefreshLayout.setRefreshing(true);
                String departmentSwiped = myDepartmentsRecyclerViewAdapter.getItemAtPosition(viewHolder.getAdapterPosition());
                vaultApiClient.deleteDepartment(departmentSwiped, new VaultApiClient.OnCallCompleted() {
                    @Override
                    public void onSuccess() {
                        vaultApiClient.refreshUser(new VaultApiClient.OnCallCompleted() {
                            @Override
                            public void onSuccess() {
                                refreshPage();
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getApplicationContext(), "The department was successfully deleted", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onUnSuccess() {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onFailure() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onUnSuccess() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });

            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(departmentsListView);
        myDepartmentsRecyclerViewAdapter.setOnItemClickListener(onItemClickListener);
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
