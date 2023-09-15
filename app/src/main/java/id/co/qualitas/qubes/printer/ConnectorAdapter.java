package id.co.qualitas.qubes.printer;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.R;

public class ConnectorAdapter extends RecyclerView.Adapter<ConnectorAdapter.ViewHolderItem> {
    public interface OnItemClickListener {
        void onItemClick(View view, AbstractConnector item);
    }

    private List<AbstractConnector> mOrigItems;
    private List<AbstractConnector> mShowItems;
    private OnItemClickListener mListener;
    private String mSearchPattern;
    private ConnectorActivity connectorActivity;

    public ConnectorAdapter(ConnectorActivity connectorActivity, List<AbstractConnector> items, OnItemClickListener l) {
        this.connectorActivity = connectorActivity;
        this.mOrigItems = items;
        this.mShowItems = new ArrayList<>(mOrigItems);
        this.mListener = l;
    }

    private final Map<AbstractConnector, Integer> mSignalStrengths = new HashMap();

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.aspp_row_view_connector, parent, false);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, final int position) {
        final AbstractConnector item = getItem(position);
        if (item instanceof NetworkConnector) {
            NetworkConnector connector = (NetworkConnector) item;
            holder.icon.setImageResource(R.drawable.ic_network);
            holder.name.setText("HOST: " + connector.getHost());
            holder.desc.setText("PORT: " + connector.getPort());
        } else if (item instanceof UsbDeviceConnector) {
            UsbDeviceConnector connector = (UsbDeviceConnector) item;
            holder.icon.setImageResource(R.drawable.ic_usb);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.name.setText(connector.getDevice().getProductName());
            } else {
                holder.name.setText("USB DEVICE");
            }
            holder.desc.setText(connector.getDevice().getDeviceName());
        } else if (item instanceof BluetoothConnector) {
            BluetoothConnector connector = (BluetoothConnector) item;
            if (ActivityCompat.checkSelfPermission(connectorActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (connector.getBluetoothDevice().getBondState() == BluetoothDevice.BOND_BONDED) {
                holder.icon.setImageResource(R.drawable.ic_bluetooth_paired);
            } else {
                holder.icon.setImageResource(R.drawable.ic_bluetooth);
            }
            String name = connector.getBluetoothDevice().getName();
            if (name == null || name.isEmpty()) {
                holder.name.setText("BLUETOOTH DEVICE");
            } else {
                holder.name.setText(connector.getBluetoothDevice().getName());
            }
            holder.desc.setText(connector.getBluetoothDevice().getAddress());
            Integer rssi = mSignalStrengths.get(connector);
            holder.status.setText(rssi != null ? (String.valueOf(rssi) + "dBm") : "");
        } else {
            throw new IllegalArgumentException("Invalid connector");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("OnClick");
                mListener.onItemClick(v, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mShowItems.size();
    }

    private AbstractConnector getItem(int position) {
        return mShowItems.get(position);
    }

    private boolean isVisible(AbstractConnector connector) {
        if (mSearchPattern.equals("")) {
            return true;
        }

        if (connector instanceof BluetoothConnector) {
            BluetoothConnector bc = (BluetoothConnector) connector;
            BluetoothDevice bd = bc.getBluetoothDevice();

            if (ActivityCompat.checkSelfPermission(connectorActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }
            String name = bd.getName();
            if (name != null && name.contains(mSearchPattern)) {
                return true;
            }

            String address = bd.getAddress();
            if (address != null && address.contains(mSearchPattern)) {
                return true;
            }

            return false;
        }

        return true;
    }

    public void clear() {
        mShowItems.clear();
        mOrigItems.clear();
        mSignalStrengths.clear();
        notifyDataSetChanged();
    }

    public void addLast(AbstractConnector connector) {
        mOrigItems.add(connector);
        mShowItems.add(connector);
        notifyItemInserted(mShowItems.size() - 1);
    }

    public void addFirst(AbstractConnector connector) {
        mOrigItems.add(0, connector);
        mShowItems.add(0, connector);
        notifyItemInserted(0);
    }

    public void remove(int position) {
        AbstractConnector connector = getItem(position);
        mShowItems.remove(connector);
        mOrigItems.remove(connector);
        notifyItemRemoved(position);
    }

    public void updateSignalStrength(AbstractConnector connector, int signalStrength) {
        mSignalStrengths.put(connector, signalStrength);
        Collections.sort(mOrigItems, new BluetoothLeComparator(mSignalStrengths));
        Collections.sort(mShowItems, new BluetoothLeComparator(mSignalStrengths));
        notifyDataSetChanged();
    }

    public void applySearchPattern(String searchPattern) {
        mSearchPattern = searchPattern;
        mShowItems = new ArrayList<>();
        for (AbstractConnector connector : mOrigItems) {
            if (isVisible(connector)) {
                mShowItems.add(connector);
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        public final ImageView icon;
        public final TextView name;
        public final TextView desc;
        public final TextView status;

        public ViewHolderItem(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            desc = (TextView) itemView.findViewById(R.id.description);
            status = (TextView) itemView.findViewById(R.id.status);
        }
    }

    public static class BluetoothLeComparator implements Comparator<AbstractConnector> {

        private Map<AbstractConnector, Integer> mValues;

        BluetoothLeComparator(Map<AbstractConnector, Integer> values) {
            this.mValues = values;
        }

        @Override
        public int compare(AbstractConnector l, AbstractConnector r) {
            Integer valueL = mValues.get(l);
            Integer valueR = mValues.get(r);

            if (valueL != null && valueR != null) {
                if (valueL > valueR) {
                    return -1;
                } else if (valueL < valueR) {
                    return 1;
                }
            }

            return 0;
        }
    }
}