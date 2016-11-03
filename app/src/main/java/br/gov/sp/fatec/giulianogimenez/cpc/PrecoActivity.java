package br.gov.sp.fatec.giulianogimenez.cpc;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.gov.sp.fatec.giulianogimenez.cpc.controller.EstabelecimentoController;
import br.gov.sp.fatec.giulianogimenez.cpc.controller.PrecoController;
import br.gov.sp.fatec.giulianogimenez.cpc.model.Estabelecimento;
import br.gov.sp.fatec.giulianogimenez.cpc.model.Preco;

public class PrecoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EstabelecimentoController estabelecimentoController = new EstabelecimentoController(getBaseContext());
        final PrecoController precoController = new PrecoController(getBaseContext());
        setContentView(R.layout.activity_preco);
        Intent intent = getIntent();
        String estNome = intent.getStringExtra("EST_NOME");
        Cursor estabelecimentoCursor = estabelecimentoController.carregaPorNome(estNome);
        estabelecimentoCursor.moveToFirst();
        TextView title = (TextView)this.findViewById(R.id.titulo);
        title.setText(estabelecimentoCursor.getString(estabelecimentoCursor.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_NOME)));
        TextView snippet = (TextView)this.findViewById(R.id.endereco);
        snippet.setText(estabelecimentoCursor.getString(estabelecimentoCursor.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_ENDERECO)));
        EditText txtPrecoGasolina = (EditText)this.findViewById(R.id.txtPrecoGasolina);
        EditText txtPrecoEtanol = (EditText)this.findViewById(R.id.txtPrecoEtanol);
        txtPrecoGasolina.setText("");
        txtPrecoEtanol.setText("");
        Cursor precoCursor = precoController.carregaPrecoEstabelecimento(estabelecimentoCursor.getString(estabelecimentoCursor.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_NOME)));
        precoCursor.moveToFirst();
        while(true) {
            if(precoCursor.getCount() == 0)
                break;
            if(precoCursor.getString(precoCursor.getColumnIndex(Preco.PrecoInfo.PRC_TIPOCOMBUSTIVEL)).equals("Gasolina")) {
                txtPrecoGasolina.setText(precoCursor.getString(precoCursor.getColumnIndex(Preco.PrecoInfo.PRC_VALOR)));
            } else if(precoCursor.getString(precoCursor.getColumnIndex(Preco.PrecoInfo.PRC_TIPOCOMBUSTIVEL)).equals("Etanol")) {
                txtPrecoEtanol.setText(precoCursor.getString(precoCursor.getColumnIndex(Preco.PrecoInfo.PRC_VALOR)));
            }
            precoCursor.moveToNext();
            if(precoCursor.isAfterLast()) {
                break;
            }
        }
        Button salvar = (Button)this.findViewById(R.id.btnSalvarPrecos);
        salvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                View parentView = (View)v.getParent();
                EditText txtPrecoGasolina = (EditText)parentView.findViewById(R.id.txtPrecoGasolina);
                EditText txtPrecoEtanol = (EditText)parentView.findViewById(R.id.txtPrecoEtanol);
                Intent intent = getIntent();
                String estNome = intent.getStringExtra("EST_NOME");
                PrecoController precoController = new PrecoController(getBaseContext());
                if(!txtPrecoGasolina.getText().toString().equals(""))
                    precoController.inserir(Float.parseFloat(txtPrecoGasolina.getText().toString()), "Gasolina", estNome);
                if(!txtPrecoEtanol.getText().toString().equals(""))
                    precoController.inserir(Float.parseFloat(txtPrecoEtanol.getText().toString()), "Etanol", estNome);
                Toast.makeText(getBaseContext(),"Preço salvo com sucesso.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


}
