package caiococaro.com.br.energy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConsumoTempoReal extends AppCompatActivity {

    //TAG do Log do Console
    private static final String TAG = "";

    //Tabelas do FireStore
    private static final String TABLE_USUARIO = "Usuario";

    //Campos do Firebase
    private static final String FIELD_CPF_CNPJ = "cpfCnpj";
    private static final String FIELD_NUM_CLIENTE = "numCliente";
    private static final String FIELD_TOKEN_ACESSO = "tokenAcesso";

    //Keys da Bundle
    private static final String KEY_NUM_CLIENTE = "NumeroCliente";
    private static final String KEY_TOKEN_ACESSO = "TokenAcesso";

    //Names para cada Bundle Específica
//    private static final String NAME_ACOMPANHAMENTO = "Acompanhamento";
//    private static final String NAME_BAIXA_RENDA = "BaixaRenda";
//    private static final String NAME_CLIENTE_VITAL = "ClienteVital";
      private static final String NAME_CONSUMO = "Consumo";
//    private static final String NAME_HISTORICO = "Historico";
//    private static final String NAME_RECLAMACAO = "Reclamacao";


    String numCliente;

    private FirebaseFirestore mFirestore;
    boolean ctrl;

    float PIS;
    float COFINS;
    float ICMS;
    float CIP;

    float TUSD;
    float TE;
    float BantarVerde;
    float BantarAmarela;
    float BantarVermelha1;
    float BantarVermelha2;


    float bVerde;
    float bAzul;
    float bAmarela;
    float bVermelha;

    float tarifa;
    float valor_consumo;

    String leituraAnterior;
    String leituraAtual;
    int leituraMes;

    Bundle bundle = new Bundle();

    /*
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String NumCliente = intent.getStringExtra("ConsumoTempoReal");

            num_cliente = Integer.valueOf(NumCliente);
        }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_tempo_real);

        //Recebendo bundle do MenuActivity com vários valores
        bundle = getIntent().getExtras();
        Log.d(TAG, "XXXXXXXXXXXXXX: "+bundle);

        if(bundle != null ){
            numCliente = bundle.getString(NAME_CONSUMO);
            Log.d(TAG, "ACTIVITYCONSUMO: "+numCliente);
        }


        WebView mWebView = (WebView) findViewById(R.id.web_view);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //mWebView.loadUrl("http://google.com");

        //LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("ConsumoTempoReal"));

        final TextView tvLeituraAnterior = (TextView) findViewById(R.id.tvLeituraAnterior);
        final TextView tvLeituraAtual = (TextView) findViewById(R.id.tvLeituraAtual);
        final TextView tvCIP = (TextView) findViewById(R.id.tvCIP);
        final TextView tvConsumo = (TextView) findViewById(R.id.tvConsumo);
        final TextView tvTarifa = (TextView) findViewById(R.id.tvTarifa);
        final TextView tvValor_consumo = (TextView) findViewById(R.id.tvValor_consumo);

        final DecimalFormat df = new DecimalFormat("#.##");

        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (String.valueOf(numCliente).equals(String.valueOf(document.getData().get("numCliente")))) {

                            leituraAnterior = String.valueOf(document.getData().get("leituraAnterior"));
                            Log.d(TAG, "LEITURAANTERIOR: "+leituraAnterior);

                            leituraAtual = String.valueOf(document.getData().get("leituraAtual"));
                            Log.d(TAG, "LEITURAATUAL: "+leituraAtual);
                            leituraMes = (Integer.valueOf(leituraAtual)) - (Integer.valueOf(leituraAnterior));

                            String cip = String.valueOf(document.getData().get("CIP"));
                            CIP = Float.valueOf(cip);

                            tvLeituraAnterior.setText(leituraAnterior);
                            tvLeituraAtual.setText(leituraAtual);
                            tvCIP.setText(String.valueOf(df.format(CIP)));
                            tvConsumo.setText(String.valueOf(leituraMes));

                        }
                    }
                }
            }
        });

        DocumentReference docRefTributario = mFirestore.collection("TarifasAplicacao").document("Tributario");
        docRefTributario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String pis="";
                String cofins="";
                String icms="";

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

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


        DocumentReference docRefResidencial = mFirestore.collection("TarifasAplicacao").document("Residencial");

        docRefResidencial.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String te="";
                String tusd="";

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                            te = String.valueOf(document.getData().get("TE"));

                            tusd = String.valueOf(document.getData().get("TUSD"));

                    }
                }
                TE = Float.valueOf(te);
                TUSD = Float.valueOf(tusd);
            }
        });

        DocumentReference docRefBantar = mFirestore.collection("TarifasAplicacao").document("Bantar");
        docRefBantar.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String bantVerde = "";
                String bantAmarela = "";
                String bantVermelha1 = "";
                String bantVermelha2 = "";

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        bantVerde = String.valueOf(document.getData().get("verde"));

                        bantAmarela = String.valueOf(document.getData().get("amarela"));

                        bantVermelha1 = String.valueOf(document.getData().get("vermelha1"));

                        bantVermelha2 = String.valueOf(document.getData().get("vermelha2"));
                    }
                }
                BantarVerde = Float.valueOf(bantVerde);
                BantarAmarela = Float.valueOf(bantAmarela);
                BantarVermelha1 = Float.valueOf(bantVermelha1);
                BantarVermelha2 = Float.valueOf(bantVermelha2);

                bVerde = (TUSD + TE) / 1000;
                bAzul = (TUSD / 1000);
                bAmarela = (BantarAmarela + bVerde);
                bVermelha = (bVerde + BantarVermelha2);

                tarifa = bVermelha;

                tvTarifa.setText(String.valueOf(tarifa));

                Float consumo = Float.valueOf(leituraMes);

                //Toast.makeText(getApplicationContext(),""+leituraMes,Toast.LENGTH_LONG).show();

                valor_consumo = consumo * tarifa;

                PIS = valor_consumo*PIS;

                COFINS = valor_consumo*COFINS;

                ICMS = valor_consumo*ICMS;

                valor_consumo = valor_consumo + PIS + COFINS + ICMS + CIP;

                tvValor_consumo.setText(String.valueOf(df.format(valor_consumo)));

            }
        });


    }


    public void showDialog(View view){

        MyDialog myDialog = new MyDialog();
        myDialog.show(getSupportFragmentManager(),"my_dialog");
    }


}



