package caiococaro.com.br.energy;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "";
    //private Firebase mRef;
    //private Button mSendData;
    //Declaração da Inicialiação do FireStore
    private FirebaseFirestore mFirestore;
    boolean ctrl = false;

    //Fazer BD de equipes de manutenção


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
                final String numClinte = etNumCliente.getText().toString();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("CpfCnpj", cpfCnpj);
                userMap.put("NumCliente", numClinte);


                //Recuperando os dados
                mFirestore.collection("Usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() ) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(String.valueOf(document.getData().get("CpfCnpj")).equals(String.valueOf(etCpfCnpj.getText()))
                                        && String.valueOf(document.getData().get("NumCliente")).equals(String.valueOf(etNumCliente.getText()))) {
                                    //                          etRecuperando.setText( document.getData().get("idUser").toString());

                                    Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
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


                            if(ctrl==true){
                                Toast.makeText(getApplicationContext(), "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                            }
                             else{
                                Toast.makeText(getApplicationContext(),"Dados incorretos.Verifique e tente novamente." , Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Log.w(TAG, "Falha ao recuperar documents.", task.getException());
                        }
                    }
                });
            }
        });
    }
}
