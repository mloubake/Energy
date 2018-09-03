package caiococaro.com.br.energy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "";
    private GoogleMap equipeMap;
    private GoogleMap usuarioMap;

    private FirebaseFirestore mFirestore;
    private Task<DocumentSnapshot> documentSnapshotTask;


    String strLocalizacao = null;
    int localEquipe;

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Bundle extras = getIntent().getExtras();


        if(strLocalizacao != null){
            strLocalizacao = extras.getString("localizacao");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("EquipeManutencao").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() ) {
                    for (DocumentSnapshot document : task.getResult()) {
                       if(document.getData().get("numRequerimento").equals(strLocalizacao)){
                            localEquipe = Integer.valueOf(String.valueOf(document.getData().get("localizacao")));
                            Log.d(TAG, "LOCALEQUIPELOCALEQUIPELOCALEQUIPELOCALEQUIPE "+localEquipe);




                       }
                    }

                }
            }
        });





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
        usuarioMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng brazil = new LatLng(localEquipe, localEquipe);
        equipeMap.addMarker(new MarkerOptions().position(brazil).title("Equipe"));
        equipeMap.moveCamera(CameraUpdateFactory.newLatLng(brazil));

        LatLng userBrazil = new LatLng(-22.886434, -43.115283);
        usuarioMap.addMarker(new MarkerOptions().position(userBrazil).title("Usuário"));
        usuarioMap.moveCamera(CameraUpdateFactory.newLatLng(userBrazil));


        Thread t = new Thread(){
            @Override
            public void run(){
                while (!isInterrupted()){
                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                /*
                                // Add a marker in Sydney and move the camera
                                LatLng brazil = new LatLng(count, -43.012719);
                                equipeMap.addMarker(new MarkerOptions().position(brazil).title("Equipe"));
                                equipeMap.moveCamera(CameraUpdateFactory.newLatLng(brazil));

                                LatLng userBrazil = new LatLng(-22.886434, -43.115283);
                                usuarioMap.addMarker(new MarkerOptions().position(userBrazil).title("Usuário"));
                                usuarioMap.moveCamera(CameraUpdateFactory.newLatLng(userBrazil));

                                Log.d(TAG, ""+count);*/
                                count++;

                            }
                        });
                    }
                    catch (InterruptedException e){

                    }
                }
            }
        };




    }
}
