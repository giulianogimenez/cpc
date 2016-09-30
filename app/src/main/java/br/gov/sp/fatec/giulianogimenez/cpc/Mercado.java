package br.gov.sp.fatec.giulianogimenez.cpc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Giuliano on 30/09/2016.
 */
public class Mercado extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + MercadoInfo.TABLE_NAME + " (" +
            MercadoInfo.MER_ID + " INTEGER PRIMARY KEY," +
            MercadoInfo.PRC_ID + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            MercadoInfo.ENC_ID + ");";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MercadoInfo.TABLE_NAME;

    public Mercado(Context context) {
        super(context, DatabaseInfo.DATABASE_NAME, null, DatabaseInfo.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public static abstract class MercadoInfo {
        public final static String TABLE_NAME = "mer_mercado";
        public final static String MER_ID = "mer_id";
        public final static String ENC_ID = "enc_id";
        public final static String PRC_ID = "prc_id";
    }
}
