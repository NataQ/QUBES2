package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.NewMainActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.ChangePasswordRequest;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.User;

public class ChangePasswordFragment extends BaseFragment {
    private EditText edtOldPass, edtNewPass, edtConfPass;
    private Button btnSave;
    private ImageView btnCancel;
    private User user;
    private ChangePasswordRequest changePasswordRequest;
    DatabaseHelper database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_change_password, container, false);

        initialize();
        init();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwd = edtOldPass.getText().toString().trim();
                String newPwd = edtNewPass.getText().toString().trim();
                String confirmPwd = edtConfPass.getText().toString().trim();

                if (oldPwd.trim().equals("")) {
                    edtOldPass.setError(getResources().getString(R.string.pleaseFillOldPass));
                } else if (newPwd.trim().equals("")) {
                    edtNewPass.setError(getResources().getString(R.string.pleaseFillNewPass));
                } else if (confirmPwd.trim().equals("")) {
                    edtConfPass.setError(getResources().getString(R.string.pleaseFillConfPass));
                } else if (!newPwd.equals(confirmPwd)) {
                    edtConfPass.setError(getResources().getString(R.string.passwordNotMatched));
                } else {
                    changePasswordRequest = new ChangePasswordRequest();
                    changePasswordRequest.setUsername(user.getNik().concat("|000"));
                    changePasswordRequest.setClient("000");
                    changePasswordRequest.setOldPassword(oldPwd);
                    changePasswordRequest.setPassword(confirmPwd);
                    new RequestUrl().execute();
                }
            }
        });

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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewMainActivity) getActivity()).changePage(5);
            }
        });
        
        return rootView;
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnSave = rootView.findViewById(R.id.btnSave);
        edtOldPass = rootView.findViewById(R.id.oldPassword);
        edtNewPass = rootView.findViewById(R.id.newPassword);
        edtConfPass = rootView.findViewById(R.id.confirmPassword);
        database = new DatabaseHelper(getActivity());
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestUrl extends AsyncTask<Void, Void, MessageResponse> {

        @Override
        protected MessageResponse doInBackground(Void... voids) {
            try {
                final String url = Constants.URL.concat(Constants.API_PROFILE_CHANGE_PASSWORD);
                return (MessageResponse) Helper.postWebserviceWithBody(url, MessageResponse.class, changePasswordRequest);
            } catch (Exception ex) {
                Log.e("ChangePasswordActivity", ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(MessageResponse msg) {
            progress.dismiss();
            if (msg != null) {
                //kalau berhasil
                if (msg.getIdMessage() == 0) {
                    Toast.makeText(getActivity(), msg.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), msg.getMessage(), Toast.LENGTH_SHORT).show();
                    ((NewMainActivity) getActivity()).changePage(5);
                }
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.serverError), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "7");
    }
}