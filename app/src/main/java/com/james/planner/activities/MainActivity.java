package com.james.planner.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.james.planner.R;
import com.james.planner.adapters.SimplePagerAdapter;
import com.james.planner.fragments.DoneFragment;
import com.james.planner.fragments.OngoingFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private LinearLayout linearLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SimplePagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ;

        adapter = new SimplePagerAdapter(this, getSupportFragmentManager(), new OngoingFragment(), new DoneFragment());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }
}
