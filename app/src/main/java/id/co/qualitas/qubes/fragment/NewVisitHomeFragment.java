package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.NewMainActivity;
import id.co.qualitas.qubes.adapter.NewTabFragmentAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;

public class NewVisitHomeFragment extends BaseFragment {
    NewTabFragmentAdapter adapter;
    TabLayout tabLayout;
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new_visit_home, container, false);

        initialize();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((NewMainActivity) getActivity()).changePage(1);
                        return true;
                    }
                }
                return false;
            }
        });

        adapter = new NewTabFragmentAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount(),"visit");
        adapter.addFragment(new RetailOutletFragment(), "Visit");
//        adapter.addFragment(new RetailOutletFragment(), "New Outlet Opening");
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(ContextCompat.getColor(getContext(), R.color.caldroid_gray), ContextCompat.getColor(getContext(), R.color.new_blue));

        return rootView;
    }

    private void initialize() {
        mViewPager = rootView.findViewById(R.id.mViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "3");
//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//                    // handle back button
//                    setContentToHome();
//                    return true;
//                }
//                return false;
//            }
//        });
    }
}