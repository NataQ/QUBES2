package id.co.qualitas.qubes.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewTabFragmentAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();
    int totalTabs;
    String from;

    public NewTabFragmentAdapter(FragmentManager fm, int totalTabs, String from) {
        super(fm);
        this.totalTabs = totalTabs;
        this.from = from;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
//        if (from.equals("home")) {
//            switch (i) {
//                case 0:
//                    CalendarFragment calendarFragment = new CalendarFragment();
//                    return calendarFragment;
//                case 1:
//                    TargetFragment targetFragment = new TargetFragment();
//                    return targetFragment;
//                case 2:
//                    PromotionFragment promotionFragment = new PromotionFragment();
//                    return promotionFragment;
//                default:
//                    CalendarFragment calFragment = new CalendarFragment();
//                    return calFragment;
//            }
//        } else {
//            switch (i) {
//                case 0:
//                    CustomerTargetFragment customerTargetFragment = new CustomerTargetFragment();
//                    return customerTargetFragment;
//                case 1:
//                    ObjectTargetFragmentv2 objectTargetFragmentv2 = new ObjectTargetFragmentv2();
//                    return objectTargetFragmentv2;
//                default:
//                    CustomerTargetFragment ctFragment = new CustomerTargetFragment();
//                    return ctFragment;
//            }
//        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
}