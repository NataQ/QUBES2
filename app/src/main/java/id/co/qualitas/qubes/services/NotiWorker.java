package id.co.qualitas.qubes.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import id.co.qualitas.qubes.R;
import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.fragment.aspp.AccountFragment;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.Bank;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.CustomerType;
import id.co.qualitas.qubes.model.DaerahTingkat;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.Parameter;
import id.co.qualitas.qubes.model.PriceCode;
import id.co.qualitas.qubes.model.Promotion;
import id.co.qualitas.qubes.model.Reason;
import id.co.qualitas.qubes.model.SalesPriceDetail;
import id.co.qualitas.qubes.model.SalesPriceHeader;
import id.co.qualitas.qubes.model.Uom;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManager;
import id.co.qualitas.qubes.session.SessionManagerQubes;
import id.co.qualitas.qubes.utils.Utils;

public class NotiWorker extends Worker {
    private static final int PARAM = 0;
    Context context;
    WorkerParameters workerParams;
    List<WSMessage> listWSMsg = new ArrayList<>();
    String url = null;
    Map req = new HashMap();
    private boolean mIsRandomGeneratorOn;
    private int mRandomNumber;
    private List<Customer> nooList = new ArrayList<>();
    private List<VisitSalesman> visitSalesmanList = new ArrayList<>();
    private List<Map> storeCheckList = new ArrayList<>(), returnList = new ArrayList<>();
    private List<CollectionHeader> collectionList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private List<Map> photoList = new ArrayList<>();
    private int sizeData = 0;
    private Database database;
    private User user;
    private WSMessage logResult;
    private String exMess;
    private List<Customer> customerList;
    boolean saveSuccess = false, setDataSyncSuccess = false;

