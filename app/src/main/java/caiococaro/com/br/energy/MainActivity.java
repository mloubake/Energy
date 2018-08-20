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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainActivity extends AppCompatActivity {


    //private Firebase mRef;
    //private Button mSendData;
    //Declaração da Inicialiação do FireStore
    private FirebaseFirestore mFirestore;


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


        Button button = (Button) findViewById(R.id.btnEntrar);
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
                String numClinte = etNumCliente.getText().toString();

                Map<String, String> userMap = new HashMap<>();

                userMap.put("CpfCnpj", cpfCnpj);
                userMap.put("NumCliente", numClinte);

                //Adiciona uma coleção "Usuários" ao DB
                mFirestore.collection("Usuarios").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        mFirestore.collection("Usuarios").document().get();

                        if (etCpfCnpj.getText().toString() == userId.toString()){
                            Toast.makeText(MainActivity.this, "Recuperando", Toast.LENGTH_LONG).show();
                           // etRecuperando.setText(userId);
                        }



                        /*
                        Toast.makeText(MainActivity.this, "Login realizado", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                        startActivity(intent);
                        */
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String erro = e.getMessage();
                        Toast.makeText(MainActivity.this, "Error: " + erro, Toast.LENGTH_LONG).show();
                    }
                });





            }
        });



    }
}
