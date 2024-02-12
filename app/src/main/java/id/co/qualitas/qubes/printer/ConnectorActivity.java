package id.co.qualitas.qubes.printer;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.datecs.printer.Printer;
import com.datecs.printer.ProtocolAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.BaseActivity;
import id.co.qualitas.qubes.activity.aspp.CollectionActivity;
import id.co.qualitas.qubes.activity.aspp.CollectionFormActivity;
import id.co.qualitas.qubes.activity.aspp.OrderActivity;
import id.co.qualitas.qubes.activity.aspp.OrderDetailActivity;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.StockRequest;
import id.co.qualitas.qubes.session.SessionManagerQubes;

public class ConnectorActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ConnectorAdapter.OnItemClickListener {
    private static final int REQUEST_PRINTER = 0;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private static final String PREF_HOST_LIST = "hosts";
    private static final String PREF_SEARCH_PATTERN = "search_pattern";
    private static final String TAG = "PrinterDemo";
    private SharedPreferences mPreferences;
    private RecyclerView mConnectorView;
    private SwipeRefreshLayout mSwipeLayout;
    private List<AbstractConnector> mConnectorList;
    private ConnectorAdapter mConnectorAdapter;
    public static final int PERMISSION_BLUETOOTH = 1;
    public static final int PERMISSION_BLUETOOTH_ADMIN = 2;
    public static final int PERMISSION_BLUETOOTH_CONNECT = 3;
    public static final int PERMISSION_BLUETOOTH_SCAN = 4;
    private static final Handler mHandler = new Handler();
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aspp_activity_connector);

        imgBack = findViewById(R.id.imgBack);
        imgLogOut = findViewById(R.id.imgLogOut);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_BLUETOOTH);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_BLUETOOTH_ADMIN);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_BLUETOOTH_CONNECT);
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, PERMISSION_BLUETOOTH_SCAN);
            }
        } else {
            setPrinterList();
        }

        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imgLogOut.setOnClickListener(v -> {
            logOut(ConnectorActivity.this);
        });
    }

    private void setPrinterList() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mConnectorList = new ArrayList<>();
        mConnectorAdapter = new ConnectorAdapter(ConnectorActivity.this, mConnectorList, this);

        mConnectorView = findViewById(R.id.list);
        mConnectorView.setAdapter(mConnectorAdapter);

        ItemTouchHelper.Callback callback = new ConnectorSwipeHelper(mConnectorAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mConnectorView);

        mSwipeLayout = findViewById(R.id.swipe_container);
        assert mSwipeLayout != null;
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // When activity is started from application launcher probably we want Bluetooth to be enabled.
//        if (Intent.ACTION_MAIN.equals(getIntent().getAction())) {
        grandLocationPermission();
        enableBluetooth();
