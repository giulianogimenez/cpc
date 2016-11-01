package br.gov.sp.fatec.giulianogimenez.cpc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

import br.gov.sp.fatec.giulianogimenez.cpc.model.Preco;

/**
 * Created by giuliano.gimenez on 01/11/2016.
 */

public class PrecoController {
    private SQLiteDatabase db;
    private Preco preco;

    public PrecoController(Context context) {
        preco = new Preco(context);
    }

    public String inserir(float valor, String tipoCombustivel, Long estId) {
        ContentValues valores;
        long resultado;

        db = preco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(Preco.PrecoInfo.EST_ID, estId);
        valores.put(Preco.PrecoInfo.PRC_TIPOCOMBUSTIVEL, tipoCombustivel);
        valores.put(Preco.PrecoInfo.PRC_VALOR, valor);
        resultado = db.insert(Preco.PrecoInfo.TABLE_NAME, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";
    }
}
