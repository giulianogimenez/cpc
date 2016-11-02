package br.gov.sp.fatec.giulianogimenez.cpc.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.gov.sp.fatec.giulianogimenez.cpc.DAO.DatabaseInfo;

/**
 * Created by giuliano.gimenez on 30/09/2016.
 */
public class Preco {

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Preco.PrecoInfo.TABLE_NAME + " (" +
            Preco.PrecoInfo.PRC_ID + DatabaseInfo.INT_TYPE + " PRIMARYKEY " + DatabaseInfo.COMMA_SEP +
            Preco.PrecoInfo.PRC_VALOR + DatabaseInfo.DECIMAL_TYPE + DatabaseInfo.COMMA_SEP +
            Preco.PrecoInfo.PRC_TIPOCOMBUSTIVEL + DatabaseInfo.TEXT_TYPE + DatabaseInfo.COMMA_SEP +
            Preco.PrecoInfo.EST_ID + DatabaseInfo.INT_TYPE + DatabaseInfo.COMMA_SEP +
            " FOREIGN KEY (" + Preco.PrecoInfo.EST_ID + ") REFERENCES " + Estabelecimento.EstabelecimentoInfo.TABLE_NAME + "(" + Estabelecimento.EstabelecimentoInfo.EST_ID + "));";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PrecoInfo.TABLE_NAME;

    public static abstract class PrecoInfo {
        public final static String TABLE_NAME = "prc_preco";
        public final static String PRC_ID = "prc_id";
        public final static String PRC_VALOR = "prc_valor";
        public final static String PRC_DATACADASTRO = "prc_datacadastro";
        public final static String PRC_TIPOCOMBUSTIVEL = "prc_tipocombustivel";
        public static final String EST_ID = "est_id";
    }
}
