package caiococaro.com.br.energy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
    private static final String KEY_LOCALIZACAO = "Localizacao";

    //Names para cada Bundle Específica
    private static final String NAME_ACOMPANHAMENTO = "Acompanhamento";

    boolean ctrl = false;
    private FirebaseFirestore mFirestore;
    boolean var1;
    boolean var2;

    String tokenAcesso;
    boolean tokenAcessoCheck;

    RelativeLayout progressContainer;
    EditText etNumRequerimento;

    Bundle bundle = new Bundle();



/*
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle tokenReceiver = intent.getBundleExtra("Acompanhamento2");

            tokenAcesso = Integer.valueOf("Acompanhamento");
            Log.d(TAG, "BROAD_ACESSO: "+tokenAcesso);
        }
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanham_equipe_tecnica);

        //Recebendo bundle do MenuActivity com vários valores

        bundle = getIntent().getExtras();

        if(bundle != null ){
            tokenAcesso = bundle.getString(NAME_ACOMPANHAMENTO);
        }

      // LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("Acompanhamento"));


        Button btnPesquisar = (Button) findViewById(R.id.btnPesquisarEquipe);
        etNumRequerimento = (EditText) findViewById(R.id.etNumRequerimento);
        progressContainer = (RelativeLayout) findViewById(R.id.progress_container);;

        mFirestore = FirebaseFirestore.getInstance();


        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressContainer.setVisibility(View.VISIBLE);
                final String numRequerimento = etNumRequerimento.getText().toString();

                pesquisaUsuario();
                pesquisaEquipe();
                pesquisaToken();

//                if(((var1)&&(var2)) && tokenAcessoCheck){
//
//                    String texto = etNumRequerimento.getText().toString();
//                    Intent intent = new Intent(AcompanhamEquipeTecnica.this, MapsActivity.class);
//                    Bundle b = new Bundle();
//                    b.putString("localizacao", texto);
//                    intent.putExtras(b);
//                    startActivity(intent);
//                }
//                else {
//                    Toast.makeText(getApplicationContext(), "Número de Requerimento incorreto", Toast.LENGTH_SHORT).show();
//                }

                //Chamando o Método: Começar a Activity do Google Maps
                /*try {
                    //sleep 5 seconds
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                startMapsActivity();



            }
        });
    }




    //Método: Checkar CPF e Número do cliente
    public boolean checkHasCpfAndClientNumber() {
        if ((var1 && var2) && tokenAcessoCheck) {
            return true;
        }
        return false;
    }

    //Método: Começar a Activity do Google Maps
    public void startMapsActivity() {
        if (checkHasCpfAndClientNumber()) {
            Toast.makeText(getApplicationContext(), "Localizando a sua equipe...", Toast.LENGTH_SHORT).show();
            String texto = etNumRequerimento.getText().toString();
            Intent intentMaps = new Intent(AcompanhamEquipeTecnica.this, MapsActivity.class);
            Bundle b = new Bundle();
            b.putString(KEY_LOCALIZACAO, texto);
            intentMaps.putExtras(b);
            startActivity(intentMaps);
        }
    }

    public void pesquisaUsuario(){

        //Recuperar da Coleção Usuário
        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() ) {
                    progressContainer.setVisibility(View.GONE);
                    Log.d(TAG, "ORDEM 1");
                    for (DocumentSnapshot document : task.getResult()) {
                        if( (String.valueOf(document.getData().get(FIELD_NUM_REQUERIMENTO)).equals(String.valueOf(etNumRequerimento.getText())))) {
                            var1 = true;
                            ctrl = true;
                            break;
                        }
                                    /*
                                    Log.d(TAG, "Valor do editText: " + String.valueOf(etCpfCnpj.getText()));
                                    Log.d(TAG, "1 Valor do recuperando: " + String.valueOf(etRecuperando.getText()));

                                    Log.d(TAG, "getId: " + document.getId());
                                    Log.d(TAG, "getData: " + document.getData());
                                    Log.d(TAG, "getData.get(NumCliente): " + document.getData().get("NumCliente"));
                                    Log.d(TAG, "getData.get(CpfCnpj): " + document.getData().get("CpfCnpj"));
                                    Log.d(TAG, "getData.get(idUser): " + document.getData().get("idUser"));
                                    Log.d(TAG, " ");
                                    */
                    }
                    //If para o Toast funcionar
                    if(ctrl==true){
                        //Sem Toast de confirmamento
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"AAA - Número de Requerimento incorreto." , Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Log.w(TAG, "Falha ao recuperar no Documents.", task.getException());
                }
            }
        });

    }

    public void pesquisaEquipe(){

        //Recuperar da Coleção EquipeManutenção
        mFirestore.collection(TABLE_EQUIPE_MANUTENCAO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() ) {
                    Log.d(TAG, "ORDEM 2");
                    for (DocumentSnapshot document : task.getResult()) {
                        if(String.valueOf(document.getData().get(FIELD_NUM_REQUERIMENTO)).equals(String.valueOf(etNumRequerimento.getText()))) {
                            var2 = true;
                            ctrl = true;
                            break;
                        }
                        else{
                            ctrl = false;
                            var2 = false;
                        }
                    }
                    //If para o Toast funcionar
                    if(ctrl==true){
                        //Sem Toast de confirmamento
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Não foi possível achar a equipe de manutenção. Por favor, verifique o Número de Requerimento." , Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Log.w(TAG, "Falha ao recuperar no Documents.", task.getException());
                }
            }
        });

    }

    public void pesquisaToken(){

        //Recuperar da Coleção Usuário verificando o Token de Acesso
        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() ) {
                    Log.d(TAG, "ORDEM 3");
                    for (DocumentSnapshot document : task.getResult()) {
                        if(String.valueOf(document.getData().get(FIELD_TOKEN_ACESSO)).equals(tokenAcesso)) {
                            tokenAcessoCheck = true;
                            Log.d(TAG, "AAA - TOKEN_ACESSO: "+tokenAcesso);
                            ctrl = true;
                            break;
                        }
                        else{
                            ctrl = false;
                        }
                    }
                    //If para o Toast funcionar
                    if(ctrl==true){
                        //Sem Toast de confirmamento
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "BBB - Número de Requerimento inválido", Toast.LENGTH_LONG).show();                            }
                }
                else{
                    Log.w(TAG, "Falha ao recuperar no Documents.", task.getException());
                }
            }
        });

    }

}

