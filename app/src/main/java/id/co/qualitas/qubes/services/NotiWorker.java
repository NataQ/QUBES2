package id.co.qualitas.qubes.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.qualitas.qubes.constants.Constants;
import id.co.qualitas.qubes.database.Database;
import id.co.qualitas.qubes.helper.Helper;
import id.co.qualitas.qubes.helper.NetworkHelper;
import id.co.qualitas.qubes.model.CollectionHeader;
import id.co.qualitas.qubes.model.Customer;
import id.co.qualitas.qubes.model.Discount;
import id.co.qualitas.qubes.model.Material;
import id.co.qualitas.qubes.model.Order;
import id.co.qualitas.qubes.model.User;
import id.co.qualitas.qubes.model.VisitSalesman;
import id.co.qualitas.qubes.model.WSMessage;
import id.co.qualitas.qubes.session.SessionManagerQubes;

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
    private List<Order> orderList = new ArrayList<>(), orderEmptyDiscount = new ArrayList<>();
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
            getOrderDiscount();
            syncDataNOO();
            syncDataVisit();
            syncDataStoreCheck();
            syncDataCollection();
            syncDataOrder();
//            syncDataReturn();
        }
        if (saveSuccess) {
            Log.e("worker", "success");
            return Result.success();
        } else {
            Log.e("worker", "failed");
            return Result.failure();
        }
    }

    private void getOrderDiscount() {
        saveSuccess = false;
        orderEmptyDiscount = new ArrayList<>();
        orderEmptyDiscount = database.getAllOrderHeaderEmptyDiscount(user.getIdSalesman());
        if (orderEmptyDiscount == null) {
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set order discount failed: " + exMess);
            database.addLog(logResult);
        } else {
            req = new HashMap();
            List<Order> mList = new ArrayList<>();
            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_GET_DISCOUNT_ORDER);
            Map request = new HashMap();
            for (Order data : orderEmptyDiscount) {
                request = new HashMap();
                request.put("id_customer", data.getId_customer());
                request.put("tipe_outlet", data.getOrder_type());
                request.put("id", data.getIdHeader());
                request.put("order_date", data.getOrder_date());

                List<Map> listBarang = new ArrayList<>();
                double omzet = 0;
                Map tempBarang = new HashMap<>();
                for (Material material : data.getMaterialList()) {
                    if (material.getQty() != 0) {
                        tempBarang = new HashMap<>();
                        tempBarang.put("id", material.getId());
                        tempBarang.put("harga", material.getPrice());
                        tempBarang.put("qty", material.getQty());
                        tempBarang.put("satuan", material.getUom());
                        listBarang.add(tempBarang);
                    }
                }
                request.put("listDetail", listBarang);
                logResult = (WSMessage) NetworkHelper.postWebserviceWithBody(url, WSMessage.class, req);
                if (logResult != null) {
                    saveSuccess = true;
                    if (logResult.getIdMessage() == 1) {
                        logResult.setMessage("Sync order discount " + data.getId() + " success");
                        Map resultMap = (Map) logResult.getResult();
                        List<Map> barangList = new ArrayList<>();
                        barangList = (List<Map>) resultMap.get("barang");
                        for (Map barangMap : barangList) {
                            String kodeBarang = barangMap.get("kodeBarang").toString();

                            //diskon
                            Map<String, String> map = (Map<String, String>) barangMap.get("diskon");
                            double totalDisc = 0;
                            List<Discount> discList = new ArrayList<>();
                            for (Map.Entry<String, String> pair : map.entrySet()) {
                                Discount disc = new Discount();
                                disc.setKeydiskon(pair.getKey());
                                disc.setValuediskon(pair.getValue());
                                totalDisc = totalDisc + Double.parseDouble(pair.getValue());
                                discList.add(disc);
                            }
                            //diskon
                            Discount extra = Helper.ObjectToGSON(barangMap.get("extra"), Discount.class);
                            for (Material material : data.getMaterialList()) {
                                if (material.getId().equals(kodeBarang)) {
//                                material.setDiscount(discount);
                                    material.setExtraDiscount(extra);
                                    material.setTotalDiscount(totalDisc);
                                    material.setDiskonList(discList);
                                }
                                omzet = 0;
                                omzet = omzet + (material.getPrice() - material.getTotalDiscount());
                            }
                            data.setOmzet(omzet);
                            database.updateOrderDiscount(data, user.getUsername());
                        }
                    } else {
                        exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                        logResult.setMessage("Sync order discount failed : " + exMess);
                    }
                    database.addLog(logResult);
                } else {
                    saveSuccess = false;
                    logResult = new WSMessage();
                    logResult.setIdMessage(0);
                    logResult.setResult(null);
                    exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
                    logResult.setMessage("Set order discount failed: " + exMess);
                    database.addLog(logResult);
                }
            }
        }
    }

    private void syncDataNOO() {
        saveSuccess = false;
        nooList = new ArrayList<>();
        nooList = database.getAllNoo(user);
        if (nooList == null) {
            logResult = new WSMessage();
            logResult.setIdMessage(0);
            logResult.setResult(null);
            exMess = Helper.getItemParam(Constants.LOG_EXCEPTION) != null ? Helper.getItemParam(Constants.LOG_EXCEPTION).toString() : "";
            logResult.setMessage("Set noo failed: " + exMess);
            database.addLog(logResult);
        } else {
            url = Constants.URL.concat(Constants.API_PREFIX).concat(Constants.API_SYNC_CUSTOMER_NOO);
            req = new HashMap();
            List<Customer> mList = new ArrayList<>();
            for (Customer data : nooList) {
                req = new HashMap();
                mList = new ArrayList<>();
                data.setSales_group(user.getId_sales_group());
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
                    logResult.setMessage("Set noo failed: " + exMess);
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
//                List<Material> mList = new ArrayList<>();
//                Map header = new HashMap();
                for (Customer customer : customerList) {
                    storeCheckList = database.getAllStoreCheckDate(customer.getId(), user.getUsername());
                    if (storeCheckList != null) {
                        setDataSyncSuccess = true;
                    } else {
                        setDataSyncSuccess = false;
                    }
//                    mList = new ArrayList<>();
//                    mList = database.getAllStoreCheckCheckOut(customer.getId());
//                    if (mList != null) {
//                        if (!mList.isEmpty()) {
//                            header = new HashMap();
//                            header.put("id_mobile", mList.get(0).getIdheader());
//                            header.put("date", mList.get(0).getDate());
//                            header.put("id_salesman", user.getUsername());
//                            header.put("id_customer", mList.get(0).getId_customer());
//                            header.put("listData", mList);
//                            storeCheckList.add(header);
//                            setDataSyncSuccess = true;
//                        } else {
//                            setDataSyncSuccess = true;
//                        }
//                    } else {
//                        setDataSyncSuccess = false;
//                    }
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
                for (Customer customer : customerList) {
                    returnList.addAll(database.getAllReturnDate(customer.getId(), user.getUsername()));
                    if (returnList != null) {
                        setDataSyncSuccess = true;
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
        user = SessionManagerQubes.getUserProfile();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.e("worker", "Worker has been canceled");
    }
}