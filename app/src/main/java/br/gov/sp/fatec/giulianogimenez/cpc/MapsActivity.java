package br.gov.sp.fatec.giulianogimenez.cpc;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
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

import br.gov.sp.fatec.giulianogimenez.cpc.DAO.DAOConnection;
import br.gov.sp.fatec.giulianogimenez.cpc.controller.EstabelecimentoController;
import br.gov.sp.fatec.giulianogimenez.cpc.controller.PrecoController;
import br.gov.sp.fatec.giulianogimenez.cpc.model.Estabelecimento;
import br.gov.sp.fatec.giulianogimenez.cpc.model.Preco;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnInfoWindowClickListener {

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;
    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.infoview, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            EstabelecimentoController estabelecimentoController = new EstabelecimentoController(getBaseContext());
            PrecoController precoController = new PrecoController(getBaseContext());
            Cursor c = estabelecimentoController.carregaPorNome(marker.getTitle());
            if(c.getCount() > 0) {
                c.moveToFirst();
                TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
                tvTitle.setText(c.getString(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_NOME)));
                TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
                tvSnippet.setText(c.getString(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_ENDERECO)));
                Cursor precoC = precoController.carregaPrecoEstabelecimento(c.getString(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_NOME)));
                TextView txGasolina = ((TextView)myContentsView.findViewById(R.id.txtPrecoGasolina));
                TextView txEtanol = ((TextView)myContentsView.findViewById(R.id.txtPrecoEtanol));
                TextView txtServicos = (TextView)myContentsView.findViewById(R.id.txtServicos);
                txGasolina.setText("--.--");
                txEtanol.setText("--.--");
                txtServicos.setText("");
                precoC.moveToFirst();
                while(true) {
                    if(precoC.getCount() == 0)
                        break;
                    if(precoC.getString(precoC.getColumnIndex(Preco.PrecoInfo.PRC_TIPOCOMBUSTIVEL)).equals("Gasolina")) {
                        txGasolina.setText("R$" + precoC.getString(precoC.getColumnIndex(Preco.PrecoInfo.PRC_VALOR)));
                    } else if(precoC.getString(precoC.getColumnIndex(Preco.PrecoInfo.PRC_TIPOCOMBUSTIVEL)).equals("Etanol")) {
                        txEtanol.setText("R$" + precoC.getString(precoC.getColumnIndex(Preco.PrecoInfo.PRC_VALOR)));
                    }
                    precoC.moveToNext();
                    if(precoC.isAfterLast()) {
                        break;
                    }
                }
                String servicos = "";
                if(c.getInt(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_ALIMENCACAO)) == 1)
                    servicos += "+ Alimentação\n";
                if(c.getInt(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_CONVENIENCIA)) == 1)
                    servicos += "+ Conveniência\n";
                if(c.getInt(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_LAVARAPIDO)) == 1)
                    servicos += "+ Lava Rápido\n";
                if(c.getInt(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_TROCAOLEO)) == 1)
                    servicos += "+ Troca de Óleo\n";
                if(c.getInt(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_CAIXAELETRONICO)) == 1)
                    servicos += "+ Caixa Eletrônico\n";
                if(c.getInt(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_MECANICO)) == 1)
                    servicos += "+ Oficina Mecânica\n";
                if(c.getInt(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_SEMPARAR)) == 1)
                    servicos += "+ Sem Parar\n";
                if(c.getInt(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_VIAFACIL)) == 1)
                    servicos += "+ Via Fácil\n";
                txtServicos.setText(servicos);
            }
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }


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
        mGoogleMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        EstabelecimentoController estabelecimentoController = new EstabelecimentoController(getBaseContext());
        PrecoController precoController = new PrecoController(getBaseContext());
        Cursor c = estabelecimentoController.carregaPorNome(marker.getTitle());
        if(c.getCount() > 0) {
            c.moveToFirst();
            precoController.carregaPrecoEstabelecimento(c.getString(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_NOME)));
            Intent intent = new Intent(this, PrecoActivity.class);
            intent.putExtra("EST_NOME", c.getString(c.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_NOME)));
            startActivity(intent);
            marker.hideInfoWindow();
        }
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
        estabelecimentoController.inserir("Posto da Gruta",
                "Av. Dep. Benedito Matarazzo, 4229 - Jardim das Industrias, São José dos Campos - SP, 12246-840",
                "-23.234035", "-45.916072",
                true, true, true, true, true, true, true, true, true, "Ipiranga");
        estabelecimentoController.inserir("Posto Cassiopéia",
                "Avenida Cassiopeia, 1024 - Jardim Satelite, São José dos Campos - SP, 12230-010",
                "-23.228943", "-45.890818",
                true, false, true, true, false, false, false, false, false, "Ipiranga");
        estabelecimentoController.inserir("Auto Posto Tak",
                "Av. Andrômeda, 2477 - Jardim Satelite, São José dos Campos - SP",
                "-23.235048", "-45.884587",
                true, false, true, true, false, false, false, false, false, "Ipiranga");
        estabelecimentoController.inserir("Auto Posto BR - Posto Satélite",
                "R. Benedito Alves Moreira, 166 - Jardim Satelite, São José dos Campos - SP, 12231-750",
                "-23.231714", "-45.879705",
                false, false, true, true, false, false, false, false, false, "Br");
        estabelecimentoController.inserir("Auto Posto BR - Posto Cata Vento",
                "Av. Eng. Francisco José Longo, 1324 - Centro, São José dos Campos - SP, 12245-001",
                "-23.204752", "-45.888366",
                true, true, true, true, false, false, false, false, false, "Br");
        estabelecimentoController.inserir("Auto Posto BR - Posto Morumbi",
                "R. Gisele Martins, 401 - Cidade Morumbi, São José dos Campos - SP, 12236-500",
                "-23.250250", "-45.897123",
                true, false, true, true, false, false, false, false, false, "Br");
        estabelecimentoController.inserir("Gascem Posto Petrobrás",
                "Av. Cidade Jardim, 920 - Floradas de São José, São José dos Campos - SP, 12231-675",
                "-23.220980", "-45.884819",
                true, true, true, true, false, false, false, false, false, "Br");
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
            } else if(bandeira.equals("Br")) {
                mko.icon(BitmapDescriptorFactory.fromResource(R.drawable.br_pin));
            }
            mko.title(postos.getString(postos.getColumnIndex(Estabelecimento.EstabelecimentoInfo.EST_NOME)));
            mGoogleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
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
