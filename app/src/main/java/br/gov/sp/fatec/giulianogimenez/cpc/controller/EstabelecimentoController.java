package br.gov.sp.fatec.giulianogimenez.cpc.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.gov.sp.fatec.giulianogimenez.cpc.model.Estabelecimento;

/**
 * Created by giuliano.gimenez on 31/10/2016.
 */

public class EstabelecimentoController {
    private SQLiteDatabase db;
    Estabelecimento estabelecimento;

    public EstabelecimentoController(Context context) {
        estabelecimento = new Estabelecimento(context);
    }

    public String inserir(String nome, String endereco, String latitude, String longitude, boolean conveniencia,
                          boolean alimentacao, boolean trocaOleo, boolean lavaRapido, boolean mecanico,
                          boolean borracheiro, boolean caixaEletronico, boolean semParar, boolean viaFacil, String bandeira) {
        ContentValues valores;
        long resultado;

        db = estabelecimento.getWritableDatabase();
        valores = new ContentValues();
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_ALIMENCACAO, alimentacao ? 1 : 0);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_BANDEIRA, bandeira);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_BORRACHEIRO, borracheiro ? 1 : 0);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_CAIXAELETRONICO, caixaEletronico ? 1 : 0);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_CONVENIENCIA, conveniencia ? 1 : 0);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_ENDERECO, endereco);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_LAT, latitude);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_LONG, longitude);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_LAVARAPIDO, lavaRapido ? 1 : 0);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_MECANICO, mecanico ? 1 : 0);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_NOME, nome);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_SEMPARAR, semParar ? 1 : 0);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_TROCAOLEO, trocaOleo ? 1 : 0);
        valores.put(Estabelecimento.EstabelecimentoInfo.EST_VIAFACIL, viaFacil ? 1 : 0);
        resultado = db.insert(Estabelecimento.EstabelecimentoInfo.TABLE_NAME, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";
    }
}
