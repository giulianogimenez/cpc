package br.gov.sp.fatec.giulianogimenez.cpc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PrecoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preco);
        Intent intent = getIntent();
        //Integer est_id = intent.getIntExtra("est_id");
    }
}
