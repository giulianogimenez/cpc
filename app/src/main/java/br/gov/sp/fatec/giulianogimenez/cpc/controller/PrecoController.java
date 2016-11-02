package br.gov.sp.fatec.giulianogimenez.cpc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.gov.sp.fatec.giulianogimenez.cpc.DAO.DAOConnection;
import br.gov.sp.fatec.giulianogimenez.cpc.model.Preco;

/**
 * Created by giuliano.gimenez on 01/11/2016.
 */

public class PrecoController {
    private SQLiteDatabase db;
    private DAOConnection dao;

    public PrecoController(Context context) {
        dao = new DAOConnection(context);
    }

    public String inserir(float valor, String tipoCombustivel, String estNome) {
        ContentValues valores;
        long resultado;

        db = dao.getWritableDatabase();
        valores = new ContentValues();
        valores.put(Preco.PrecoInfo.EST_NOME, estNome);
        valores.put(Preco.PrecoInfo.PRC_TIPOCOMBUSTIVEL, tipoCombustivel);
        valores.put(Preco.PrecoInfo.PRC_VALOR, valor);
        resultado = db.insert(Preco.PrecoInfo.TABLE_NAME, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";
    }

    public Cursor carregaPrecoEstabelecimento(String estabelecimento) {
        Cursor cursor;

        db = dao.getReadableDatabase();
        String where = Preco.PrecoInfo.EST_NOME + " = '" + estabelecimento + "'";
        String[] campos = {Preco.PrecoInfo.PRC_TIPOCOMBUSTIVEL, Preco.PrecoInfo.PRC_VALOR,
                           Preco.PrecoInfo.EST_NOME};
        cursor = db.query(Preco.PrecoInfo.TABLE_NAME, campos, where, null, null, null, null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
}
