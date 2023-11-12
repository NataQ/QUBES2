package id.co.qualitas.qubes.fragment.aspp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.activity.aspp.CollectionActivity;
import id.co.qualitas.qubes.activity.aspp.InvoiceVerificationActivity;
import id.co.qualitas.qubes.activity.aspp.StockRequestListActivity;
import id.co.qualitas.qubes.activity.aspp.VisitActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;

public class ActivityFragment extends BaseFragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    private CardView llStockRequest, llInvoice, llVisit, llCollection;
    WorkManager workManager;
    private WorkRequest workRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_activity, container, false);
        getActivity().setTitle(getString(R.string.activity));

        initProgress();
        initFragment();
        initialize();

        workManager = WorkManager.getInstance(getActivity());

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
//                    ((MainActivity) getActivity()).changePage(1);
                    ((MainActivity) getActivity()).backPress();
                }
            }
        });

        llStockRequest.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StockRequestListActivity.class);
            startActivity(intent);
        });

        llInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InvoiceVerificationActivity.class);
            startActivity(intent);
        });

        llVisit.setOnClickListener(v -> {
//            workRequest = new PeriodicWorkRequest.Builder(NotiWorker.class, 15, TimeUnit.MINUTES).build();
//            workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, (PeriodicWorkRequest) workRequest);
            Intent intent = new Intent(getActivity(), VisitActivity.class);
            startActivity(intent);
        });

        llCollection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CollectionActivity.class);
            startActivity(intent);
        });

//        llUnloading.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), UnloadingHeaderActivity.class);
//            startActivity(intent);
//        });

        return rootView;
    }


    private void initialize() {
        database = new Database(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

//        llUnloading = rootView.findViewById(R.id.llUnloading);
//        llEndVisit = rootView.findViewById(R.id.llEndVisit);
        llStockRequest = rootView.findViewById(R.id.llStockRequest);
        llInvoice = rootView.findViewById(R.id.llInvoice);
        llVisit = rootView.findViewById(R.id.llVisit);
        llCollection = rootView.findViewById(R.id.llCollection);
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "3");

        if (user.getType_sales().equals("CO")) {
            llStockRequest.setVisibility(View.VISIBLE);
        } else {
            llStockRequest.setVisibility(View.GONE);
        }
    }
}