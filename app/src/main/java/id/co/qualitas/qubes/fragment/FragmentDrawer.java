package id.co.qualitas.qubes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.adapter.NavigationDrawerAdapter;
import id.co.qualitas.qubes.model.LoginResponse;
import id.co.qualitas.qubes.model.NavDrawerItem;


public class FragmentDrawer extends Fragment {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private static NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private RecyclerView recyclerView;
    View layout;
    private RelativeLayout layout_menu_drawer;
    private FragmentDrawerListener drawerListener;
    private LoginResponse loginResponse;
    private List<NavDrawerItem> menuHomeList;
    private ImageButton btnSync;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static void updateSelected(int position){
        adapter.updateSelected(position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    private void prepareMenu() {
        menuHomeList = new ArrayList<>();
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu1)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu2)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu3)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu4)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu5)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu6)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu7)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu8)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu9)));
        menuHomeList.add(new NavDrawerItem(getString(R.string.navmenu10)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating view layout
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = layout.findViewById(R.id.drawerList);
        layout_menu_drawer = layout.findViewById(R.id.layout_menu_drawer);
//        layout_menu_drawer.setBackgroundDrawable(new ColorDrawable(getResources().getColor( R.color.darkBlue)));

        prepareMenu();
        adapter = new NavigationDrawerAdapter(getActivity(), menuHomeList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
                adapter.updateSelected(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_attach);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }

}