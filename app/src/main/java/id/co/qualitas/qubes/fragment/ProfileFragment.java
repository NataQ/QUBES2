package id.co.qualitas.qubes.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.LoginActivity;
import id.co.qualitas.qubes.activity.MainActivityDrawer;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.MessageResponse;
import id.co.qualitas.qubes.model.User;

public class ProfileFragment extends BaseFragment {
    private TextView txtNameProfile, txtUserNumber, txtJobStatus, txtNameSpvProfile, txtUserSpvNumber, txtJobStatusSpv;
    private LinearLayout linearSpv;
    private Button btnChangePassword, btnLogout;
    private User user = new User();
    DatabaseHelper database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_check_profile, container, false);
        getActivity().setTitle(getString(R.string.navmenu7));
        ((MainActivityDrawer) getActivity()).enableBackToolbar(false);
        init();
        initialize();
        setData();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChangeProfileFragment();
                setContent(fragment);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View dialogView;
                final Dialog alertDialog = new Dialog(getContext());
                dialogView = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setContentView(dialogView);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btnCancel = alertDialog.findViewById(R.id.btnCancel);
                Button btnSave = alertDialog.findViewById(R.id.btnSave);
                TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);

                txtDialog.setText(getResources().getString(R.string.textDialogLogout));
                btnSave.setText(getResources().getString(R.string.yes));
                btnCancel.setText(getResources().getString(R.string.no));

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Fragment fr = new ProfileFragment();
                        setContent(fr);
                        alertDialog.dismiss();

                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new RequestUrl().execute();
                        progress.show();

                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        return rootView;
    }

    private void initialize() {
        database = new DatabaseHelper(getContext());
        txtNameProfile = rootView.findViewById(R.id.txtNameProfile);
        txtUserNumber = rootView.findViewById(R.id.txtUserNumber);
        txtJobStatus = rootView.findViewById(R.id.txtJobStatus);
        txtNameSpvProfile = rootView.findViewById(R.id.txtNameSpvProfile);
        txtUserSpvNumber = rootView.findViewById(R.id.txtUserSpvNumber);
        txtJobStatusSpv = rootView.findViewById(R.id.txtJobSpvStatus);
        linearSpv = rootView.findViewById(R.id.linearSpv);
        btnChangePassword = rootView.findViewById(R.id.btnChangePassword);
        btnLogout = rootView.findViewById(R.id.btnLogout);
        user = (User) Helper.getItemParam(Constants.USER_DETAIL);

        if (user != null) {
            if (user.getReason() != 2) {
                btnChangePassword.setEnabled(false);
                btnChangePassword.setBackgroundColor(getResources().getColor(R.color.darkGray));
            }

            if (user.getReason() != 0) {
                if(user.getSupervisorName() != null && user.getSupervisorNik() != null && user.getSupervisorPosition() != null){
//                    linearSpv.setVisibility(View.VISIBLE);
                    txtNameSpvProfile.setVisibility(View.VISIBLE);
                    txtUserSpvNumber.setVisibility(View.VISIBLE);
                    txtJobStatusSpv.setVisibility(View.VISIBLE);
                    txtNameSpvProfile.setText(user.getSupervisorName());
                    txtUserSpvNumber.setText(user.getSupervisorNik());
                    txtJobStatusSpv.setText(user.getSupervisorPosition());
                }
            } else {
//                linearSpv.setVisibility(View.GONE);
                txtNameSpvProfile.setVisibility(View.GONE);
                txtUserSpvNumber.setVisibility(View.GONE);
                txtJobStatusSpv.setVisibility(View.GONE);
            }
        }
    }

    private void setData() {
        if (user != null) {
            txtNameProfile.setText(Helper.validateResponseEmpty(user.getFullName()));
            txtUserNumber.setText(Helper.validateResponseEmpty(user.getNik()));
            txtJobStatus.setText(Helper.validateResponseEmpty(user.getPosition()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button
                    setContentToHome();
                    return true;
                }
                return false;
            }
        });
    }

    private class  RequestUrl extends AsyncTask<Void, Void, MessageResponse> {

        @Override
        protected MessageResponse doInBackground(Void... voids) {
            try {
                String URL_GET_HOME_LIST = Constants.API_LOGOUT.concat(Constants.QUESTION).concat(Constants.USERNAME.concat(Constants.EQUALS))
                        + user.getNik().concat(getString(R.string.clientId));

                final String url = Constants.URL.concat(URL_GET_HOME_LIST);
                return (MessageResponse) Helper.getWebservice(url, MessageResponse.class);

            } catch (Exception ex) {
                if (ex.getMessage() != null) {
                    Log.e("LoginActivity", ex.getMessage());
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(MessageResponse messageResponse) {
            progress.dismiss();
            if (messageResponse != null) {
                if (messageResponse.getIdMessage() == 1) {

                    /*newest6Nov*/
                    database.deleteAttendance();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), messageResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.errCon), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
