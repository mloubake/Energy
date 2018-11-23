package caiococaro.com.br.energy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConsumoTempoReal extends AppCompatActivity {

    //Tabelas do FireStore
    private static final String TABLE_USUARIO = "Usuario";
    private static final String TABLE_TARIFAS_APLICACAO = "TarifasAplicacao";

    //Documentos do Firestore
    private static final String DOCUMENT_BANTAR= "Bantar";
    private static final String DOCUMENT_RESIDENCIAL= "Residencial";
    private static final String DOCUMENT_TRIBUTARIO= "Tributario";


    //Campos do Firestore - Usuario
    private static final String FIELD_CIP= "CIP";
    private static final String FIELD_LEITURA_ANTERIOR = "leituraAnterior";
    private static final String FIELD_LEITURA_ATUAL= "leituraAtual";
    private static final String FIELD_NUM_CLIENTE = "numCliente";
    private static final String FIELD_TOKEN_ACESSO = "tokenAcesso";

    //Campos do Firestore - Aplicação - Tributário
    private static final String FIELD_COFINS= "COFINS";
    private static final String FIELD_ICMS= "ICMS";
    private static final String FIELD_PIS= "PIS";

    //Campos do Firestore - Aplicação - Tributário
    private static final String FIELD_TE= "TE";
    private static final String FIELD_TUSD= "TUSD";

    private static final String FIELD_AMARELA= "amarela";
    private static final String FIELD_VERDE= "verde";
    private static final String FIELD_VERMELHA_1= "vermelha1";
    private static final String FIELD_VERMELHA_2= "vermelha2";

    //Variáveis da Bundle
    private static final String VAR_BUNDLE_NUM_CLIENTE = "NumeroCliente";

    //Texts da classe
    private static final String TEXT_ATUALIZAR = "Atualizar";
    private static final String TEXT_CALCULANDO = "Calculando...";
    private static final String TEXT_CANCELAR = "Cancelar";
    private static final String TEXT_ERRO_VERIFIQUE_LEITURA = "ERRO: verifique a leitura e tente novamente";
    private static final String TEXT_ERRO_VERIFIQUE_DADOS = "Verifique os dados informados e tente novamente";


    private ProgressDialog progress;

    String numCliente;

    private FirebaseFirestore mFirestore;
    String myId;

    int ctrl = 0;

    float PIS, COFINS, ICMS, CIP;

    float TUSD, TE;
    float BantarVerde, BantarAmarela,
            BantarVermelha1,
            BantarVermelha2=0;

    float bVerde, bAzul, bAmarela, bVermelha;

    float tarifa, valor_consumo, consumo,
            estimativaConsumo, totalEstimado;

    int leituraAnterior, leituraAtual,
            leituraMes, lastDay;

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

    int today = Integer.valueOf(dataFormatada.substring(0,2));

    Calendar c = Calendar.getInstance();

    int ultimoDia = Integer.valueOf(c.getActualMaximum(Calendar.DAY_OF_MONTH));
    int diasDif = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumo_tempo_real);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        lastDay = calendar.DAY_OF_MONTH;

        tvLeituraAnterior = findViewById(R.id.tvLeituraAnterior);
        tvLeituraAtual = findViewById(R.id.tvLeituraAtual);
        tvCIP = findViewById(R.id.tvCIP);
        tvConsumo = findViewById(R.id.tvConsumo);
        tvConsumo = findViewById(R.id.tvConsumo);
        tvTarifa = findViewById(R.id.tvTarifa);
        tvValor_consumo_parcial = findViewById(R.id.tvValor_consumo_parcial);
        tvTotal_estimado = findViewById(R.id.tvTotal_estimado);
        btnAtualizar = findViewById(R.id.btnAtualizar);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            numCliente = bundle.getString(VAR_BUNDLE_NUM_CLIENTE);
        }

        progress = new ProgressDialog(ConsumoTempoReal.this);
        progress.setMessage(TEXT_CALCULANDO);
        progress.show();

        mFirestore = FirebaseFirestore.getInstance();

        pesquisa();

    }


    void update() {
        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (numCliente.equals(String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)))) {
                            //Atualiza leituras
                            mFirestore.collection(TABLE_USUARIO).document(myId).update(FIELD_LEITURA_ANTERIOR, leituraAnterior);
                            mFirestore.collection(TABLE_USUARIO).document(myId).update(FIELD_LEITURA_ATUAL, leituraAtual);
                            pesquisa();
                        }
                    }
                }
            }
        });
    }

    void pesquisa() {
        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (String.valueOf(numCliente).equals(String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)))) {
                            myId = document.getId();
                            leituraAnterior = Integer.valueOf(String.valueOf(document.getData().get(FIELD_LEITURA_ANTERIOR)));

                            String LA = String.valueOf(document.getData().get(FIELD_LEITURA_ATUAL));
                            leituraAtual = Integer.valueOf(LA);
                            leituraMes = (Integer.valueOf(leituraAtual)) - (Integer.valueOf(leituraAnterior));

                            consumo = Float.valueOf(leituraMes);

                            String cip = String.valueOf(document.getData().get(FIELD_CIP));
                            CIP = Float.valueOf(cip);

                            tokenAcesso = document.getData().get(FIELD_TOKEN_ACESSO).toString();

                        }
                    }
                }
            }

        });


        pesquisa2();
    }

    void pesquisa2() {

        DocumentReference docRefTributario = mFirestore.collection(TABLE_TARIFAS_APLICACAO).document(DOCUMENT_TRIBUTARIO);
        docRefTributario.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String pis = "";
                String cofins = "";
                String icms = "";

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        pis = String.valueOf(document.getData().get(FIELD_PIS));
                        cofins = String.valueOf(document.getData().get(FIELD_COFINS));
                        icms = String.valueOf(document.getData().get(FIELD_ICMS));
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

        DocumentReference docRefResidencial = mFirestore.collection(TABLE_TARIFAS_APLICACAO).document(DOCUMENT_RESIDENCIAL);

        docRefResidencial.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String te = "";
                String tusd = "";

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        te = String.valueOf(document.getData().get(FIELD_TE));
                        tusd = String.valueOf(document.getData().get(FIELD_TUSD));


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

        DocumentReference docRefBantar = mFirestore.collection(TABLE_TARIFAS_APLICACAO).document(DOCUMENT_BANTAR);
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
                        bantVerde = String.valueOf(document.getData().get(FIELD_VERDE));
                        bantAmarela = String.valueOf(document.getData().get(FIELD_AMARELA));
                        bantVermelha1 = String.valueOf(document.getData().get(FIELD_VERMELHA_1));
                        bantVermelha2 = String.valueOf(document.getData().get(FIELD_VERMELHA_2));
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

        //RESULTADO
        valor_consumo += CIP;

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
        tvLeituraAnterior.setText(String.valueOf(leituraAnterior));
        tvLeituraAtual.setText(String.valueOf(leituraAtual));
        tvCIP.setText(String.valueOf(df.format(CIP)));
        tvConsumo.setText(String.valueOf(leituraMes));

        grafico();
    }

    void grafico() {

        float intensGrafico[] = {valor_consumo, estimativaConsumo};

        String descricao[] = {"","" };

        grafico = findViewById(R.id.graficoID);

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

        final EditText anterior = dialogView.findViewById(R.id.leituraAnteriorMedidor);
        final EditText atual = dialogView.findViewById(R.id.leituraAtualMedidor);

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
                .setPositiveButton(TEXT_ATUALIZAR, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //atualizar
                        if((!anterior.getText().toString().equals("")) && (!atual.getText().toString().equals(""))){

                            leituraAnterior = (Integer.valueOf(anterior.getText().toString()));
                            leituraAtual = (Integer.valueOf(atual.getText().toString()));



                            if(leituraAtual < Integer.valueOf(leituraAnterior)){

                                Toast.makeText(getApplicationContext(),TEXT_ERRO_VERIFIQUE_LEITURA,Toast.LENGTH_LONG).show();

                            }
                            else {

                                //Recebendo bundle do MenuActivity com vários valores
                                bundle = getIntent().getExtras();

                                if (bundle != null) {
                                    numCliente = bundle.getString(VAR_BUNDLE_NUM_CLIENTE);
                                }

                                progress = new ProgressDialog(ConsumoTempoReal.this);
                                progress.setMessage(TEXT_CALCULANDO);
                                progress.show();

                                update();
                            }
                        }
                        else{
                                Toast.makeText(getApplicationContext(),TEXT_ERRO_VERIFIQUE_DADOS,Toast.LENGTH_LONG).show();

                        }

                    }
                })
                .setNegativeButton(TEXT_CANCELAR, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //cancelar

                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
}
