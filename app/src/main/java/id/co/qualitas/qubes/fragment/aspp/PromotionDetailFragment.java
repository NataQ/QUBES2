package id.co.qualitas.qubes.fragment.aspp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.activity.aspp.SummaryDetailActivity;
import id.co.qualitas.qubes.adapter.aspp.SummaryAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.fragment.BaseFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class PromotionDetailFragment extends BaseFragment {
    private TextView txtTitle, txtDesc, txtPeriode, txtNoPromo;
    private Promotion header;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.aspp_fragment_promotion_detail, container, false);
        getActivity().setTitle("Promotion Detail");

        initProgress();
        initFragment();
        initialize();

        imgBack.setOnClickListener(v -> {
            ((MainActivity) getActivity()).changePage(1);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    setEnabled(false);
                    ((MainActivity) getActivity()).changePage(1);
//                    ((MainActivity) getActivity()).backPress();
                }
            }
        });

        return rootView;
    }

    private void initialize() {
        database = new Database(getContext());
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        imgBack = rootView.findViewById(R.id.imgBack);
        txtTitle = rootView.findViewById(R.id.txtTitle);
        txtPeriode = rootView.findViewById(R.id.txtPeriode);
        txtNoPromo = rootView.findViewById(R.id.txtNoPromo);
        txtDesc = rootView.findViewById(R.id.txtDesc);
    }

    @Override
    public void onResume() {
        super.onResume();
        setFirstData();
        Helper.setItemParam(Constants.CURRENTPAGE, "6");
    }

    private void setFirstData() {
        if (Helper.getItemParam(Constants.DETAIL_PROMOTION) != null) {
            header = (Promotion) Helper.getItemParam(Constants.DETAIL_PROMOTION);

            String validFrom = null, validTo = null;
            if (!Helper.isNullOrEmpty(header.getValid_from())) {
                validFrom = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getValid_from());
            }

            if (!Helper.isNullOrEmpty(header.getValid_to())) {
                validTo = Helper.changeDateFormat(Constants.DATE_FORMAT_3, Constants.DATE_FORMAT_5, header.getValid_to());
            }
            String noPromo = Helper.isEmpty(header.getNo_promo(), "");
            String namaPromo = Helper.isEmpty(header.getNama_promo(), "");
            String descPromo = Helper.isEmpty(header.getKeterangan(), "");

            txtTitle.setText(namaPromo);
            txtDesc.setText(descPromo);
            txtNoPromo.setText(noPromo);
            txtPeriode.setText(validFrom + " - " + validTo);
        } else {
            setToast("Gagal mendapatkan promosi");
            ((MainActivity) getActivity()).changePage(1);
        }
    }
}