package caiococaro.com.br.energy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

public class ConsumoTempoReal extends AppCompatActivity {

    //TAG do Log do Console
    private static final String TAG = "";

    //Tabelas do FireStore
    private static final String TABLE_USUARIO = "Usuario";

    //Campos do Firebase
    private static final String FIELD_TOKEN_ACESSO = "tokenAcesso";

    //Keys da Bundle
    private static final String KEY_NUM_CLIENTE = "NumeroCliente";
    private static final String KEY_TOKEN_ACESSO = "TokenAcesso";

    //Names para cada Bundle Específica

    private static final String NAME_CONSUMO = "Usuario";
    private static final String NAME_CONSUMO_ATUAL = "ConsumoAtual";
    private ProgressDialog progress;


    String numCliente;

    private FirebaseFirestore mFirestore;

    int ctrl = 0;

    float PIS;
    float COFINS;
    float ICMS;
    float CIP;

    float TUSD;
    float TE;
    float BantarVerde;
    float BantarAmarela;
    float BantarVermelha1;
    float BantarVermelha2=0;

    float bVerde;
    float bAzul;
    float bAmarela;
    float bVermelha;

    float tarifa;
    float valor_consumo;
    float consumo;
    float estimativaConsumo;
    float totalEstimado;

    String leituraAnterior;
    int leituraAtual;
    int leituraMes;

    int lastDay;

    String tokenAcesso;

    Bundle bundle = new Bundle();

    TextView tvLeituraAnterior = null;
    TextView tvLeituraAtual = null;
    TextView tvCIP = null;
    TextView tvConsumo = null;
    TextView tvTarifa = null;
    TextView tvValor_consumo_parcial = null;
    TextView tvTotal_estimado = null;
    PieChart grafico = null;
    Button btnAtualizar = null;

    SimpleDateFormat formatData = new SimpleDateFormat("dd-MM-yyyy");

    final DecimalFormat df = new DecimalFormat("#,###.##");

    final DecimalFormat tf = new DecimalFormat("#.#####");

    Date data = new Date();

    String dataFormatada = formatData.format(data);

    String hoje = String.valueOf(dataFormatada);

    int today = 23;
    int ultimoDia = 31;
    int diasDif=0;

    //String um = String.valueOf(today.indexOf("-"));
    //String dois = String.valueOf(today.lastIndexOf("-"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_tempo_real);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        lastDay = calendar.DAY_OF_MONTH;

