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

import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.datacontract.Credential;
import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.session.Session;
import com.trendoidtechnologies.vault.service.VaultApiClient;
import com.trendoidtechnologies.vault.ui.activities.base.BaseActivity;
import com.trendoidtechnologies.vault.ui.adapter.CredentialsRecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;
import com.trendoidtechnologies.vault.ui.widgets.MyAppBarLayout;

public class CredentialsActivity extends BaseActivity {

    private String departmentName;
    private String computerName;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView credentialsListView;
    private LinearLayoutManager linearLayoutManager;
    private CredentialsRecyclerViewAdapter credentialsRecyclerViewAdapter;
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
            expandedImage.setTransitionName(Session.currentComputer.getComputerName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage();
    }

    private void refreshPage() {
        credentialsRecyclerViewAdapter.clear();
        for(Permission permissions : Session.user.getPermissions()){
            if(permissions.getDepartmentName().equals(departmentName)) {
                for(Computer computer : permissions.getComputers()){
                    if(computer.getComputerName().equals(computerName)) {
                        for(Credential credential : computer.getCredentials()) {
                            credentialsRecyclerViewAdapter.add(credential);
                        }
                    }
                }
            }
        }

        credentialsListView.setAdapter(credentialsRecyclerViewAdapter);
        credentialsListView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initializeView() {
        myAppBarLayout = (MyAppBarLayout) findViewById(R.id.app_bar_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_credentials_swipe_to_refresh_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CredentialsActivity.this, AddCredentialActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(CredentialsActivity.this, expandedImage, "button");
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

        computerName = Session.currentComputer.getComputerName();
        departmentName = Session.currentDepartment;

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        setCollapsingToolbarLayoutTitle(computerName);

        credentialsListView = (RecyclerView) findViewById(R.id.departments_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        credentialsListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        credentialsListView.setHasFixedSize(true);
        credentialsListView.setLayoutManager(linearLayoutManager);
        credentialsRecyclerViewAdapter = new CredentialsRecyclerViewAdapter(this);
        credentialsRecyclerViewAdapter.setOnItemClickListener(onItemClickListener);

        final ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                swipeRefreshLayout.setRefreshing(true);
                Credential credentialSwiped = credentialsRecyclerViewAdapter.getItemAtPosition(viewHolder.getAdapterPosition());
                vaultApiClient.deleteCredential(credentialSwiped.getId(), new VaultApiClient.OnCallCompleted() {
                    @Override
                    public void onSuccess() {
                        vaultApiClient.refreshUser(new VaultApiClient.OnCallCompleted() {
                            @Override
                            public void onSuccess() {
                                refreshPage();
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(getApplicationContext(), "The credential was successfully deleted", Toast.LENGTH_LONG).show();
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
        itemTouchHelper.attachToRecyclerView(credentialsListView);
    }

    private void setCollapsingToolbarLayoutTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
    }

    private CredentialsRecyclerViewAdapter.OnItemClickListener onItemClickListener = new CredentialsRecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(CredentialsRecyclerViewAdapter.ItemHolder item, int position) {
            int itemPosition = position;


            Credential itemValue = credentialsRecyclerViewAdapter.getItemAtPosition(position);
            Session.currentCredential = itemValue;
            navigateToActivity(EditCredentialActivity.class);
        }
    };

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        swipeRefreshLayout.setEnabled(false);

        if(myAppBarLayout.state == MyAppBarLayout.State.EXPANDED){
            swipeRefreshLayout.setEnabled(true);
        }
    }

    @Override
    protected int activityToInflate() {
        return R.layout.activity_credentials;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);

    }
}
