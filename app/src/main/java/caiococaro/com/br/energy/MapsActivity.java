package caiococaro.com.br.energy;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;



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


    private GoogleMap equipeMap;

    private FirebaseFirestore mFirestore;
    private Task<DocumentSnapshot> documentSnapshotTask;

    double geoLat, geoLongi;
    String strLocalizacao;

    int count = 1;

    // Create the Handler object (on the main thread by default)
    Handler handler = new Handler();
    Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //tentar jogar a busca no map ready

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            strLocalizacao = bundle.getString(KEY_LOCALIZACAO);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFirestore = FirebaseFirestore.getInstance();


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

        Thread t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){

                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mFirestore.collection(TABLE_EQUIPE_MANUTENCAO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                if (String.valueOf(document.getData().get(FIELD_NUM_REQUERIMENTO)).equals(String.valueOf(strLocalizacao))) {

                                                    geoLat = Double.parseDouble(String.valueOf(document.getGeoPoint(FIELD_LOCALIZACAO).getLatitude()));
                                                    geoLongi = Double.parseDouble(String.valueOf(document.getGeoPoint(FIELD_LOCALIZACAO).getLongitude()));


                                                    // Add a marker in Sydney and move the camera
                                                    LatLng equipeBrazil = new LatLng(geoLat, geoLongi);
                                                    equipeMap.addMarker(new MarkerOptions().position(equipeBrazil).title(TITLE_EQUIPE));
                                                    equipeMap.moveCamera(CameraUpdateFactory.newLatLng(equipeBrazil));

                                                }
                                            }
                                        }
                                    }
                                });
                                Toast.makeText(getApplicationContext(), "AAAAA", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();








    }

}


