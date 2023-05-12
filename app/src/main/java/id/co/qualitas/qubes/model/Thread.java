package id.co.qualitas.qubes.model;

import java.util.ArrayList;
import java.util.List;

import id.co.qualitas.qubes.database.DatabaseHelper;

/**
 * Created by Wiliam on 11/3/2017.
 */

public class Thread extends java.lang.Thread{
    private List<UnitOfMeasure> uomList;
    private DatabaseHelper db;

    public Thread(List<UnitOfMeasure> uomList, DatabaseHelper db) {
        this.uomList = uomList;
        this.db = db;
    }

    public void run(){
        if(uomList != null && !uomList.isEmpty()){
            List<UnitOfMeasure> tempUom = new ArrayList<>();
            for(int i = 0; i < uomList.size(); i++){
//                tempUom = new ArrayList<>(uomList);

                UnitOfMeasure uom = uomList.get(i);
                UnitOfMeasure temp = new UnitOfMeasure();

                if(uom.getId() != null){
                    temp.setId(uom.getId());
                }

                if(uom.getUomName() != null){
                    temp.setUomName(uom.getUomName());
                }

                tempUom.add(temp);

                if(tempUom.size() == 500 || i == uomList.size() - 1){
                    try {
                        db.addMasterUomN(tempUom);
                    }catch (Exception ignored){
                    }
                    tempUom = new ArrayList<>();
                }
            }
        }
    }
}
