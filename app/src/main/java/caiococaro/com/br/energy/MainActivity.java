package caiococaro.com.br.energy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //TAG do Log do Console
    private static final String TAG = "";

    //Tabelas do FireStore
    private static final String TABLE_USUARIO = "Usuario";

    //Campos do Firebase
    private static final String FIELD_CPF_CNPJ = "cpfCnpj";
    private static final String FIELD_NUM_CLIENTE = "numCliente";
    private static final String FIELD_TOKEN_ACESSO = "tokenAcesso";

    //Keys da Bundle (Dados (ou valores) que serão passados para a próxima Activity através da Bundle)
    private static final String KEY_NUM_CLIENTE = "NumeroCliente";
    private static final String KEY_TOKEN_ACESSO = "TokenAcesso";


    //private Firebase mRef;
    //private Button mSendData;
    //Declaração da Inicialiação do FireStore
    private FirebaseFirestore mFirestore;
    boolean ctrl = false;

    public String tokenAcesso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //NÃO APAGAR, USAR COMO AJUDA/EXEMPLO

        final Button button = (Button) findViewById(R.id.btnEntrar);
        final EditText etCpfCnpj = (EditText) findViewById(R.id.etCpfCnpj);
        final EditText etNumCliente = (EditText) findViewById(R.id.etNumCliente);
        final String debugLogHeader = "Linklet Debug Message";
        //final EditText etRecuperando = (EditText) findViewById(R.id.etRecuperando);

        //Inicialização do FireStore
        mFirestore = FirebaseFirestore.getInstance();

        //Botão do Login
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //EditTexts passando para Strings para serem usadas no Map
                final String cpfCnpj = etCpfCnpj.getText().toString();
                final String numCliente = etNumCliente.getText().toString();

                //Recuperando os dados
                mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() ) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(String.valueOf(document.getData().get(FIELD_CPF_CNPJ)).equals(String.valueOf(etCpfCnpj.getText()))
                                        && String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)).equals(String.valueOf(etNumCliente.getText()))) {

                                    tokenAcesso = document.getData().get(FIELD_TOKEN_ACESSO).toString();
                                    Log.d(TAG, "TOKEN_ACESSO: "+tokenAcesso);

                                    //Iniciar activity MenuPrincipal com Bud
                                    Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(KEY_NUM_CLIENTE, numCliente);
                                    bundle.putString(KEY_TOKEN_ACESSO, tokenAcesso);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    ctrl = true;
                                    break;
                                }
                                else{
                                    ctrl = false;
                                }
                                    /*
                                    Log.d(TAG, "Valor do editText: " + String.valueOf(etCpfCnpj.getText()));
                                    Log.d(TAG, "1 Valor do recuperando: " + String.valueOf(etRecuperando.getText()));

                                    //Recupera o id do documento especificado
                                    Log.d(TAG, "getId: " + document.getId());

                                   //Recupera todos os campos do documento especificado
                                    Log.d(TAG, "getData: " + document.getData());

                                    //Recupera só o campo numClinete do documento especificado
                                    Log.d(TAG, "getData.get(NumCliente): " + document.getData().get("NumCliente"));

                                    //Recupera só o campo cpfCnpj do documento especificado
                                    Log.d(TAG, "getData.get(CpfCnpj): " + document.getData().get("CpfCnpj"));

                                    //Recupera só o campo idUser do documento especificado
                                    Log.d(TAG, "getData.get(idUser): " + document.getData().get("idUser"));
                                    Log.d(TAG, " ");
                                    */

                            }
                            //If para o Toast funcionar
                            if(ctrl==true){
                                Toast.makeText(getApplicationContext(), "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                            }
                             else{
                                Toast.makeText(getApplicationContext(),"Dados incorretos.Verifique e tente novamente." , Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Log.w(TAG, "Falha ao recuperar no Documents.", task.getException());
                        }
                    }
                });
            }
        });
    }
}
