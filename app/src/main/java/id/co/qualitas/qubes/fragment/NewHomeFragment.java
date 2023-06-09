package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.NewTabFragmentAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;

public class NewHomeFragment extends BaseFragment {
    TabLayout mTabs;
    View mIndicator;
    ViewPager mViewPager;

    NewTabFragmentAdapter adapter;
    private int indicatorWidth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.new_fragment_home, container, false);
        //Assign view reference
        mTabs = rootView.findViewById(R.id.tab);
        mIndicator = rootView.findViewById(R.id.indicator);
        mViewPager = rootView.findViewById(R.id.viewPager);

//        mTabs.addTab(mTabs.newTab().setText("Calendar"));
//        mTabs.addTab(mTabs.newTab().setText("Target"));
//        mTabs.addTab(mTabs.newTab().setText("Promotion"));
//        adapter = new NewTabFragmentAdapter(getActivity().getSupportFragmentManager(), mTabs.getTabCount(), "home");
//        mViewPager.setAdapter(adapter);

        //Set up the view pager and fragments
        adapter = new NewTabFragmentAdapter(getChildFragmentManager(), mTabs.getTabCount(), "home");
        adapter.addFragment(new CalendarFragment(), "Calendar");
        adapter.addFragment(new TargetFragment(), "Target");
        adapter.addFragment(new PromotionFragment(), "Promotion");

        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);

        mTabs.getTabAt(0).setIcon(R.drawable.ic_calender);
        mTabs.getTabAt(1).setIcon(R.drawable.ic_target);
        mTabs.getTabAt(2).setIcon(R.drawable.ic_promotion);
        mTabs.setTabTextColors(ContextCompat.getColor(getContext(), R.color.white), ContextCompat.getColor(getContext(), R.color.white));

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));

        mViewPager.setCurrentItem(0);

        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Determine indicator width at runtime
        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth() / mTabs.getTabCount();
                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {
                mViewPager.setCurrentItem(mTabs.getSelectedTabPosition());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "1");
    }
}