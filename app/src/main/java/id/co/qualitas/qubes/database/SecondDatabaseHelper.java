package id.co.qualitas.qubes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import id.co.qualitas.qubes.model.Reason;

public class SecondDatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "qubescoba.sqlite";

    // Database Path
    private static final String DATABASE_PATH = "";
    private static final String KEY_TAX = "tax";

    private Boolean flagCreate = false;


    private static final String TABLE_REASON = "Reason";
    private static final String KEY_ID_REASON = "idReason";
    private static final String KEY_REASON_DESC = "reasonDesc";
    private static final String KEY_TYPE = "type";

    public SecondDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        flagCreate = true;
//        init(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON);
    }

    public ArrayList<Reason> getAllReason() {
        ArrayList<Reason> listReason = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REASON;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Reason reason = new Reason();
                reason.setId(cursor.getInt(0));
                reason.setDescription(cursor.getString(1));
                reason.setType(cursor.getString(2));

                listReason.add(reason);
            } while (cursor.moveToNext());
        }

        return listReason;
    }

    public void addReason(Reason reason) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_REASON, reason.getId());
        values.put(KEY_REASON_DESC, reason.getDescription());
        values.put(KEY_TYPE, reason.getType());

        db.insert(TABLE_REASON, null, values);
        db.close();
    }
}
