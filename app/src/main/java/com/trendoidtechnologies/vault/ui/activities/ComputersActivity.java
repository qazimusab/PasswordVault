package com.trendoidtechnologies.vault.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.session.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;
import com.trendoidtechnologies.vault.ui.activities.base.BaseActivity;
import com.trendoidtechnologies.vault.ui.adapter.ComputerRecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;
import com.trendoidtechnologies.vault.ui.widgets.MyAppBarLayout;

public class ComputersActivity extends BaseActivity {

    private String department;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView computersListView;
    private LinearLayoutManager linearLayoutManager;
    private ComputerRecyclerViewAdapter computerRecyclerViewAdapter;
    private TextView expandedImage;
    private MyAppBarLayout myAppBarLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expandedImage = (TextView) findViewById(R.id.expandedImage);
        expandedImage.setBackground(Session.getListViewHeader());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandedImage.setTransitionName(Session.currentDepartment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage();
    }

    @Override
    protected void initializeView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        myAppBarLayout = (MyAppBarLayout) findViewById(R.id.app_bar_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_computers_swipe_to_refresh_layout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComputersActivity.this, AddComputerActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(ComputersActivity.this, expandedImage, "button");
                startActivity(intent, options.toBundle());
            }
        });
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

        department = Session.currentDepartment;

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        setCollapsingToolbarLayoutTitle(department);

        computersListView = (RecyclerView) findViewById(R.id.departments_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        computersListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        computersListView.setHasFixedSize(true);
        computersListView.setLayoutManager(linearLayoutManager);
        computerRecyclerViewAdapter = new ComputerRecyclerViewAdapter(this);
        computerRecyclerViewAdapter.setOnItemClickListener(onItemClickListener);

        final ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                swipeRefreshLayout.setRefreshing(true);
                Computer computerSwiped = computerRecyclerViewAdapter.getItemAtPosition(viewHolder.getAdapterPosition());
                vaultApiClient.deleteComputer(computerSwiped.getComputerId(), new VaultApiClient.OnCallCompleted() {
                    @Override
                    public void onSuccess() {
                        vaultApiClient.refreshUser(new VaultApiClient.OnCallCompleted() {
                            @Override
                            public void onSuccess() {
                                refreshPage();
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getApplicationContext(), "The computer was successfully deleted", Toast.LENGTH_LONG).show();
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
        itemTouchHelper.attachToRecyclerView(computersListView);

    }

    private void refreshPage() {
        computerRecyclerViewAdapter.clear();
        for(Permission permissions : Session.user.getPermissions()){
            if(permissions.getDepartmentName().equals(department)) {
                for(Computer computer : permissions.getComputers()){
                    computerRecyclerViewAdapter.add(computer);
                }
            }
        }
        computersListView.setAdapter(computerRecyclerViewAdapter);
        computersListView.setItemAnimator(new DefaultItemAnimator());
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
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
    }

    private ComputerRecyclerViewAdapter.OnItemClickListener onItemClickListener = new ComputerRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ComputerRecyclerViewAdapter.ItemHolder item, int position) {
            Computer itemValue = computerRecyclerViewAdapter.getItemAtPosition(position);
            Session.currentComputer = itemValue;
            Intent intent = new Intent(ComputersActivity.this, CredentialsActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(ComputersActivity.this, item.textItemName, itemValue.getComputerName());
            startActivity(intent, options.toBundle());
        }
    };

    @Override
    protected int activityToInflate() {
        return R.layout.activity_computers;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);

    }
}
