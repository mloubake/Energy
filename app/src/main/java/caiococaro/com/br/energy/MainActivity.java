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
    String teteus = "Teteus";

    //Fazer BD de equipes de manutenção


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*NÃO APAGAR, USAR COMO AJUDA/EXEMPLO
        EditText etCpfCnpj = (EditText) findViewById(R.id.etCpfCnpj);
        EditText etNumCliente = (EditText) findViewById(R.id.etNumCliente);
        Button button = (Button) findViewById(R.id.btnEntrar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Criar botão de cadastro
                //Tentar botar um forEach para sempre incrementar um Indice

                final String valueCpf = etCpfCnpj.getText().toString();
                String valueNumCliente = etNumCliente.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRefCpfCnpj = database.getReference("CpfCnpj");
                DatabaseReference myRefNumCliente = database.getReference("NumCliente");

                myRefCpfCnpj.setValue(valueCpf);
                myRefNumCliente.setValue(valueNumCliente);

                Toast.makeText(getApplicationContext(),"enviado", Toast.LENGTH_LONG).show();;
                //Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                //startActivity(intent);

            }
        });
    */


        final Button button = (Button) findViewById(R.id.btnEntrar);
        final EditText etCpfCnpj = (EditText) findViewById(R.id.etCpfCnpj);
        final EditText etNumCliente = (EditText) findViewById(R.id.etNumCliente);
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


                /*
                //Cria uma Coleção e add um Documento à essa coleção
                mFirestore.collection("Usuario").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
                */

                //Recuperando os dados
                mFirestore.collection("Usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() ) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if(String.valueOf(document.getData().get("CpfCnpj")).equals(String.valueOf(etCpfCnpj.getText()))
                                        && String.valueOf(document.getData().get("NumCliente")).equals(String.valueOf(etNumCliente.getText()))){
          //                          etRecuperando.setText( document.getData().get("idUser").toString());

                                    Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Login Efetuado com Sucesso.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "entrou no if");



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
                                else {
                                    Toast.makeText(getApplicationContext(),"Dados incorretos. Verifique novamente." , Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "entrou no else");
                                    /*
                                    Log.d(TAG, "22 Valor do recuperando: " + String.valueOf(etRecuperando.getText()));
                                    etRecuperando.setText( "idUser não encontrado");
                                    Log.d(TAG, "333 Valor do recuperando: " + String.valueOf(etRecuperando.getText()));
                                    */
                                }
                            }
                        }
                        else{
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
            }
        });
    }
}
