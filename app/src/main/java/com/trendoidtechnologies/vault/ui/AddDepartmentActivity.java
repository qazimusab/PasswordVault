package com.trendoidtechnologies.vault.ui;

import com.trendoidtechnologies.vault.R;

public class AddDepartmentActivity extends BaseActivity {


    @Override
    protected void initializeView() {
        toolbar.setTitle(getString(R.string.add_department_page_title));
    }

    @Override
    protected int activityToInflate() {
        return R.layout.activity_add_department;
    }
}
