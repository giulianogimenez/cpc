package br.gov.sp.fatec.giulianogimenez.cpc.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import br.gov.sp.fatec.giulianogimenez.cpc.DAO.DatabaseInfo;

/**
 * Created by giuliano.gimenez on 30/09/2016.
 */
public final class Estabelecimento extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + EstabelecimentoInfo.TABLE_NAME + " (" +
            EstabelecimentoInfo.EST_ID + DatabaseInfo.INT_TYPE + " PRIMARYKEY AUTOINCREMENT " + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_NOME + DatabaseInfo.TEXT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_ENDERECO + DatabaseInfo.TEXT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_LAT + DatabaseInfo.TEXT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_LONG + DatabaseInfo.TEXT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_ALIMENCACAO + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_BORRACHEIRO + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_CAIXAELETRONICO + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_CONVENIENCIA + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_LAVARAPIDO + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_MECANICO + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_TROCAOLEO + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_SEMPARAR + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_VIAFACIL + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            EstabelecimentoInfo.EST_BANDEIRA + DatabaseInfo.TEXT_TYPE + ");";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EstabelecimentoInfo.TABLE_NAME;


    public Estabelecimento(Context context) {
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

    public  static abstract class EstabelecimentoInfo implements BaseColumns {
        public static final String TABLE_NAME = "est_estabelecimento";
        public static final String EST_ID = "est_id";
        public static final String EST_NOME = "est_nome";
        public static final String EST_ENDERECO = "est_endereco";
        public static final String EST_LAT = "est_lat";
        public static final String EST_LONG = "est_long";
        public static final String EST_CONVENIENCIA = "est_conveniencia";
        public static final String EST_ALIMENCACAO = "est_alimentacao";
        public static final String EST_TROCAOLEO = "est_trocaoleo";
        public static final String EST_LAVARAPIDO = "est_lavarapido";
        public static final String EST_MECANICO = "est_mecanico";
        public static final String EST_BORRACHEIRO = "est_borracheiro";
        public static final String EST_CAIXAELETRONICO = "est_caixaeletronico";
        public static final String EST_SEMPARAR = "est_semparar";
        public static final String EST_VIAFACIL = "est_viafacil";
        public static final String EST_BANDEIRA = "est_bandeira";
    }
}
