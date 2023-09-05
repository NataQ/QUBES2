package id.co.qualitas.qubes.activity.aspp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.NewTabFragmentAdapter;
import id.co.qualitas.qubes.fragment.aspp.OutletNooFragment;
import id.co.qualitas.qubes.fragment.aspp.OutletVisitFragment;

public class OutletActivity extends BaseActivity {
    NewTabFragmentAdapter adapter;
    TabLayout tabLayout;
    ViewPager mViewPager;
    ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_outlet);

        init();
        initialize();

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        adapter = new NewTabFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), "visit");
        adapter.addFragment(new OutletVisitFragment(), "Visit");
        adapter.addFragment(new OutletNooFragment(), "New Outlet Opening");
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(ContextCompat.getColor(this, R.color.caldroid_gray), ContextCompat.getColor(this, R.color.new_blue));
    }

    private void initialize() {
        mViewPager = findViewById(R.id.mViewPager);
        imgBack = findViewById(R.id.imgBack);
        tabLayout = findViewById(R.id.tabLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}