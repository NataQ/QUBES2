package id.co.qualitas.qubes.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.fragment.RetailOutletFragment;
import id.co.qualitas.qubes.helper.GPSTracker;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.SecureDate;
import id.co.qualitas.qubes.model.CheckInOutRequest;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.User;

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.DataObjectHolder> {
    private List<OutletResponse> mDataset;
    private RetailOutletFragment mcontext;
    private GPSTracker gpsTracker;
    private int wall = 0;

    class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView txtOutletName, txtOutletCode, index, txtOutletAddress;
//        View line;
        LinearLayout LLOutlets;
        ImageView outletStatus;
        User user = new User();

        DataObjectHolder(View itemView) {
            super(itemView);
            gpsTracker = new GPSTracker(mcontext.getContext());
            index = itemView.findViewById(R.id.index);
            txtOutletName = itemView.findViewById(R.id.txtOutletName);
            txtOutletCode = itemView.findViewById(R.id.txtOutetCode);
            txtOutletAddress = itemView.findViewById(R.id.txtOutletAddress);
            LLOutlets = itemView.findViewById(R.id.LLOutlets);
            outletStatus = itemView.findViewById(R.id.outletStatus);
//            line = itemView.findViewById(R.id.line);
            user = (User) Helper.getItemParam(Constants.USER_DETAIL);
        }
    }

    public OutletAdapter(ArrayList<OutletResponse> myDataset, RetailOutletFragment retailOutletFragment) {
        mDataset = myDataset;
        mcontext = retailOutletFragment;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_outlet_n, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final OutletResponse outletResponse = mDataset.get(position);
        holder.txtOutletName.setId(position);
        holder.txtOutletCode.setId(position);
        holder.outletStatus.setId(position);

        holder.index.setText(String.valueOf(position + 1));

        if (outletResponse.getOutletName() != null) {
            holder.txtOutletName.setText(outletResponse.getOutletName());
        }

        if (outletResponse.getIdOutlet() != null) {
            holder.txtOutletCode.setText(outletResponse.getIdOutlet());
        }

        if (outletResponse.getStreet1() != null) {
            holder.txtOutletAddress.setText(outletResponse.getStreet1());
        }

        if (outletResponse.getStatusCheckIn() != null) {
            if (!outletResponse.isEnabled() && outletResponse.getStatusCheckIn().equals(Constants.UNCHECKED_IN)) {
                holder.outletStatus.setImageResource(R.drawable.ic_action_close);
                holder.outletStatus.setEnabled(true);
                holder.outletStatus.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("InflateParams")
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = LayoutInflater.from(mcontext.getContext());
                        View dialogview;
                        final Dialog alertDialog = new Dialog(mcontext.getContext());
                        dialogview = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        alertDialog.setContentView(dialogview);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        Button btnCancel = alertDialog.findViewById(R.id.btnCancel);
                        Button btnSave = alertDialog.findViewById(R.id.btnSave);

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mcontext.deleteVisit(outletResponse.getId(), outletResponse.getIdOutlet());
                                mDataset.clear();
                                notifyDataSetChanged();
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.show();
                    }
                });
            }
        }


        if (outletResponse.getStatusCheckIn() != null) {
            switch (outletResponse.getStatusCheckIn()) {
                case Constants.CHECK_IN:
                    holder.outletStatus.setImageResource(R.drawable.ic_checked_in);
                    break;
                case Constants.PAUSE:
                    holder.outletStatus.setImageResource(R.drawable.ic_paused);
                    break;
                case Constants.RESUME:
                    holder.outletStatus.setImageResource(R.drawable.ic_checked_in);
                    break;
                case Constants.FINISHED:
                    holder.outletStatus.setImageResource(R.drawable.ic_visited);
                    break;
                case Constants.UNCHECKED_IN:
                    if (!outletResponse.isEnabled()) {
                        holder.outletStatus.setImageResource(R.drawable.ic_action_close);
                    }
                default:
                    if (outletResponse.isEnabled()) {
                        holder.outletStatus.setImageResource(0);
                    }
                    break;
            }
        }

        holder.LLOutlets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outletResponse.getStatusCheckIn() != null) {
                    for (int i = 0; i < mDataset.size(); i++) {
                        if(mDataset.get(i).getStatusCheckIn() != null){
                            if (mDataset.get(i).getStatusCheckIn().equals(Constants.CHECK_IN) || mDataset.get(i).getStatusCheckIn().equals(Constants.RESUME)) {
                                wall++;
                            }
                        }
                    }

                    if (!outletResponse.getStatusCheckIn().equals(Constants.UNCHECKED_IN)) {//udah di checkin atau lagi di pause
                        CheckInOutRequest checkInOutRequest = new CheckInOutRequest();
                        checkInOutRequest.setIdOutlet(outletResponse.getIdOutlet());
                        checkInOutRequest.setOutletName(outletResponse.getOutletName());
                        checkInOutRequest.setStatusCheckIn(outletResponse.getStatusCheckIn());
                        checkInOutRequest.setTimer(outletResponse.getTimer());
                        checkInOutRequest.setCheckInTime(outletResponse.getCheckInTime());
                        checkInOutRequest.setPauseTime(outletResponse.getPause_time());
                        checkInOutRequest.setContinueTime(outletResponse.getContinue_time());
                        Helper.setItemParam(Constants.CHECK_IN_REQUEST, checkInOutRequest);

                        mcontext.changeFragment();
//                        Intent intent = new Intent(mcontext.getActivity(), Timer2Fragment.class);
//                        mcontext.getActivity().startActivity(intent);
//                        Fragment fr = new TimerFragment();
//                        mcontext.setContent(fr);
                    } else if (outletResponse.getStatusCheckIn().equals(Constants.FINISHED)) {//udah dikunjungi
//                        Fragment fr = new TimerFragment();
//                        mcontext.setContent(fr);
                        mcontext.changeFragment();
//                        Intent intent = new Intent(mcontext.getActivity(), Timer2Fragment.class);
//                        mcontext.getActivity().startActivity(intent);
                        Toast.makeText(mcontext.getContext(), R.string.sudahDiKunjungi, Toast.LENGTH_SHORT).show();
                    } else if (outletResponse.getStatusCheckIn().equals(Constants.UNCHECKED_IN) && wall > 0) {//bukan outlet yang checkin tapi uda ada yang di check
                        Toast.makeText(mcontext.getContext(), R.string.lagiKunjungan, Toast.LENGTH_SHORT).show();
                    } else {
                        if (!outletResponse.getOutletName().equals("")) {
                            if (gpsTracker.canGetLocation()) {
                                CheckInOutRequest checkInOutRequest = new CheckInOutRequest();
                                checkInOutRequest.setIdOutlet(outletResponse.getIdOutlet());
                                checkInOutRequest.setOutletName(outletResponse.getOutletName());
                                if (holder.user != null) {
                                    checkInOutRequest.setIdEmployee(holder.user.getIdEmployee());
                                }
                                checkInOutRequest.setLatCheckIn(String.valueOf(gpsTracker.getLatitude()));
                                checkInOutRequest.setLongCheckIn(String.valueOf(gpsTracker.getLongitude()));
                                Date curDate = SecureDate.getInstance().getDate();
                                checkInOutRequest.setCheckInTime(String.valueOf(curDate.getTime()));
                                checkInOutRequest.setStatusCheckIn(Constants.CHECK_IN);
                                checkInOutRequest.setTimer(outletResponse.getTimer());

                                Helper.removeItemParam(Constants.GET_DETAIL_VISIT);
                                Helper.setItemParam(Constants.CHECK_IN_REQUEST, checkInOutRequest);

                                mcontext.getLocation();
                            } else {
                                gpsTracker.showSettingsAlert();
                            }
                        } else {
                            Toast.makeText(mcontext.getContext(), "Data pada outlet ini bermasalah", Toast.LENGTH_SHORT).show();
                        }

                    }

                    switch (outletResponse.getStatusCheckIn()) {
                        case Constants.CHECK_IN:
                            mcontext.setPARAM_STATUS_OUTLET(Constants.CHECK_IN);
                            break;
                        case Constants.PAUSE:
                            mcontext.setPARAM_STATUS_OUTLET(Constants.PAUSE);
                            break;
                        case Constants.RESUME:
                            mcontext.setPARAM_STATUS_OUTLET(Constants.RESUME);
                            break;
                        case Constants.FINISHED:
                            mcontext.setPARAM_STATUS_OUTLET(Constants.FINISHED);
                            break;
                    }
                }
                wall = 0;

                Helper.setItemParam(Constants.OUTLET, outletResponse);
            }

        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}