package br.gov.sp.fatec.giulianogimenez.cpc;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Hamilton on 25/04/2017.
 */

public class MultiD extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