    public NotiWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParams = workerParams;
        mIsRandomGeneratorOn = true;
    }

    @NonNull
    @Override
    public Result doWork() {
        WSMessage result = new WSMessage();
        String url = null;
        if (!isStopped()) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }).start();
            setSession();
            syncDataNOO();
            syncDataVisit();
            syncDataStoreCheck();
            syncDataCollection();
            syncDataOrder();
            syncDataReturn();
        }
        if (saveSuccess) {
            Log.e("worker", "success");
            return Result.success();
        } else {
            Log.e("worker", "failed");
            return Result.failure();
        }
    }

    private void syncDataNOO() {
        saveSuccess = false;
        nooList = new ArrayList<>();
        nooList = database.getAllNoo();
        if (nooList == null) {
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline noo failed: " + exMess);
            database.addLog(logResult);
        } else {
            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_CUSTOMER_NOO);
            req = new HashMap();
            List<Customer> mList = new ArrayList<>();
            for (Customer data : nooList) {
                req = new HashMap();
                mList = new ArrayList<>();
                mList.add(data);
                req.put("listData", mList);
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                if (logResult != null) {
                    saveSuccess = true;
                    if (logResult.getIdMessage() == 1) {
                        logResult.setMessage("Sync noo " + data.getId() + " success");
                        database.updateSyncNoo(data);
                    } else {
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Sync noo failed : " + exMess);
                    }
                    database.addLog(logResult);
                } else {
                    saveSuccess = false;
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set offline noo failed: " + exMess);
                    database.addLog(logResult);
                }
            }
        }
    }

    private void syncDataVisit() {
        saveSuccess = false;
        visitSalesmanList = new ArrayList<>();
        visitSalesmanList = database.getAllVisitSalesman();
        if (visitSalesmanList == null) {
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline visit failed: " + exMess);
            database.addLog(logResult);
        } else {
            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_VISIT);
            req = new HashMap();
            List<VisitSalesman> mList = new ArrayList<>();
            for (VisitSalesman data : visitSalesmanList) {
                req = new HashMap();
                mList = new ArrayList<>();
                mList.add(data);
                req.put("listData", mList);
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                if (logResult != null) {
                    saveSuccess = true;
                    if (logResult.getIdMessage() == 1) {
                        logResult.setMessage("Sync offline visit " + data.getIdHeader() + " success");
                        database.updateSyncVisitSalesman(data);
                    } else {
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline visit failed: " + exMess);
                    }
                    database.addLog(logResult);
                } else {
                    saveSuccess = false;
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set offline visit failed: " + exMess);
                    database.addLog(logResult);
                }
            }
        }
    }

    private void syncDataStoreCheck() {
        user = SessionManagerQubes.getUserProfile();
        customerList = database.getAllCustomerCheckOut();
        if (customerList != null) {
            if (!customerList.isEmpty()) {
                List<Material> mList = new ArrayList<>();
                Map header = new HashMap();
                for (Customer customer : customerList) {
                    mList = new ArrayList<>();
                    mList = database.getAllStoreCheckCheckOut(customer.getId());
                    if (mList != null) {
                        if (!mList.isEmpty()) {
                            header = new HashMap();
                            header.put("id_mobile", mList.get(0).getIdheader());
                            header.put("date", mList.get(0).getDate());
                            header.put("id_salesman", user.getUsername());
                            header.put("id_customer", mList.get(0).getId_customer());
                            header.put("listData", mList);
                            storeCheckList.add(header);
                            setDataSyncSuccess = true;
                        } else {
                            setDataSyncSuccess = true;
                        }
                    } else {
                        setDataSyncSuccess = false;
                    }
                }
            } else {
                setDataSyncSuccess = true;
                storeCheckList = new ArrayList<>();
            }
        } else {
            setDataSyncSuccess = false;
        }

        if (setDataSyncSuccess) {
            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_STORE_CHECK);
            for (Map data : storeCheckList) {
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                if (logResult != null) {
                    saveSuccess = true;
                    if (logResult.getIdMessage() == 1) {
                        logResult.setMessage("Sync offline store check " + data.get("id_mobile").toString() + " success");
                        database.updateSyncStoreCheck(data);
                    } else {
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline store check failed: " + exMess);
                    }
                    database.addLog(logResult);
                } else {
                    saveSuccess = false;
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set offline store check failed: " + exMess);
                    database.addLog(logResult);
                }
            }
        } else {
            saveSuccess = false;
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline store check failed: " + exMess);
            database.addLog(logResult);
        }
    }

    private void syncDataCollection() {
        user = SessionManagerQubes.getUserProfile();
        customerList = database.getAllCustomerCheckOut();
        if (customerList != null) {
            if (!customerList.isEmpty()) {
                collectionList = new ArrayList<>();
                List<CollectionHeader> mList = new ArrayList<>();
                for (Customer customer : customerList) {
                    mList = new ArrayList<>();
                    mList = database.getAllCollectionHeader(customer.getId());
                    if (mList != null) {
                        if (!mList.isEmpty()) {
                            collectionList.addAll(mList);
                            setDataSyncSuccess = true;
                        } else {
                            setDataSyncSuccess = true;
                        }
                    } else {
                        setDataSyncSuccess = false;
                    }
                }
            } else {
                setDataSyncSuccess = true;
                storeCheckList = new ArrayList<>();
            }
        } else {
            setDataSyncSuccess = false;
        }

        if (setDataSyncSuccess) {
            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_COLLECTION);
            for (CollectionHeader data : collectionList) {
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                if (logResult != null) {
                    saveSuccess = true;
                    if (logResult.getIdMessage() == 1) {
                        logResult.setMessage("Sync offline collection " + data.getIdHeader() + " success");
                        database.updateSyncCollectionHeader(data);
                    } else {
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline collection failed: " + exMess);
                    }
                    database.addLog(logResult);
                } else {
                    saveSuccess = false;
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set offline collection failed: " + exMess);
                    database.addLog(logResult);
                }
            }
        } else {
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline collection failed: " + exMess);
            database.addLog(logResult);
        }
    }

    private void syncDataOrder() {
        user = SessionManagerQubes.getUserProfile();
        customerList = database.getAllCustomerCheckOut();
        if (customerList != null) {
            if (!customerList.isEmpty()) {
                orderList = new ArrayList<>();
                List<Order> mList = new ArrayList<>();
                for (Customer customer : customerList) {
                    mList = new ArrayList<>();
                    mList = database.getAllOrderHeader(customer.getId(), user.getUsername());
                    if (mList != null) {
                        if (!mList.isEmpty()) {
                            orderList.addAll(mList);
                            setDataSyncSuccess = true;
                        } else {
                            setDataSyncSuccess = true;
                        }
                    } else {
                        setDataSyncSuccess = false;
                    }
                }
            } else {
                setDataSyncSuccess = true;
                storeCheckList = new ArrayList<>();
            }
        } else {
            setDataSyncSuccess = false;
        }

        if (setDataSyncSuccess) {
            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_ORDER);
            for (Order data : orderList) {
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);
                if (logResult != null) {
                    saveSuccess = true;
                    if (logResult.getIdMessage() == 1) {
                        logResult.setMessage("Sync offline order " + data.getIdHeader() + " success");
                        database.updateSyncOrderHeader(data);
                    } else {
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline order failed: " + exMess);
                    }
                    database.addLog(logResult);
                } else {
                    saveSuccess = false;
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set offline order failed: " + exMess);
                    database.addLog(logResult);
                }
            }
        } else {
            saveSuccess = false;
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline order failed: " + exMess);
            database.addLog(logResult);
        }
    }

    private void syncDataReturn() {
        user = SessionManagerQubes.getUserProfile();
        customerList = database.getAllCustomerCheckOut();
        if (customerList != null) {
            if (!customerList.isEmpty()) {
                returnList = new ArrayList<>();
                List<Material> mList = new ArrayList<>();
                Map header = new HashMap();
                for (Customer customer : customerList) {
                    mList = new ArrayList<>();
                    mList = database.getAllReturnCheckOut(customer.getId());
                    if (mList != null) {
                        if (!mList.isEmpty()) {
                            header = new HashMap();
                            header.put("id_mobile", mList.get(0).getIdheader());
                            header.put("date", mList.get(0).getDate());
                            header.put("id_salesman", user.getUsername());
                            header.put("id_customer", mList.get(0).getId_customer());
                            header.put("listData", mList);
                            returnList.add(header);
                            setDataSyncSuccess = true;
                        } else {
                            setDataSyncSuccess = true;
                        }
                    } else {
                        setDataSyncSuccess = false;
                    }
                }
                if (!setDataSyncSuccess) {
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set offline return failed: " + exMess);
                }
            } else {
                setDataSyncSuccess = true;
                storeCheckList = new ArrayList<>();
            }
        } else {
            setDataSyncSuccess = false;
        }

        if (setDataSyncSuccess) {
            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_RETURN);
            for (Map data : returnList) {
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, data);

                if (logResult != null) {
                    saveSuccess = true;
                    if (logResult.getIdMessage() == 1) {
                        logResult.setMessage("Sync offline return " + data.get("id_customer").toString() + " success");
                        database.updateSyncReturn(data);
                    } else {
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Set offline return failed: " + exMess);
                    }
                    database.addLog(logResult);
                } else {
                    saveSuccess = false;
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set offline return failed: " + exMess);
                    database.addLog(logResult);
                }
            }
        } else {
            saveSuccess = false;
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            String exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set offline return error: " + exMess);
        }
    }

    public void setSession() {
        if (SessionManagerQubes.getUrl() != null) {
            String ipAddress = SessionManagerQubes.getUrl();
            Constants.URL = ipAddress;
            Helper.setItemParam(Constants.URL, Constants.URL);
        }
        database = new Database(context);
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.e("worker", "Worker has been canceled");
    }
}