package id.co.qualitas.qubes.activity.aspp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.adapter.aspp.OrderDetailAdapter;
import id.co.qualitas.qubes.adapter.aspp.SummaryDetailAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.User;

public class OrderDetailActivity extends BaseActivity {
    private OrderDetailAdapter mAdapter;
    private List<Material> mList, mListExtra;
    private ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_order_detail);

        init();
        initialize();
        initData();
        setDataDummy();

        mAdapter = new OrderDetailAdapter(this, mList, header -> {
        });

        recyclerView.setAdapter(mAdapter);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(OrderDetailActivity.this);
        });
    }

    private void setDataDummy() {
        String jsonFileString = Helper.getJsonFromAssets(getApplicationContext(), "discount.json");
//        Gson gson = new Gson();
//        Type listUserType = new TypeToken<List<ShipmentResponse>>() {
//        }.getType();
//
//        shipmentResponseList = new ArrayList<>();
//        shipmentResponseList = gson.fromJson(jsonFileString, listUserType);
//        bgTaskFilter(shipmentResponseList);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(jsonFileString);
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }

//        JSONObject json_array = args.optJSONObject(0);

        Iterator<?> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            System.out.println("Key: " + key);
            try {
                System.out.println("Value: " + jsonObject.get(key));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new Material("11_KTD R", "11008_KRATINGDAENG LUAR PULAU - MT", 1, "1,000", "BTL", initDataExtra()));
        mList.add(new Material("11_KTD R", "11007_KRATINGDAENG - MT", 1, "2,000", "BTL", initDataExtra()));
        mList.add(new Material("11_KTD R", "11006_KRATINGDAENG - LAIN-LAIN", 1, "3,000", "BTL", initDataExtra()));
        mList.add(new Material("11_KTD R", "11005_KRATINGDAENG LUAR PULAU", 1, "4,000", "BTL", initDataExtra()));
    }

    private List<Material> initDataExtra() {
        mListExtra = new ArrayList<>();
        mListExtra.add(new Material("11_KTD R", "11007_KRATINGDAENG - MT", 1, "2,000", "BTL"));
        mListExtra.add(new Material("11_KTD R", "11005_KRATINGDAENG LUAR PULAU", 1, "4,000", "BTL"));
        return mListExtra;
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        imgLogOut = findViewById(R.id.imgLogOut);
        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}