        tvLeituraAnterior = (TextView) findViewById(R.id.tvLeituraAnterior);
        tvLeituraAtual = (TextView) findViewById(R.id.tvLeituraAtual);
        tvCIP = (TextView) findViewById(R.id.tvCIP);
        tvConsumo = (TextView) findViewById(R.id.tvConsumo);
        tvConsumo = (TextView) findViewById(R.id.tvConsumo);
        tvTarifa = (TextView) findViewById(R.id.tvTarifa);
        tvValor_consumo_parcial = (TextView) findViewById(R.id.tvValor_consumo_parcial);
        tvTotal_estimado = (TextView) findViewById(R.id.tvTotal_estimado);
        btnAtualizar = (Button) findViewById(R.id.btnAtualizar);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            numCliente = bundle.getString(NAME_CONSUMO);

        }

        progress = new ProgressDialog(ConsumoTempoReal.this);
        progress.setMessage("Calculando...");
        progress.show();

        mFirestore = FirebaseFirestore.getInstance();

        pesquisa1();

    }


    void update() {
        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (numCliente.equals(String.valueOf(document.getData().get("numCliente")))) {

                            //ATUALIZAR A LEITURA ATUAL AQUI
                            mFirestore.collection(TABLE_USUARIO).document("padrão").update("leituraAtual", leituraAtual);
                            pesquisa1();
                        }
                    }
                }
            }
        });

    }

    void pesquisa1() {
        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (String.valueOf(numCliente).equals(String.valueOf(document.getData().get("numCliente")))) {

                            leituraAnterior = String.valueOf(document.getData().get("leituraAnterior"));
                            Log.d(TAG, "LEITURAANTERIOR: " + leituraAnterior);

                            String LA = String.valueOf(document.getData().get("leituraAtual"));
                            leituraAtual = Integer.valueOf(LA);
                            Log.d(TAG, "LEITURAATUAL: " + leituraAtual);
                            leituraMes = (Integer.valueOf(leituraAtual)) - (Integer.valueOf(leituraAnterior));

                            consumo = Float.valueOf(leituraMes);

                            String cip = String.valueOf(document.getData().get("CIP"));
                            CIP = Float.valueOf(cip);

                            tokenAcesso = document.getData().get(FIELD_TOKEN_ACESSO).toString();
                            Log.d(TAG, "TOKEN_ACESSO: " + tokenAcesso);

                        }
                        //calcula();
                    }
                }
            }

        });


        pesquisa2();
    }

    void pesquisa2() {

        DocumentReference docRefTributario = mFirestore.collection("TarifasAplicacao").document("Tributario");
        docRefTributario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String pis = "";
                String cofins = "";
                String icms = "";

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
                //calcula();
            }


        });
        pesquisa3();
    }

    void pesquisa3() {

        DocumentReference docRefResidencial = mFirestore.collection("TarifasAplicacao").document("Residencial");

        docRefResidencial.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String te = "";
                String tusd = "";

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        te = String.valueOf(document.getData().get("TE"));
                        tusd = String.valueOf(document.getData().get("TUSD"));


                    }
                }
                TE = Float.valueOf(te);
                TUSD = Float.valueOf(tusd);
                calcula();
            }
        });
        pesquisa4();
    }

    void pesquisa4() {

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
                //calcula();
            }
        });
    }

    void calcula() {

        //BANDEIRA
        bVerde = (TUSD + TE) / 1000;
        bAzul = (TUSD / 1000);
        bAmarela = (BantarAmarela + bVerde);
        bVermelha = (bVerde + BantarVermelha2);

        tarifa = bVermelha;

        //TRIBUTOS
        PIS *= tarifa;
        COFINS *= tarifa;
        ICMS *= tarifa;

        tarifa += PIS + COFINS + ICMS;

        valor_consumo = consumo * tarifa;

            /*PIS *= valor_consumo;
            COFINS *= valor_consumo;
            ICMS *= valor_consumo;

            valor_consumo += PIS + COFINS + ICMS;*/

        //RESULTADO
        valor_consumo += CIP;

        //Toast.makeText(getApplicationContext(),"today: "+today,Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),"lastDay: "+lastDay,Toast.LENGTH_LONG).show();

        atualizar();
    }

    void atualizar() {

        diasDif = ultimoDia - today;

        estimativaConsumo = (valor_consumo/today);

        estimativaConsumo = estimativaConsumo * diasDif;

        totalEstimado = valor_consumo + estimativaConsumo;

        tvValor_consumo_parcial.setText(String.valueOf(df.format(valor_consumo)));
        tvTarifa.setText(String.valueOf(tf.format(tarifa)));
        tvTotal_estimado.setText(String.valueOf(df.format(totalEstimado)));
        tvLeituraAtual.setText(String.valueOf(leituraAtual));
        tvLeituraAnterior.setText(leituraAnterior);
        tvLeituraAtual.setText(String.valueOf(leituraAtual));
        tvCIP.setText(String.valueOf(df.format(CIP)));
        tvConsumo.setText(String.valueOf(leituraMes));

        grafico();
    }

    void grafico() {


/*        String vc = "R$ ".concat(String.valueOf(df.format(valor_consumo)));
        String ec = "R$ ".concat(String.valueOf(df.format(estimativaConsumo)));*/

        float intensGrafico[] = {valor_consumo, estimativaConsumo};

        String descricao[] = {"Consumo parcial","Consumo estimado"};

        grafico = (PieChart) findViewById(R.id.graficoID);

        List<PieEntry> entradasGrafico = new ArrayList<>();

        for (int i = 0; i < intensGrafico.length; i++) {
            entradasGrafico.add(new PieEntry(intensGrafico[i], descricao[i]));
        }

        PieDataSet dataSet = new PieDataSet(entradasGrafico, "");

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueFormatter(new PercentFormatter());
        dataSet.setHighlightEnabled(true);


        PieData pieData = new PieData(dataSet);


        grafico.setData(pieData);
        grafico.invalidate();

        grafico.animateXY(2000, 2000);
        grafico.setUsePercentValues(true);
        grafico.setDrawSliceText(true);


        progress.dismiss();

    }

    public void AtualizaConsumo(View view) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_atualizar, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.leituraAtualMedidor);

        PIS = 0;
        COFINS = 0;
        ICMS = 0;
        CIP = 0;

        TE = 0;
        BantarVerde = 0;
        BantarAmarela = 0;
        BantarVermelha1 = 0;
        BantarVermelha2 = 0;

        bVerde = 0;
        bAzul = 0;
        bAmarela = 0;
        bVermelha = 0;

        valor_consumo = 0;
        consumo = 0;
        estimativaConsumo = 0;
        totalEstimado = 0;

        dialogBuilder
                .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //atualizar
                        leituraAtual = (Integer.valueOf(editText.getText().toString()));

                        if(leituraAtual < Integer.valueOf(leituraAnterior)){

                            Toast.makeText(getApplicationContext(),"ERRO: verifique a leitura e tente novamente",Toast.LENGTH_LONG).show();
                            
                        }
                        else {

                            //Recebendo bundle do MenuActivity com vários valores
                            bundle = getIntent().getExtras();

                            if (bundle != null) {
                                numCliente = bundle.getString(NAME_CONSUMO);
                            }

                            progress = new ProgressDialog(ConsumoTempoReal.this);
                            progress.setMessage("Calculando...");
                            progress.show();

                            update();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //cancelar

                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
}







