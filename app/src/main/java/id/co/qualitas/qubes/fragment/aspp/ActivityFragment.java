package id.co.qualitas.qubes.fragment.aspp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.AlarmManagerActivity;
import id.co.qualitas.qubes.activity.NewMainActivity;
import id.co.qualitas.qubes.activity.aspp.CollectionActivity;
import id.co.qualitas.qubes.activity.aspp.InvoiceVerificationActivity;
import id.co.qualitas.qubes.activity.aspp.OutletActivity;
import id.co.qualitas.qubes.activity.aspp.StockRequestHeaderActivity;
import id.co.qualitas.qubes.activity.aspp.UnloadingHeaderActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.services.NotiWorker;

public class ActivityFragment extends BaseFragment {
    private static final String TAG = NewMainActivity.class.getSimpleName();
    private LinearLayout llStockRequest, llInvoice, llVisit, llCollection, llEndVisit, llUnloading;
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

        init();
        initFragment();
        initialize();

        workManager = WorkManager.getInstance(getActivity());

        llEndVisit.setOnClickListener(v -> {
            workManager.cancelAllWork();
            openDialogEndVisit();
        });

        llStockRequest.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StockRequestHeaderActivity.class);
            startActivity(intent);
        });

        llInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), InvoiceVerificationActivity.class);
            startActivity(intent);
        });

        llVisit.setOnClickListener(v -> {
            workRequest = new PeriodicWorkRequest.Builder(NotiWorker.class, 15, TimeUnit.MINUTES).build();
            workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, (PeriodicWorkRequest) workRequest);
            openDialogStartVisit();
        });

        llCollection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CollectionActivity.class);
            startActivity(intent);
        });

        llUnloading.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UnloadingHeaderActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void openDialogEndVisit() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_end_visit);
        Button btnEnd = dialog.findViewById(R.id.btnEnd);

        btnEnd.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openDialogStartVisit() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.aspp_dialog_start_visit);
        Button btnStart = dialog.findViewById(R.id.btnStart);
        ImageView imgBerangkat = dialog.findViewById(R.id.imgBerangkat);
        LinearLayout llImgBerangkat = dialog.findViewById(R.id.llImgBerangkat);

        llImgBerangkat.setOnClickListener(v -> {
//            Intent camera = new Intent(this, Camera3PLActivity.class);//3
//            startActivityForResult(camera, Constants.REQUEST_CAMERA_CODE);
        });

        btnStart.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(getActivity(), OutletActivity.class);
            startActivity(intent);
        });

        dialog.show();
    }

    private void initialize() {
        db = new DatabaseHelper(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llUnloading = rootView.findViewById(R.id.llUnloading);
        llEndVisit = rootView.findViewById(R.id.llEndVisit);
        llStockRequest = rootView.findViewById(R.id.llStockRequest);
        llInvoice = rootView.findViewById(R.id.llInvoice);
        llVisit = rootView.findViewById(R.id.llVisit);
        llCollection = rootView.findViewById(R.id.llCollection);
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "3");
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
    }
}