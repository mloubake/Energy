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
import android.util.Log;
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

    private static final String TAG = "";
    private GoogleMap equipeMap;

    private FirebaseFirestore mFirestore;
    private Task<DocumentSnapshot> documentSnapshotTask;

    double geoLat, geoLongi;
    String strLocalizacao;

    int count =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //tentar jogar a busca no map ready

        Bundle extras = getIntent().getExtras();

        if(strLocalizacao == null){
            strLocalizacao = extras.getString("localizacao");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFirestore = FirebaseFirestore.getInstance();


    }


    //Update
    /*
    Thread t = new Thread(){
        @Override
        public void run(){
            while (!isInterrupted()){
                try{
                    Thread.sleep(10000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                        }
                    });
                }
                catch (InterruptedException e){

                }
            }
        }
    };
    */



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





        mFirestore.collection("EquipeManutencao").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() ) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if(String.valueOf(document.getData().get("numRequerimento")).equals(String.valueOf(strLocalizacao))){

                            geoLat = Double.parseDouble(String.valueOf(document.getGeoPoint("localizacao").getLatitude()));
                            geoLongi = Double.parseDouble(String.valueOf(document.getGeoPoint("localizacao").getLongitude()));

                            // Add a marker in Sydney and move the camera
                            LatLng equipeBrazil = new LatLng( geoLat, geoLongi);
                            equipeMap.addMarker(new MarkerOptions().position(equipeBrazil).title("Equipe"));
                            equipeMap.moveCamera(CameraUpdateFactory.newLatLng(equipeBrazil));

                        }
                    }
                }
            }
        });



/*
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @SuppressWarnings("unchecked")
                    public void run() {
                        try {

                        }
                        catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        //timer.schedule(doTask, 0, "Your time 10 minute");
*/






/*
        Thread t = new Thread(){
            @Override
            public void run(){
                while (!isInterrupted()){
                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                            }
                        });
                    }
                    catch (InterruptedException e){

                    }
                }
            }
        };
*/


        //pesquisar sobre handler









    }
}