//        }

        // Register receiver to notify when USB device is detached.
        registerReceiver(mUsbDeviceDetachedReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));
        // Register receiver to notify when Bluetooth state is changed.
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        bluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBluetoothReceiver, bluetoothFilter);

        initConnector();

        if (SessionManagerQubes.getPrinter() != null) {
            String desc = SessionManagerQubes.getPrinter();
            connectPrinter(desc.replace("\"", ""));
        }
    }

    private void connectPrinter(String desc) {
        BluetoothDevice mBluetoothDevice;
        BluetoothManager bluetoothManager = null;
        BluetoothAdapter adapter1;
        BluetoothConnector connector1;

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to device...");
        dialog.setCancelable(true);
        dialog.show();

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothDevice = bluetoothManager.getAdapter().getRemoteDevice(desc);
                adapter1 = BluetoothAdapter.getDefaultAdapter();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (mBluetoothDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                    connector1 = new BluetoothLeConnector(this, adapter1, mBluetoothDevice);
                } else {
                    connector1 = new BluetoothSppConnector(this, adapter1, mBluetoothDevice);
                }

                if (adapter1 != null && adapter1.isDiscovering()) {
                    adapter1.cancelDiscovery();
                }

                final Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            try {
                                connector1.connect();
                            } catch (Exception e) {
                                fail("Connection error: " + e.getMessage());
                                return;
                            }

                            try {
                                PrinterManager.instance.init(connector1);
                            } catch (Exception e) {
                                try {
                                    connector1.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                fail("Pinpad error: " + e.getMessage());
                                return;
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initPrinter();
                                    printText();//pernah save printer sebelumnya
                                    onBackPressed();
                                }
                            });

                        } finally {
                            dialog.dismiss();
                        }
                    }
                });
                thread.start();
            } else {
                Toast.makeText(this, "Please update your android version", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        } catch (Exception e) {
//            setToast("");
            dialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        // Don't forget to unregister receivers.
        unregisterReceiver(mUsbDeviceDetachedReceiver);
        unregisterReceiver(mBluetoothReceiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) {
            UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            AbstractConnector connector = new UsbDeviceConnector(this, manager, device);
            mConnectorAdapter.addFirst(connector);
            mConnectorView.smoothScrollToPosition(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            initProgress();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRefresh() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            adapter.startDiscovery();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeLayout.setRefreshing(false);
                }
            });
        }
        mConnectorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, final AbstractConnector item) {
        connectToPrinter(item);
    }

    private void connectToPrinter(AbstractConnector item) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting to device...");
        dialog.setCancelable(false);
        dialog.show();

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (adapter != null && adapter.isDiscovering()) {
            adapter.cancelDiscovery();
        }

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try {
                        item.connect();
                    } catch (Exception e) {
                        fail("Connection error: " + e.getMessage());
                        onBackPressed();
                    }

                    try {
                        PrinterManager.instance.init(item);
                    } catch (Exception e) {
                        try {
                            item.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        fail("Pinpad error: " + e.getMessage());
                        onBackPressed();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (item instanceof NetworkConnector) {
                                NetworkConnector connector = (NetworkConnector) item;
                                String host = connector.getHost();
                                int port = connector.getPort();
                                SessionManagerQubes.setUrl(String.valueOf(port));
                            } else if (item instanceof UsbDeviceConnector) {
                                UsbDeviceConnector connector = (UsbDeviceConnector) item;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    String name = connector.getDevice().getProductName();
                                }
                                String device = connector.getDevice().getDeviceName();
                            } else if (item instanceof BluetoothConnector) {
                                BluetoothConnector connector = (BluetoothConnector) item;
                                if (ActivityCompat.checkSelfPermission(ConnectorActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                String name = connector.getBluetoothDevice().getName();
                                String desc = connector.getBluetoothDevice().getAddress().replace("\"", "");
                                SessionManagerQubes.setPrinter(desc);
                            } else {
                                throw new IllegalArgumentException("Invalid connector");
                            }
                            initPrinter();
                            printText();
                        }
                    });
                } finally {
                    dialog.dismiss();
                }
            }
        });
        thread.start();
    }

    public void grandLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    private void fail(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initConnector() {
        mConnectorAdapter.clear();

        // Enumerate all network devices.
        Set<String> hostList = mPreferences.getStringSet(PREF_HOST_LIST, new HashSet<String>());
        for (String url : hostList) {
            int delimiter = url.indexOf(":");
            String host = url.substring(0, delimiter > 0 ? delimiter : url.length());
            int port = Integer.parseInt(url.substring(delimiter > 0 ? delimiter + 1 : 0));
            AbstractConnector connector = new NetworkConnector(this, host, port);
            mConnectorAdapter.addLast(connector);//NetworkConnector
            mConnectorView.smoothScrollToPosition(0);
        }

        // Enumerate USB devices
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if (manager != null) {
            HashMap<String, UsbDevice> deviceList = manager.getDeviceList();

            for (UsbDevice device : deviceList.values()) {
                if (manager.hasPermission(device)) {
                    AbstractConnector connector = new UsbDeviceConnector(this, manager, device);
                    mConnectorAdapter.addLast(connector);//UsbDeviceConnector
                    mConnectorView.smoothScrollToPosition(0);
                }
            }
        }

        // Enumerate Bluetooth devices
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> boundedDevices = adapter.getBondedDevices();

            for (BluetoothDevice device : boundedDevices) {
                AbstractConnector connector;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                        connector = new BluetoothLeConnector(this, adapter, device);
                    } else {
                        connector = new BluetoothSppConnector(this, adapter, device);
                    }
                } else {
                    connector = new BluetoothSppConnector(this, adapter, device);
                }
                mConnectorAdapter.addLast(connector);//BluetoothLeConnector (BluetoothSppConnector,BluetoothSppConnector)
                mConnectorView.smoothScrollToPosition(0);
            }
        }

