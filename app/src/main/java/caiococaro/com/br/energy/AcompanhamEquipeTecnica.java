package caiococaro.com.br.energy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

public class AcompanhamEquipeTecnica extends AppCompatActivity {

    //TAG do Log do Console
    private static final String TAG = "";

    //Tabelas do FireStore
    private static final String TABLE_USUARIO = "Usuario";
    private static final String TABLE_EQUIPE_MANUTENCAO = "EquipeManutencao";

    //Campos do Firebase
    private static final String FIELD_NUM_REQUERIMENTO = "numRequerimento";
    private static final String FIELD_TOKEN_ACESSO = "tokenAcesso";

    //Keys da Bundle
    private static final String KEY_NUM_CLIENTE = "NumeroCliente";
    private static final String KEY_TOKEN_ACESSO = "TokenAcesso";
    private static final String KEY_NUM_REQUERIMENTO = "NumRequerimento";

    //Names para cada Bundle Específica
    private static final String NAME_ACOMPANHAMENTO = "Acompanhamento";
    private static final String CHANNEL_ID = "ID Pessoal";

    FirebaseFirestore mFirestore;

    Bundle bundle = new Bundle();
    String numRequerimento;

    int statusPedido;

    double porcStatusPedido;
    ProgressBar mProgresBar;
    LinearLayout layoutStatus;
    TextView txtNaoSolicitado;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;
    ArrayList<ImageView> imgArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanham_equipe_tecnica);


        bundle = getIntent().getExtras();
        if (bundle != null) {
            numRequerimento = bundle.getString(NAME_ACOMPANHAMENTO);
            Log.d("", "TOKENACESSO: " + numRequerimento);
        }

        mProgresBar = findViewById(R.id.progress);
        img1 = findViewById(R.id.imagem1);
        img2 = findViewById(R.id.imagem2);
        img3 = findViewById(R.id.imagem3);
        img4 = findViewById(R.id.imagem4);
        img5 = findViewById(R.id.imagem5);
        img6 = findViewById(R.id.imagem6);
        imgArrayList.add(img1);
        imgArrayList.add(img2);
        imgArrayList.add(img3);
        imgArrayList.add(img4);
        imgArrayList.add(img5);
        imgArrayList.add(img6);
        txtNaoSolicitado = findViewById(R.id.txt_nao_solicitado);

        layoutStatus = findViewById(R.id.layoutStatus);

        acompanhamentoBD();

    }

    public void acompanhamentoBD(){
//        TODO numSolicitação em cima dos status
        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.enableNetwork();

        mFirestore.collection(TABLE_EQUIPE_MANUTENCAO).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    if (String.valueOf(document.getData().get(FIELD_NUM_REQUERIMENTO)).equals(String.valueOf(numRequerimento))) {
                        Log.d("", "STATUS-PEDIDO: " + String.valueOf(document.getData().get("statusPedido")));
                        statusPedido = Integer.valueOf(String.valueOf(document.getData().get("statusPedido")));
                        showStatus();
                        if(statusPedido >= 1 && statusPedido <= imgArrayList.size()){
                            Log.d("", "***STATUS-PEDIDO: " + statusPedido);
                            tintIcon();
                        }else{
                            hideStatus();
                        }

//                        switch (statusPedido) {
//                            case 1:
//                                Log.d("", "***STATUS-PEDIDO: " + statusPedido);
//                                tintIcon();
//                                break;
//                            case 2:
//                                Log.d("", "***STATUS-PEDIDO: " + statusPedido);
//                                tintIcon();
//                                break;
//                            case 3:
//                                Log.d("", "***STATUS-PEDIDO: " + statusPedido);
//                                tintIcon();
//                                break;
//                            case 4:
//                                Log.d("", "***STATUS-PEDIDO: " + statusPedido);
//                                tintIcon();
//                                break;
//                            case 5:
//                                Log.d("", "***STATUS-PEDIDO: " + statusPedido);
//                                tintIcon();
//                                break;
//                            case 6:
//                                Log.d("", "***STATUS-PEDIDO: " + statusPedido);
//                                tintIcon();
//                                break;
//                            default:
//                                hideStatus();
//                                break;
//                        }
                    }
                }
            }
        });

    }

    public void tintIcon(){
        mProgresBar.setProgress(100/imgArrayList.size() * statusPedido);
        for(ImageView iv : imgArrayList){
            int index = imgArrayList.indexOf(iv);
            if (index < statusPedido) {
                imgArrayList.get(index).setImageResource(R.drawable.check);
            }if (index == statusPedido) {
                imgArrayList.get(index).setImageResource(R.drawable.carro);
            } if(index > statusPedido) {
                imgArrayList.get(index).setImageResource(R.drawable.vazio);
            }
        }
    }

    private void hideStatus() {
        layoutStatus.setVisibility(View.GONE);
        txtNaoSolicitado.setVisibility(View.VISIBLE);
    }
    private void showStatus() {
        layoutStatus.setVisibility(View.VISIBLE);
        txtNaoSolicitado.setVisibility(View.GONE);
    }
}