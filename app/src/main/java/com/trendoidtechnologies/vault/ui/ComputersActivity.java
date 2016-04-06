package com.trendoidtechnologies.vault.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.trendoidtechnologies.vault.ui.adapter.RecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;

import java.util.List;

public class ComputersActivity extends BaseActivity {

    private String department;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView computersListView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter myRecyclerViewAdapter;
    private TextView expandedImage;
    public static String COMPUTER_KEY = "computer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expandedImage = (TextView) findViewById(R.id.expandedImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandedImage.setTransitionName(getExtras().getString(DepartmentsActivity.DEPARTMENT_KEY));
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

        department = getExtras().getString(DepartmentsActivity.DEPARTMENT_KEY);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        setCollapsingToolbarLayoutTitle(department);

        computersListView = (RecyclerView) findViewById(R.id.departments_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        computersListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        computersListView.setHasFixedSize(true);
        computersListView.setLayoutManager(linearLayoutManager);
        myRecyclerViewAdapter = new RecyclerViewAdapter(this);
        myRecyclerViewAdapter.setOnItemClickListener(onItemClickListener);


        for(Permission permissions : Session.user.getPermissions()){
            if(permissions.getDepartmentName().equals(department)) {
                for(Computer computer : permissions.getComputers()){
                    myRecyclerViewAdapter.add(computer.getComputerName());
                }
            }
        }

        computersListView.setAdapter(myRecyclerViewAdapter);
        computersListView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setCollapsingToolbarLayoutTitle(String title) {
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
    }

    private RecyclerViewAdapter.OnItemClickListener onItemClickListener = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerViewAdapter.ItemHolder item, int position) {
            String itemValue = myRecyclerViewAdapter.getItemAtPosition(position);
            Bundle bundle = new Bundle();
            bundle.putString(COMPUTER_KEY, itemValue);
            bundle.putString(DepartmentsActivity.DEPARTMENT_KEY, department);
            Intent intent = new Intent(ComputersActivity.this, CredentialsActivity.class);
            intent.putExtras(bundle);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(ComputersActivity.this, item.textItemName, itemValue);
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
