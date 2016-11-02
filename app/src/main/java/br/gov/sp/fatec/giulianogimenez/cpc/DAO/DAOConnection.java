package br.gov.sp.fatec.giulianogimenez.cpc.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.gov.sp.fatec.giulianogimenez.cpc.model.Estabelecimento;
import br.gov.sp.fatec.giulianogimenez.cpc.model.Preco;

/**
 * Created by giuliano on 02/11/16.
 */

public class DAOConnection extends SQLiteOpenHelper {

    public DAOConnection(Context context) {
        super(context, DatabaseInfo.DATABASE_NAME, null, DatabaseInfo.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Estabelecimento.SQL_CREATE_ENTRIES);
        db.execSQL(Preco.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(Estabelecimento.SQL_DELETE_ENTRIES);
        db.execSQL(Preco.SQL_DELETE_ENTRIES);
    }
}
