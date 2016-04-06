package com.trendoidtechnologies.vault.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.datacontract.Computer;
import com.trendoidtechnologies.vault.datacontract.Permission;
import com.trendoidtechnologies.vault.service.Session;
import com.trendoidtechnologies.vault.ui.adapter.ComputerRecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;

public class ComputersActivity extends BaseActivity {

    private String department;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView computersListView;
    private LinearLayoutManager linearLayoutManager;
    private ComputerRecyclerViewAdapter computerRecyclerViewAdapter;
    private TextView expandedImage;


    @Override
    public void onBackPressed() {
        Session.currentDepartment = "";
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expandedImage = (TextView) findViewById(R.id.expandedImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandedImage.setTransitionName(Session.currentDepartment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    protected void initializeView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComputersActivity.this, AddComputerActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(ComputersActivity.this, expandedImage, "button");
                startActivity(intent, options.toBundle());
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
