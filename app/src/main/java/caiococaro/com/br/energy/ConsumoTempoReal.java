package caiococaro.com.br.energy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ConsumoTempoReal extends AppCompatActivity {

    private FirebaseFirestore db;
    private static final String TAG = "";
    boolean ctrl;
    int num_cliente;

    float TUSD;//*********************************************
    float TE;//*********************************************
    float kwBantar1;//*********************************************
    float kwBantar2;//*********************************************
    float kwBantar3;//*********************************************


    float bVerde;
    float bAzul;
    float bAmarela;
    float bVermelha;

    String kwAnterior;
    String kwAtual;
    int kwMes;


    /*colocar após recuperação de dados
    bVerde = (TUSD+TE)/1000;
    bAzul = (TUSD/1000);
    bAmarela = (kwBantar1+bVerde);
    bVermelha = (bVerde+kwBantar3);
    kwMes = (kwAtual-kwAnterior);
    */

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String NumCliente = intent.getStringExtra("ConsumoTempoReal");

            num_cliente = Integer.valueOf(NumCliente);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_tempo_real);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("ConsumoTempoReal"));

        final TextView etkwAnterior = (TextView) findViewById(R.id.etkwAnterior);
        final TextView etkwAtual = (TextView) findViewById(R.id.etkwAtual);

        db = FirebaseFirestore.getInstance();

        db.collection("Usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (String.valueOf(num_cliente).equals(String.valueOf(document.getData().get("numCliente")))) {

                            kwAnterior = String.valueOf(document.getData().get("kwAnterior"));

                            kwAtual = String.valueOf(document.getData().get("kwAtual"));

                            kwMes = (Integer.valueOf(kwAtual)) - (Integer.valueOf(kwAnterior));

                            etkwAnterior.setText(kwAnterior);
                            etkwAtual.setText(kwAtual);


                        }
                    }
                }
            }
        });


    }
}




