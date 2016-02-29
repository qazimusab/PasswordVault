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

import com.trendoidtechnologies.vault.R;
import com.trendoidtechnologies.vault.ui.adapter.RecyclerViewAdapter;
import com.trendoidtechnologies.vault.ui.widgets.DividerItemDecoration;

public class DepartmentsActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView departmentsListView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter myRecyclerViewAdapter;
    public static String DEPARTMENT_KEY = "department";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeView() {

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DepartmentsActivity.this, AddDepartmentActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(DepartmentsActivity.this, fab, "fab_transition");
                startActivity(intent, options.toBundle());
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setCollapsingToolbarLayoutTitle(getString(R.string.departments_page_title));

        departmentsListView = (RecyclerView) findViewById(R.id.departments_list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        departmentsListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        departmentsListView.setHasFixedSize(true);
        departmentsListView.setLayoutManager(linearLayoutManager);
        myRecyclerViewAdapter = new RecyclerViewAdapter(this);
        myRecyclerViewAdapter.setOnItemClickListener(onItemClickListener);


        myRecyclerViewAdapter.add("Software Engineering");
        myRecyclerViewAdapter.add("Information Technology");
        myRecyclerViewAdapter.add("Mechanical Engineering");
        myRecyclerViewAdapter.add("Computer Science");
        myRecyclerViewAdapter.add("Game Design");
        myRecyclerViewAdapter.add("Biology");
        myRecyclerViewAdapter.add("Mathematics");

        departmentsListView.setAdapter(myRecyclerViewAdapter);
        departmentsListView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setCollapsingToolbarLayoutTitle(String title) {
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryDark));
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.DEFAULT);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsingToolbarLayout.setTitle(title);
    }

    private RecyclerViewAdapter.OnItemClickListener onItemClickListener = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(RecyclerViewAdapter.ItemHolder item, int position) {

            int itemPosition = position;


            String itemValue = myRecyclerViewAdapter.getItemAtPosition(position);

            // Show Alert
//            Toast.makeText(getApplicationContext(),
//                    "Position :"+itemPosition+"  ListItem : " + itemValue , Toast.LENGTH_LONG)
//                    .show();
            Bundle bundle = new Bundle();
            bundle.putString(DEPARTMENT_KEY, itemValue);
            Intent intent = new Intent(DepartmentsActivity.this, ComputersActivity.class);
            intent.putExtras(bundle);
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