//        String searchPattern = mSearchView.getText().toString();
//        mConnectorAdapter.applySearchPattern(searchPattern);
//        mConnectorAdapter.notifyDataSetChanged();
    }

    private void enableBluetooth() {
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if (adapter != null && !adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private class UsbDeviceDetachedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) {
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                int position = 0;

                for (AbstractConnector connector : mConnectorList) {
                    if (connector instanceof UsbDeviceConnector) {
                        UsbDeviceConnector usbDeviceConnector = (UsbDeviceConnector) connector;

                        if (usbDeviceConnector.getDevice().equals(device)) {
                            mConnectorAdapter.remove(position);
                            break;
                        }
                    }
                    position++;
                }
            }
        }
    }

    private final UsbDeviceDetachedReceiver mUsbDeviceDetachedReceiver = new UsbDeviceDetachedReceiver();

    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        initProgress();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        initProgress();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                // Nothing to do here
                System.out.println("Bluetooth discovery is started");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                System.out.println("Bluetooth discovery is finished");
                if (mSwipeLayout.isRefreshing()) {
                    mSwipeLayout.setRefreshing(false);
                }
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                System.out.println("Bluetooth device is found");
                AbstractConnector connector;
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) -255);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (ActivityCompat.checkSelfPermission(ConnectorActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                        connector = new BluetoothLeConnector(context, adapter, device);
                    } else {
                        connector = new BluetoothSppConnector(context, adapter, device);
                    }
                } else {
                    connector = new BluetoothSppConnector(context, adapter, device);
                }
                // Do not duplicate devices.
                if (!mConnectorList.contains(connector)) {
                    mConnectorAdapter.addFirst(connector);
                    mConnectorView.smoothScrollToPosition(0);
                }
                if (rssi != -255) {
                    mConnectorAdapter.updateSignalStrength(connector, rssi);
                }
            }
        }
    }

    private final BluetoothReceiver mBluetoothReceiver = new BluetoothReceiver();

    private class ConnectorSwipeHelper extends ItemTouchHelper.SimpleCallback {
        private ConnectorAdapter mConnectorAdapter;

        public ConnectorSwipeHelper(ConnectorAdapter movieAdapter) {
            super(0, ItemTouchHelper.RIGHT);
            this.mConnectorAdapter = movieAdapter;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //TODO: Not implemented here
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            AbstractConnector connector = mConnectorList.get(position);
            // Removed network device from list
            if (connector instanceof NetworkConnector) {
                NetworkConnector networkConnector = (NetworkConnector) connector;
                Set<String> hostList = mPreferences.getStringSet(PREF_HOST_LIST, new HashSet<String>());
                String url = networkConnector.getHost() + ":" + networkConnector.getPort();
                if (hostList.remove(url)) {
                    Set<String> tmpHostList = new HashSet<>(hostList);
                    mPreferences.edit().putStringSet(PREF_HOST_LIST, tmpHostList).apply();
                }
            }
            mConnectorAdapter.remove(position);
        }
    }

    public void initPrinter() {
        ProtocolAdapter protocolAdapter = PrinterManager.instance.getProtocolAdapter();
        if (protocolAdapter.isProtocolEnabled()) {
            protocolAdapter.setPrinterListener(new ProtocolAdapter.PrinterListener() {
                @Override
                public void onThermalHeadStateChanged(boolean overheated) {
                    if (overheated) {
                        Log.d(TAG, "Thermal head is overheated");
                        status("OVERHEATED");
                    } else {
                        status(null);
                    }
                }

                @Override
                public void onPaperStateChanged(boolean noPaper) {
                    if (noPaper) {
                        Log.d(TAG, "Event: Paper out");
                        status("PAPER OUT");
                    } else {
                        status(null);
                    }
                }

                @Override
                public void onBatteryStateChanged(boolean lowBattery) {
                    if (lowBattery) {
                        Log.d(TAG, "Low battery");
                        status("LOW BATTERY");
                    } else {
                        status(null);
                    }
                }
            });
        }

//        RC663 rc663 = PrinterManager.instance.getRC663();
//        if (rc663 != null) {
//            rc663.setCardListener(new RC663.CardListener() {
//                @Override
//                public void onCardDetect(ContactlessCard card) {
//                    processContactlessCard(card);
//                }
//            });
//        }

        Printer printer = PrinterManager.instance.getPrinter();
        printer.setConnectionListener(new Printer.ConnectionListener() {
            @Override
            public void onDisconnect() {
                warning("Printer is disconnected");
                finish();
            }
        });
    }

    private void warning(final String text) {
        Log.d(TAG, text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void status(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (text != null) {
                    Toast.makeText(ConnectorActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private interface PrinterRunnable {
        void run(ProgressDialog dialog, Printer printer) throws IOException;
    }

    private void printText() {
        setFormatSeparator();
        Log.d(TAG, "Print Text");

        runTask(new PrinterRunnable() {
            @Override
            public void run(ProgressDialog dialog, Printer printer) throws IOException {
                StringBuffer textBuffer = new StringBuffer();
//                  textBuffer.append("{reset}{center}{w}{h}Yasha Kishi");
//                textBuffer.append("{br}");
//                textBuffer.append("{reset}1. {b}First item{br}");
//                textBuffer.append("{reset}{right}{h}$0.50 A{br}");
//                textBuffer.append("{reset}2. {u}Second item{br}");
//                textBuffer.append("{reset}{right}{h}$1.00 B{br}");
//                textBuffer.append("{reset}3. {i}Third item{br}");
//                textBuffer.append("{reset}{right}{h}$1.50 C{br}");
//                textBuffer.append("{reset}{right}{w}{h}TOTAL: {/w}$3.00  {br}");
//                textBuffer.append("{reset}{center}{s}Thank You!{br}");
                //max character 32

                order = SessionManagerQubes.getOrder();

                Customer cust = database.getDetailCustomer(order.getId_customer());
                StockRequest stock = database.getDetailStockRequest(order.getIdStockHeaderDb());

                String no = String.valueOf(order.getIdHeader());
                String id = Helper.isEmpty(order.getId_customer(), "-");
                String name = Helper.isEmpty(cust.getNama(), "-");
                String address = Helper.isEmpty(cust.getAddress(), "-");
                String noSJ = Helper.isEmpty(stock.getNo_surat_jalan(), "-");
                String payment = order.isStatusPaid() ? "Cash" : "Kredit";

                textBuffer.append("{reset}{center}{b}PT. ASIASEJAHTERAPERDANA P{br}");
                textBuffer.append("{reset}{center}Jl.Gatot Subroto Kav. 99{br}");
                textBuffer.append("{reset}{center}Jakarta Selatan{br}{br}");
                textBuffer.append("{reset}{center}{b}Tanda Terima Barang{br}{br}");
                textBuffer.append("{reset}No.     : " + no + "{br}");
                textBuffer.append("{reset}Tgl.    : " + Helper.getTodayDate(Constants.DATE_FORMAT_6) + "{br}");
                textBuffer.append("{reset}Toko    : " + name + "{br}");
                textBuffer.append("{reset}Alamat  : " + address + "{br}");
                textBuffer.append("{reset}No.Cust : " + id + "{br}");
                textBuffer.append("{reset}No.SJ   : " + noSJ + "{br}");
                textBuffer.append("{reset}Payment : " + payment + "{br}");
                textBuffer.append("{reset}================================{br}");


                List<Material> mList = new ArrayList<>();
                mList.addAll(database.getAllDetailOrder(order));

                double totalDiscount = 0.0, totalPrice = 0.0;
                for (Material mat : mList) {
                    textBuffer.append("{reset}" + mat.getId() + " - " + mat.getNama() + "{br}");
                    String qtyUom = format.format(mat.getQty()) + " " + mat.getUom();
                    String price = format.format(mat.getPrice());
                    String priceSpace = "";
//                    while (qtyUom.length() < 20) qtyUom.concat(" ");
//                    for (int i = qtyUom.length(); i < 20; i++) {
//                        qtyUom.concat(" ");
//                    }
                    qtyUom = String.format("%-17s", qtyUom);
                    priceSpace = String.format("%1$" + ((11 - price.length()) + priceSpace.length()) + "s", priceSpace) + price;

//                    while (priceSpace.length() < (12 - price.length())) priceSpace.concat(" ");
                    textBuffer.append("{reset}" + qtyUom + "Rp. " + priceSpace + "{br}");
                    textBuffer.append("{reset}{left}Disc {/left}{reset}{right}Rp. " + format.format(mat.getTotalDiscount()) + "{/right}{br}");
                    totalPrice = totalPrice + mat.getPrice();
                    totalDiscount = totalDiscount + mat.getTotalDiscount();
                    if (mat.getExtraItem() != null) {
                        for (Material matExtra : mat.getExtraItem()) {
                            textBuffer.append("{reset}" + matExtra.getId() + " - " + matExtra.getNama() + "[PROMO]{br}");
                            textBuffer.append("{reset}" + format.format(matExtra.getQty()) + " " + matExtra.getUom() + "{reset}{right}Rp. " + matExtra.getPrice() + "{br}");
//                            textBuffer.append("{reset}{right}Disc Rp. " + format.format(matExtra.getTotalDiscount()) + "{br}");
                            totalPrice = totalPrice + matExtra.getPrice();
                            totalDiscount = totalDiscount + matExtra.getTotalDiscount();
                        }
                    }
                }
                textBuffer.append("{reset}--------------------------------{br}");
                String subTotPrice = format.format(totalPrice);
                String totDiscount = format.format(totalDiscount);
                String total = format.format(totalPrice - totalDiscount);
                String nameSales = SessionManagerQubes.getUserProfile().getFull_name();

                if ((nameSales.length() % 2) == 1) {
                    nameSales = String.format("%" + (nameSales.length() + 1) + "s", nameSales).replace(' ', ' ');//left
                }
                int lengthToko = ((16 - name.length()) / 2) + name.length();
                int lengthName = ((16 - nameSales.length()) / 2) + nameSales.length();

                nameSales = String.format("%" + lengthName + "s", nameSales).replace(' ', ' ');//bf
                nameSales = String.format("%-16s", nameSales).replace(' ', ' ');//af
                name = String.format("%" + lengthToko + "s", name).replace(' ', ' ');//bf
                name = String.format("%-16s", name).replace(' ', ' ');//af
                subTotPrice = String.format("%1$" + 12 + "s", subTotPrice);
                totDiscount = String.format("%1$" + 12 + "s", totDiscount);
                total = String.format("%1$" + 12 + "s", total);
//                String.format("%[L]s", str).replace(' ', ch);//left

//                String.format("%-[L]s", str).replace(' ', ch);//right

                textBuffer.append("{reset}     Sub Total : Rp." + subTotPrice + "{br}");
                textBuffer.append("{reset}     Discount  : Rp." + totDiscount + "{br}");
                textBuffer.append("{reset}     Total     : Rp." + total + "{br}");
                textBuffer.append("{reset}{center}TANDA TANGAN / STEMPEL{br}");
                textBuffer.append("{br}{br}{br}{br}{br}");
                textBuffer.append("{reset}" + nameSales + name + "{br}");

                printer.reset();
                printer.printTaggedText(textBuffer.toString());
                printer.feedPaper(110);
                printer.flush();
            }
        }, R.string.msg_printing_text);
    }

    private void error(final String text) {
        Log.w(TAG, text);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
    }

    private void runTask(final PrinterRunnable r, final int msgResId) {
        final ProgressDialog dialog = new ProgressDialog(ConnectorActivity.this);
        dialog.setTitle(getString(R.string.pleaseWait));
        dialog.setMessage(getString(msgResId));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final boolean[] errorPrint = {false};
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Printer printer = PrinterManager.instance.getPrinter();
                    r.run(dialog, printer);
                } catch (IOException e) {
                    errorPrint[0] = true;
                    e.printStackTrace();
                    error("I/O error occurs: " + e.getMessage());
                } catch (Exception e) {
                    errorPrint[0] = true;
                    e.printStackTrace();
                    error("Critical error occurs: " + e.getMessage());
                    finish();
                } finally {
                    if (!errorPrint[0]) {
                        updateMaxPrint();
                    }
                    dialog.dismiss();
                }
            }
        });
        t.start();
    }

    private void updateMaxPrint() {
        int printOrder = order.getPrintOrder() + 1;
        Map param = new HashMap();
        param.put("id", order.getIdHeader());
        param.put("print", printOrder);
        param.put("username", user.getUsername());
        database.updatePrint(param);

        if (printOrder < database.getMaxPrint()) {
            runOnUiThread(new Runnable() {
                public void run() {
                    openDialogConfirmation();
                }
            });
        } else {
            setToast("Sudah mencapai maksimal print");
        }
    }

    private void openDialogConfirmation() {
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
        txtTitle.setText("Print");
        txtDialog.setText("Print again?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                printText();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
    }
}
