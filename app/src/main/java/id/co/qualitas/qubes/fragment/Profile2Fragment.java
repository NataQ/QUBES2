package id.co.qualitas.qubes.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.NewMainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;

public class Profile2Fragment extends BaseFragment {
    private EditText edtName, edtEmail, edtPhone;
    private Spinner spinnerGender;
    private Button btnSave;
    private String[] gender = new String[2];
    private ImageView imgBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_profile, container, false);
        initialize();
        initFragment();

        gender[0] = "Male";
        gender[1] = "Female";
        setSpinnerAdapter3(gender, spinnerGender);

        edtName.setText(user.getFullName()!= null ?user.getFullName() :"-");
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((NewMainActivity) getActivity()).changePage(5);
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NewMainActivity) getActivity()).changePage(5);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "6");
    }

    private void initialize() {
        imgBack = rootView.findViewById(R.id.imgBack);
        edtName = rootView.findViewById(R.id.edtName);
        edtEmail = rootView.findViewById(R.id.edtEmail);
        spinnerGender = rootView.findViewById(R.id.spinnerGender);
        edtPhone = rootView.findViewById(R.id.edtPhone);
    }
}