package id.co.qualitas.qubes.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.activity.aspp.MainActivity;
import id.co.qualitas.qubes.adapter.CreateStoreCheckAdapter;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.DatabaseHelper;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.model.MaterialResponse;
import id.co.qualitas.qubes.model.OutletResponse;
import id.co.qualitas.qubes.model.Uom;

public class StoreCheckFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    private ArrayList<MaterialResponse> materialResponseArrayList = new ArrayList<>();
    private ArrayList<MaterialResponse> listDelete = new ArrayList<>();
    private ArrayList<Uom> listUom = new ArrayList<>();
    private ArrayList<MaterialResponse> listMaterialStore = new ArrayList<>();

    private CreateStoreCheckAdapter mAdapter;

    private FloatingActionButton btnAddMaterial;
    private TextView customerName, layDate;
    private TextView relEmpty;
    private LinearLayout l1;

    private String idMaterial, idOutlet, outletName = Constants.EMPTY_STRING;
    private int flag = 0;

    private MaterialResponse material;
    private OutletResponse outletResponse;

    private ScrollView scrollView;
    private ImageView imgSearch, imgSave, imgBack;
    private EditText edtTxtSearch;
    boolean searchVisible = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_store_check, container, false);
        initFragment();
        initialize();
        initProgress();
        setTodayDate();

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        this.rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((MainActivity) getActivity()).changePage(8);
                        return true;
                    }
                }
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).changePage(8);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchVisible) {
                    searchVisible = false;
                    edtTxtSearch.setVisibility(View.GONE);
                    setMaterialData(materialResponseArrayList);
                } else {
                    searchVisible = true;
                    edtTxtSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listDelete != null && !listDelete.isEmpty()) {
                    for (MaterialResponse deleteMaterial : listDelete) {
                        if (deleteMaterial.getId_store_check() != null) {
                            if (deleteMaterial.getId_store_check().contains(Constants.ID_ORI_STORE_CHECK)) {
                                db.deleteStoreCheckByIdSoft(deleteMaterial.getId_store_check());
                            } else {
                                db.deleteStoreCheckByIdHard(deleteMaterial.getId_store_check());
                            }
                        }
                    }
                }

                if (materialResponseArrayList != null && !materialResponseArrayList.isEmpty()) {
                    for (MaterialResponse materialSc : materialResponseArrayList) {

                        if (materialSc.getQty1() != null || materialSc.getQty2() != null) {
                            if (!materialSc.getQty1().equals("0") || !materialSc.getQty2().equals("0")) {
                                if (materialSc.getUom2() != null) {
                                    if (materialSc.getUom2().equals(Constants.NULL)) {
                                        materialSc.setUom2(null);
                                    }
                                }

                                materialSc.setIdOutlet(idOutlet);
                                if (listMaterialStore != null && !listMaterialStore.isEmpty()) {
                                    for (int a = 0; a < listMaterialStore.size(); a++) {
                                        if (materialSc.getId_store_check() == null) {
                                            materialSc.setId_store_check(Constants.ID_SC_MOBILE.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                            materialSc.setDeleted(false);
                                            materialSc.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                            db.addStoreCheck(materialSc);
                                            break;
                                        } else {
                                            if (materialSc.getId_store_check().equals(listMaterialStore.get(a).getId_store_check())) {
                                                db.updateStoreCheck(materialSc);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    materialSc.setId_store_check(Constants.ID_SC_MOBILE.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                    materialSc.setDeleted(false);
                                    materialSc.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                    db.addStoreCheck(materialSc);
                                }

                            }
                        }
                    }
                }

                ((MainActivity) getActivity()).changePage(8);
//                Intent intent = new Intent(StoreCheckActivity.this, Timer2Fragment.class);
//                startActivity(intent);
//                fragment = new TimerFragment();
//                setContent(fragment);
//                getFragmentManager().popBackStack();
            }
        });

        edtTxtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence newText, int start, int before, int count) {
                if (searchVisible) {
                    if (newText == null || newText.toString().trim().isEmpty()) {
                        if (materialResponseArrayList.size() != 0) {
                            setMaterialData(materialResponseArrayList);
                        }
                    }

                    final ArrayList<MaterialResponse> filteredList = new ArrayList<>();

                    for (int i = 0; i < materialResponseArrayList.size(); i++) {
                        final String text = Helper.validateResponseEmpty(materialResponseArrayList.get(i).getIdMaterial()).toLowerCase() +
                                " " + Helper.validateResponseEmpty(materialResponseArrayList.get(i).getMaterialName()).toLowerCase() +
                                " " + Helper.validateResponseEmpty(materialResponseArrayList.get(i).getIdMaterialClass()).toLowerCase();
                        if (text.contains(newText)) {
                            filteredList.add(materialResponseArrayList.get(i));
                        }
                    }
                    setMaterialData(filteredList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Helper.setItemParam(Constants.VIEW_STORE_CHECK, Constants.DONE_VIEW_STORE_CHECK);

        customerName.setText(Helper.validateResponseEmpty(outletName));

        btnAddMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.removeItemParam(Constants.VISIT_ORDER_DETAIL);
                Helper.removeItemParam(Constants.RETURN_DETAIL);
                Helper.setItemParam(Constants.STORE_CHECK_DETAIL, materialResponseArrayList);
                if (alertDialog != null) {
                    if (alertDialog.isShowing()) return;
                }
                openDialog(DIALOG_ADD_MATERIAL);
            }
        });

        return rootView;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem save = menu.findItem(R.id.action_save);
        final MenuItem filter = menu.findItem(R.id.action_filter);
        final MenuItem search = menu.findItem(R.id.action_search);
        final MenuItem edit = menu.findItem(R.id.action_edit);
        final MenuItem settings = menu.findItem(R.id.action_settings);
        final MenuItem next = menu.findItem(R.id.action_next);

        filter.setVisible(false);
        edit.setVisible(false);
        settings.setVisible(false);
        save.setVisible(true);
        search.setVisible(true);
        next.setVisible(false);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search..");

        save.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (listDelete != null && !listDelete.isEmpty()) {
                    for (MaterialResponse deleteMaterial : listDelete) {
                        if (deleteMaterial.getId_store_check() != null) {
                            if (deleteMaterial.getId_store_check().contains(Constants.ID_ORI_STORE_CHECK)) {
                                db.deleteStoreCheckByIdSoft(deleteMaterial.getId_store_check());
                            } else {
                                db.deleteStoreCheckByIdHard(deleteMaterial.getId_store_check());
                            }
                        }
                    }
                }

                if (materialResponseArrayList != null && !materialResponseArrayList.isEmpty()) {
                    for (MaterialResponse materialSc : materialResponseArrayList) {

                        if (materialSc.getQty1() != null || materialSc.getQty2() != null) {
                            if (!materialSc.getQty1().equals("0") || !materialSc.getQty2().equals("0")) {
                                if (materialSc.getUom2() != null) {
                                    if (materialSc.getUom2().equals(Constants.NULL)) {
                                        materialSc.setUom2(null);
                                    }
                                }

                                materialSc.setIdOutlet(idOutlet);
                                if (listMaterialStore != null && !listMaterialStore.isEmpty()) {
                                    for (int a = 0; a < listMaterialStore.size(); a++) {
                                        if (materialSc.getId_store_check() == null) {
                                            materialSc.setId_store_check(Constants.ID_SC_MOBILE.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                            materialSc.setDeleted(false);
                                            materialSc.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                            db.addStoreCheck(materialSc);
                                            break;
                                        } else {
                                            if (materialSc.getId_store_check().equals(listMaterialStore.get(a).getId_store_check())) {
                                                db.updateStoreCheck(materialSc);
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    materialSc.setId_store_check(Constants.ID_SC_MOBILE.concat(String.valueOf(Helper.mixNumber(Calendar.getInstance(Locale.getDefault()).getTime()))));
                                    materialSc.setDeleted(false);
                                    materialSc.setDate_mobile(Helper.changeDateFormat(Constants.DATE_TYPE_1, Constants.DATE_TYPE_2, user.getCurrentDate()));
                                    db.addStoreCheck(materialSc);
                                }

                            }
                        }
                    }
                }

//                fragment = new TimerFragment();
//                setContent(fragment);
                getFragmentManager().popBackStack();
                return false;
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            if (materialResponseArrayList.size() != 0) {
                setMaterialData(materialResponseArrayList);
            }
            return false;
        }

        final ArrayList<MaterialResponse> filteredList = new ArrayList<>();

        for (int i = 0; i < materialResponseArrayList.size(); i++) {
            final String text = Helper.validateResponseEmpty(materialResponseArrayList.get(i).getIdMaterial()).toLowerCase() +
                    " " + Helper.validateResponseEmpty(materialResponseArrayList.get(i).getMaterialName()).toLowerCase() +
                    " " + Helper.validateResponseEmpty(materialResponseArrayList.get(i).getIdMaterialClass()).toLowerCase();
            if (text.contains(newText)) {
                filteredList.add(materialResponseArrayList.get(i));
            }
        }
        setMaterialData(filteredList);
        return false;
    }

    private void initialize() {
        outletResponse = (OutletResponse) Helper.getItemParam(Constants.OUTLET);

        if (outletResponse != null) {
            idOutlet = outletResponse.getIdOutlet();
            outletName = outletResponse.getOutletName();
        }

        imgBack = rootView.findViewById(R.id.imgBack);
        imgSearch = rootView.findViewById(R.id.imgSearch);
        imgSave = rootView.findViewById(R.id.imgSave);
        edtTxtSearch = rootView.findViewById(R.id.edtTxtSearch);

        relEmpty = rootView.findViewById(R.id.relEmpty);

        btnAddMaterial = rootView.findViewById(R.id.btnAdd);
        customerName = rootView.findViewById(R.id.customerName);
        layDate = rootView.findViewById(R.id.layDate);
        l1 = rootView.findViewById(R.id.list_view_stock_opname_before);
        l1.removeAllViews();

        if (idOutlet != null) {
            listMaterialStore = db.getListMaterialStoreCheck(idOutlet);
        }

        scrollView = rootView.findViewById(R.id.l1);
    }

    private void setTodayDate() {
        if (outletResponse.getVisitDate() != null) {
            layDate.setText(Helper.changeDateFormat(Constants.DATE_TYPE_2, Constants.DATE_TYPE_12, outletResponse.getVisitDate()));
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        super.onResume();
        Helper.setItemParam(Constants.CURRENTPAGE, "10");
        if (outletResponse.getStatusCheckIn().equals(Constants.FINISHED)) {
            btnAddMaterial.setVisibility(View.INVISIBLE);
        } else {
            btnAddMaterial.setVisibility(View.VISIBLE);
        }

        getData();
    }


    private void getData() {
        db = new DatabaseHelper(getActivity());
        if (Helper.getItemParam(Constants.GET_DETAIL_VISIT) != null) {
            if (Helper.getItemParam(Constants.ADD_MATERIAL) != null) {//add material
                material = (MaterialResponse) Helper.getItemParam(Constants.ADD_MATERIAL_STORE);
                idMaterial = material.getIdMaterial();
                Helper.removeItemParam(Constants.GET_DETAIL_VISIT);
                materialResponseArrayList = new ArrayList<>();
                materialResponseArrayList = (ArrayList<MaterialResponse>) Helper.getItemParam(Constants.STORE_CHECK_DETAIL);

                if (!materialResponseArrayList.isEmpty()) {
                    Helper.removeItemParam(Constants.STORE_CHECK_DETAIL);
                    for (int i = 0; i < materialResponseArrayList.size(); i++) {
                        if (materialResponseArrayList.get(i).getIdMaterial() != null) {
                            if (materialResponseArrayList.get(i).getIdMaterial().equals(idMaterial)) {
                                flag++;
                            }
                        }
                    }

                    if (flag == 0) {
                        if (material != null) {
                            setDataRet(material);
                            Helper.removeItemParam(Constants.ADD_MATERIAL);
                        }
                    } else {
                        flag = 0;
                        Toast.makeText(getActivity(), getString(R.string.barangSudahAda), Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (Helper.getItemParam(Constants.ADD_MATERIAL_STORE) != null) {
                        setDataRet(material);

                        Helper.removeItemParam(Constants.ADD_MATERIAL);
                        Helper.removeItemParam(Constants.STORE_CHECK_DETAIL);
                    }
                }
            }
        } else {
            if (Helper.getItemParam(Constants.STORE_CHECK_DETAIL) != null) {
                setMaterialData((ArrayList<MaterialResponse>) Helper.getItemParam(Constants.STORE_CHECK_DETAIL));
            } else {
                /*kalau udah ada di db*/
                if (idOutlet != null) {
                    materialResponseArrayList = db.getListMaterialStoreCheck(idOutlet);
                    if (materialResponseArrayList != null && !materialResponseArrayList.isEmpty()) {
                        for (int i = 0; i < materialResponseArrayList.size(); i++) {
                            if (materialResponseArrayList.get(i).getIdMaterial() != null) {
                                if (db.getMaterialName(materialResponseArrayList.get(i).getIdMaterial()) != null) {
                                    materialResponseArrayList.get(i).setMaterialName(db.getMaterialName(materialResponseArrayList.get(i).getIdMaterial()));
                                }
                            }
                        }
                        setMaterialData(materialResponseArrayList);
                        relEmpty.setVisibility(View.GONE);
                    } else {
                        relEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public void setDataRet(MaterialResponse material) {
        l1.removeAllViews();
        relEmpty.setVisibility(View.GONE);

        material.setMaterialName(Helper.validateResponseEmpty(db.getMaterialName(material.getIdMaterial())));
        if (material.getIdMaterial() != null) {
            listUom = db.getListUomByIdMat(material.getIdMaterial(), Constants.IS_ORDER);
            if (listUom != null && !listUom.isEmpty()) {
                material.setListUomName(listUom);
            }
        }

        materialResponseArrayList.add(material);
        mAdapter = new CreateStoreCheckAdapter(this, materialResponseArrayList);

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i, null, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 15);
            l1.addView(item, layoutParams);
        }
        progress.dismiss();
    }

    private void setMaterialData(ArrayList<MaterialResponse> mData) {
        l1.removeAllViews();

        listUom = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            listUom = db.getListUomByIdMat(mData.get(i).getIdMaterial(), Constants.IS_ORDER);
            mData.get(i).setListUomName(listUom);
        }

        mAdapter = new CreateStoreCheckAdapter(this, mData);

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View item = mAdapter.getView(i, null, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 15);
            l1.addView(item, layoutParams);
        }
    }

    @Override
    public void onPause() {
        Helper.setItemParam(Constants.STORE_CHECK_DETAIL, materialResponseArrayList);
        super.onPause();
    }

    public void deleteStoreCheck(final String id, final int position) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView;
//        android.view.WindowManager$BadTokenException: Unable to add window -- token null is not for an application
        final Dialog alertDialog = new Dialog(getActivity());
        dialogView = inflater.inflate(R.layout.custom_dialog_alert_delete, null);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(dialogView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnCancel = alertDialog.findViewById(R.id.btnCancel);
        Button btnSave = alertDialog.findViewById(R.id.btnSave);
        TextView txtDialog = alertDialog.findViewById(R.id.txtDialog);

        txtDialog.setText(getResources().getString(R.string.noticeDelete));
        btnSave.setText(getResources().getString(R.string.yes));
        btnCancel.setText(getResources().getString(R.string.no));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (materialResponseArrayList != null && !materialResponseArrayList.isEmpty()) {
                    listDelete.add(materialResponseArrayList.get(position));
                    materialResponseArrayList.remove(position);
                    setMaterialData(materialResponseArrayList);
                }
                mAdapter.notifyDataSetChanged();

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}