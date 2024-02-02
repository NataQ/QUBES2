package id.co.qualitas.qubes.activity.aspp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.OrderAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.printer.ConnectorActivity;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class OrderActivity extends BaseActivity {
    private OrderAdapter mAdapter;
    private List<Order> mList;
    private Button btnAdd;
    private Customer outletHeader;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;
    private static final int REQUEST_LOCATION_PERMISSION = 5;
    private static final int REQUEST_ENABLE_BT = 6;

    ArrayList<String> permissionsList;
    String[] permissionsStr = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN
    };
    int permissionsCount = 0;

    ActivityResultLauncher<String[]> permissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    new ActivityResultCallback<Map<String, Boolean>>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onActivityResult(Map<String, Boolean> result) {
                            ArrayList<Boolean> list = new ArrayList<>(result.values());
                            permissionsList = new ArrayList<>();
                            permissionsCount = 0;
                            for (int i = 0; i < list.size(); i++) {
                                if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
                                    permissionsList.add(permissionsStr[i]);
                                } else if (!hasPermission(OrderActivity.this, permissionsStr[i])) {
                                    permissionsCount++;
                                }
                            }
                            if (permissionsList.size() > 0) {
                                //Some permissions are denied and can be asked again.
                                askForPermissions(permissionsList);
                            } else if (permissionsCount > 0) {
                                //Show alert dialog
                                showPermissionDialog();
                            } else {
                                SessionManagerQubes.clearOrderSession();
                                Intent intent = new Intent(OrderActivity.this, OrderAddActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order);

        initialize();

        permissionsList = new ArrayList<>();
        permissionsList.addAll(Arrays.asList(permissionsStr));

        btnAdd.setOnClickListener(y -> {
            if (Helper.isCanvasSales(user)) {
                askForPermissions(permissionsList);
            } else {
                SessionManagerQubes.clearOrderSession();
                Intent intent = new Intent(OrderActivity.this, OrderAddActivity.class);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderActivity.this);
        });

        swipeLayout.setColorSchemeResources(R.color.blue_aspp,
                R.color.green_aspp,
                R.color.yellow_krang,
                R.color.red_krang);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private boolean hasPermission(Context context, String permissionStr) {
        return ContextCompat.checkSelfPermission(context, permissionStr) == PackageManager.PERMISSION_GRANTED;
    }

    private void askForPermissions(ArrayList<String> permissionsList) {
        String[] newPermissionStr = new String[permissionsList.size()];
        for (int i = 0; i < newPermissionStr.length; i++) {
            newPermissionStr[i] = permissionsList.get(i);
        }
        if (newPermissionStr.length > 0) {
            permissionsLauncher.launch(newPermissionStr);
        } else {
            /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
            which will lead them to app details page to enable permissions from there. */
            showPermissionDialog();
        }
    }

    private void showPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission required")
                .setMessage("Some permissions are needed to be allowed to use this app without any problems.")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                });
        if (alertDialog == null) {
            alertDialog = builder.create();
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

    private void setAdapter() {
        mAdapter = new OrderAdapter(this, mList, outletHeader, header -> {
            if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT && !header.isDeleted() && header.getOrder_type().equals("co")) {
                if (!header.isStatusPaid()) {
                    dialogConfirm(header);
                } else {
                    SessionManagerQubes.setOrder(header);
                    Intent intent = new Intent(this, OrderDetailActivity.class);
                    startActivity(intent);
                }
            } else {
                SessionManagerQubes.setOrder(header);
                Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        llNoData = findViewById(R.id.llNoData);
        imgLogOut = findViewById(R.id.imgLogOut);
        imgBack = findViewById(R.id.imgBack);
        btnAdd = findViewById(R.id.btnAdd);
        progressCircle = findViewById(R.id.progressCircle);
        swipeLayout = findViewById(R.id.swipeLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFirstDataOffline();
    }

    private void getFirstDataOffline() {
        outletHeader = SessionManagerQubes.getOutletHeader();
        if (outletHeader.getStatus() == Constants.CHECK_IN_VISIT) {
            btnAdd.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.GONE);
        }

        setAdapter();
        if (Helper.isEmptyOrNull(mList)) requestData();
    }

    public void requestData() {
        llNoData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressCircle.setVisibility(View.VISIBLE);
        new RequestUrl().execute();
    }

    private void getData() {
        mList = new ArrayList<>();
        mList = database.getAllOrder(outletHeader);
    }

    private class RequestUrl extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                getData();
                return true;
            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("Order", ex.getMessage());
                }
                return false;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressCircle.setVisibility(View.GONE);
            if (Helper.isEmptyOrNull(mList)) {
                recyclerView.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            } else {
                mAdapter.setData(mList);
                recyclerView.setVisibility(View.VISIBLE);
                llNoData.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(this, DailySalesmanActivity.class);
        startActivity(intent);
    }

    public void dialogConfirm(Order header) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final Dialog dialog = new Dialog(this);
        View dialogView = inflater.inflate(R.layout.aspp_dialog_confirmation, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(400, ViewGroup.LayoutParams.WRAP_CONTENT);//height => (4 * height) / 5
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDialog = dialog.findViewById(R.id.txtDialog);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        txtTitle.setText("Collection");
        txtDialog.setText("Apakah outlet ini akan melakukan pembayaran?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SessionManagerQubes.setOrder(header);
                SessionManagerQubes.setAlreadyPrint(true);
                SessionManagerQubes.setCollectionSource(3);
                Intent intent = new Intent(OrderActivity.this, CollectionFormActivity.class);
                startActivity(intent);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SessionManagerQubes.setOrder(header);
                Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }
}