package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.ChangePasswordRequest;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.User;

public class ChangeProfileFragment extends BaseFragment {
    private EditText edtOldPass, edtNewPass, edtConfPass;
    private Button btnSave,btnCancel;
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
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
//        ((MainActivityDrawer) getActivity()).setTitle(getString(R.string.navmenu7z));
//        ((MainActivityDrawer) getActivity()).enableBackToolbar(true);

        initialize();
        initProgress();

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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                setContent(fragment);
            }
        });

        return rootView;
    }

    private void initialize() {
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        btnSave = rootView.findViewById(R.id.btnSave);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        edtOldPass = rootView.findViewById(R.id.oldPassword);
        edtNewPass = rootView.findViewById(R.id.newPassword);
        edtConfPass = rootView.findViewById(R.id.confirmPassword);
        database = new DatabaseHelper(getContext());
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
                    Toast.makeText(getContext(), msg.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), msg.getMessage(), Toast.LENGTH_SHORT).show();
                    fragment = new ProfileFragment();
                    setContent(fragment);
                }
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.serverError), Toast.LENGTH_SHORT).show();
            }
        }
    }
}