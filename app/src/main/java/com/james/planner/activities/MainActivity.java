package com.james.planner.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.james.planner.R;
import com.james.planner.adapters.SimplePagerAdapter;
import com.james.planner.data.NoteData;
import com.james.planner.dialogs.NoteDialog;
import com.james.planner.fragments.DoneFragment;
import com.james.planner.fragments.OngoingFragment;
import com.james.planner.utils.ImageUtils;
import com.james.planner.utils.StaticUtils;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private SimplePagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        adapter = new SimplePagerAdapter(this, getSupportFragmentManager(), new OngoingFragment(), new DoneFragment());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) fab.show();
                else fab.hide();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        fab.setImageDrawable(ImageUtils.getVectorDrawable(this, R.drawable.ic_add));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteData data = new NoteData();

                NoteDialog dialog = new NoteDialog(MainActivity.this, data);
                dialog.setTransition(NoteDialog.TRANSITION_CIRCLE, StaticUtils.getRelativeLeft(v) + (v.getWidth() / 2), StaticUtils.getRelativeTop(v), v.getWidth(), v.getHeight());
                dialog.show();
            }
        });
    }
}
