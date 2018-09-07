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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class ConsumoTempoReal extends AppCompatActivity {

    private FirebaseFirestore db;
    private static final String TAG = "";
    boolean ctrl;
    int num_cliente;

    float PIS;
    float COFINS;
    float ICMS;
    float CIP;

    float TUSD;//*********************************************
    float TE;//*********************************************
    float BantarVerde;//*********************************************
    float BantarAmarela;//*********************************************
    float BantarVermelha1;//*********************************************
    float BantarVermelha2;//*********************************************


    float bVerde;
    float bAzul;
    float bAmarela;
    float bVermelha;

    String leituraAnterior;
    String leituraAtual;
    int leituraMes;



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

        final TextView etleituraAnterior = (TextView) findViewById(R.id.etleituraAnterior);
        final TextView etleituraAtual = (TextView) findViewById(R.id.etleituraAtual);

        db = FirebaseFirestore.getInstance();

        db.collection("Usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (String.valueOf(num_cliente).equals(String.valueOf(document.getData().get("numCliente")))) {

                            leituraAnterior = String.valueOf(document.getData().get("leituraAnterior"));

                            leituraAtual = String.valueOf(document.getData().get("leituraAtual"));

                            leituraMes = (Integer.valueOf(leituraAtual)) - (Integer.valueOf(leituraAnterior));

                            String cip = String.valueOf(document.getData().get("CIP"));
                            CIP = Float.valueOf(cip);

                            etleituraAnterior.setText(leituraAnterior);
                            etleituraAtual.setText(leituraAtual);

                        }
                    }
                }
            }
        });

        db.collection("TarifasAplicacao").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String pis="";
                String cofins="";
                String icms="";

                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                            pis = String.valueOf(document.getData().get("PIS"));

                            cofins = String.valueOf(document.getData().get("COFINS"));

                            icms = String.valueOf(document.getData().get("ICMS"));
                        }
                }
                PIS = Float.valueOf(pis);
                COFINS = Float.valueOf(cofins);
                ICMS = Float.valueOf(icms);
                }
        });

        //Não recupera TE e TUSD

        DocumentReference docRefBantar = db.collection("TarifasAplicacao").document("Bantar");

        docRefBantar.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String te="";
                String tusd="";

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                        te = String.valueOf(document.getData().get("TE"));

                        tusd = String.valueOf(document.getData().get("TUSD"));

                    }
                    Toast.makeText(getApplicationContext(),"TE "+te,Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"TUSD "+tusd,Toast.LENGTH_LONG).show();

                }
                //TE = Float.valueOf(te);
                //TUSD = Float.valueOf(tusd);
                //Toast.makeText(getApplicationContext(),"TE "+TE,Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),"TUSD "+TUSD,Toast.LENGTH_LONG).show();
        });

        db.collection("TarifasAplicacao").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String bantVerde="";
                String bantAmarela="";
                String bantVermelha1="";
                String bantVermelha2="";

                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        bantVerde = String.valueOf(document.getData().get("verde"));

                        bantAmarela = String.valueOf(document.getData().get("amarela"));

                        bantVermelha1 = String.valueOf(document.getData().get("vermelha1"));

                        bantVermelha2 = String.valueOf(document.getData().get("vermelha2"));
                    }
                }
                //BantarVerde = Float.valueOf(bantVerde);
                //BantarAmarela = Float.valueOf(bantAmarela);
                //BantarVermelha1 = Float.valueOf(bantVermelha1);
                //BantarVermelha2 = Float.valueOf(bantVermelha2);

                Toast.makeText(getApplicationContext(),"Bantar Vermelha 2 "+bantVermelha2,Toast.LENGTH_LONG).show();
            }
        });

    }
}



