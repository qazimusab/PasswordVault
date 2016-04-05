package com.trendoidtechnologies.vault.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.datacontract.Credential;
import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.ui.adapter.CredentialsRecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;

public class CredentialsActivity extends BaseActivity {

    private String departmentName;
    private String computerName;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView credentialsListView;
    private LinearLayoutManager linearLayoutManager;
    private CredentialsRecyclerViewAdapter credentialsRecyclerViewAdapter;
    private TextView expandedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expandedImage = (TextView) findViewById(R.id.expandedImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandedImage.setTransitionName(getExtras().getString(ComputersActivity.COMPUTER_KEY));
        }
    }

    @Override
    protected void initializeView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        computerName = getExtras().getString(ComputersActivity.COMPUTER_KEY);
        departmentName = getExtras().getString(DepartmentsActivity.DEPARTMENT_KEY);

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

            // Show Alert
//            Toast.makeText(getApplicationContext(),
//                    "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
//                    .show();
        }
    };

    @Override
    protected int activityToInflate() {
        return R.layout.activity_credentials;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);

    }
}
