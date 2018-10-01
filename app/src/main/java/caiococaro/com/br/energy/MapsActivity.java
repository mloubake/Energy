package caiococaro.com.br.energy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //TAG do Log do Console
    private static final String TAG = "";

    //Tabelas do FireStore
    private static final String TABLE_EQUIPE_MANUTENCAO = "EquipeManutencao";

    //Campos do Firebase
    private static final String FIELD_NUM_REQUERIMENTO = "numRequerimento";
    private static final String FIELD_LOCALIZACAO = "localizacao";

    //Keys da Bundle
    private static final String KEY_LOCALIZACAO = "Localizacao";

    //Título dos Marcadores do Maps
    private static final String TITLE_EQUIPE = "Equipe";
    private static final String TITLE_USER = "Usuário";


    private FirebaseFirestore mFirestore;
    private Task<DocumentSnapshot> documentSnapshotTask;


    private GoogleMap equipeMap, usuarioMap;
    double geoLat, geoLongi;
    double userGeoLat, userGeoLongi;
    String strLocalizacao;


    // Create the Handler object (on the main thread by default)
    Handler handler = new Handler();
    Runnable mRunnable;

    LocationManager LM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            strLocalizacao = bundle.getString(KEY_LOCALIZACAO);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFirestore = FirebaseFirestore.getInstance();

        LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = LM.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location l = LM.getLastKnownLocation(bestProvider);
        if(l!=null){
            userGeoLat = l.getLatitude();
            userGeoLongi = l.getLongitude();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        equipeMap = googleMap;
        usuarioMap = googleMap;

        final LatLng userBrazil = new LatLng(userGeoLat, userGeoLongi);
        usuarioMap.addMarker(new MarkerOptions().position(userBrazil).title(TITLE_USER));



/*

        //Thread para atualizar o Maps
        Thread t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try{
                        //Atualiza o mapa de X em X milissegundos
                        Thread.sleep(1000);
                        //Thread para aparecer na tela
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mFirestore.collection(TABLE_EQUIPE_MANUTENCAO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                if (String.valueOf(document.getData().get(FIELD_NUM_REQUERIMENTO)).equals(String.valueOf(strLocalizacao))) {

                                                    //Joga as coordenadas dentro das variáveis
                                                    geoLat = Double.parseDouble(String.valueOf(document.getGeoPoint(FIELD_LOCALIZACAO).getLatitude()));
                                                    geoLongi = Double.parseDouble(String.valueOf(document.getGeoPoint(FIELD_LOCALIZACAO).getLongitude()));

                                                    equipeMap.clear();
                                                    // Add a marker in Sydney and move the camera
                                                    LatLng equipeBrazil = new LatLng(geoLat, geoLongi);
                                                    equipeMap.addMarker(new MarkerOptions().position(equipeBrazil).title(TITLE_EQUIPE)  );
                                                    equipeMap.moveCamera(CameraUpdateFactory.newLatLng(equipeBrazil));

                                                    PolylineOptions linha = new PolylineOptions();

                                                    linha.add(equipeBrazil);

                                                    equipeMap.addPolyline(linha);


                                                }
                                            }
                                        }
                                    }
                                });

                            }
                        });

                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();*/
    }
}
