package br.gov.sp.fatec.giulianogimenez.cpc;

import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.gov.sp.fatec.giulianogimenez.cpc.controller.EstabelecimentoController;
import br.gov.sp.fatec.giulianogimenez.cpc.model.Estabelecimento;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) throws SecurityException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) throws SecurityException {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);

        buildGoogleApiClient();

        mGoogleApiClient.connect();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @Override
    public void onConnected(Bundle bundle) throws SecurityException {
        Toast.makeText(this,"Carregando mapa",Toast.LENGTH_SHORT).show();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        EstabelecimentoController estabelecimentoController = new EstabelecimentoController(getBaseContext());
        Cursor postos = estabelecimentoController.carregaDados();
        if(postos.getCount() > 0) {
            Toast.makeText(this,"Limpando dados.",Toast.LENGTH_SHORT).show();
            estabelecimentoController.limpaBase();
        }
        estabelecimentoController.inserir("Impéria",
                "Av. Dr. João Batista de Souza Soares, 2105 - Cidade Morumbi, São José dos Campos - SP, 12236-660",
                "-23.242443", "-45.906174",
                true, true, true, true, false, false, false, false, false, "Shell");
        estabelecimentoController.inserir("Posto Carrefour",
                "Av. Dep. Benedito Matarazzo, 5207 - Parque Res. Aquarius",
                "-23.224164", "-45.906034",
                false, false, false, false, false, false, false, false, false, "Shell");
        estabelecimentoController.inserir("Posto Ipê",
                "Av. Guadalupe, 659 - Jardim America",
                "-23.231848", "-45.901247",
                true, false, true, true, false, false, false, true, false, "Shell");
        estabelecimentoController.inserir("Posto Ipê",
                "Av. Guadalupe, 659 - Jardim America",
                "-23.231848", "-45.901247",
                true, false, true, true, false, false, false, true, false, "Shell");
        estabelecimentoController.inserir("Posto Ipiranga - Guadalupe",
                "Av. Guadalupe, 660 - Jardim America",
                "-23.231626", "-45.901140",
                true, true, true, true, false, false, false, false, true, "Ipiranga");
        estabelecimentoController.inserir("Posto Bola Branca - Shell",
                "Av. Dr. João Batista de Souza Soares, 255 - Jardim Anhembi",
                "-23.223207", "-45.900820",
                true, true, true, true, false, false, false, true, false, "Shell");
        postos = estabelecimentoController.carregaDados();
        postos.moveToFirst();
        while(true) {
            MarkerOptions mko = new MarkerOptions();
            mko.position(new LatLng(postos.getFloat(postos.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_LAT)),
                            postos.getFloat(postos.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_LONG))));
            String bandeira = postos.getString(postos.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_BANDEIRA));
            if(bandeira.equals("Shell")) {
                mko.icon(BitmapDescriptorFactory.fromResource(R.drawable.shell_pin));
            } else if(bandeira.equals("Ipiranga")) {
                mko.icon(BitmapDescriptorFactory.fromResource(R.drawable.ipiranga_pin));
            }
            mGoogleMap.addMarker(mko);
            postos.moveToNext();
            if(postos.isAfterLast()) {
                break;
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Conexão suspensa",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Conexão expirou",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

}
