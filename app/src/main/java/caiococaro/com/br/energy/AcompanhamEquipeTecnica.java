package caiococaro.com.br.energy;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AcompanhamEquipeTecnica extends AppCompatActivity {

    boolean ctrl = false;
    private FirebaseFirestore mFirestore;
    private static final String TAG = "";
    boolean var1;
    boolean var2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanham_equipe_tecnica);

        //final FirebaseFirestore mFirestore;


        Button btnPesquisar = (Button) findViewById(R.id.btnPesquisarEquipe);
        final EditText etNumRequerimento = (EditText) findViewById(R.id.etNumRequerimento);

        mFirestore = FirebaseFirestore.getInstance();




        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String numRequerimento = etNumRequerimento.getText().toString();

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("numRequerimento", numRequerimento);


                //Var1
                mFirestore.collection("Usuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() ) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(String.valueOf(document.getData().get("numRequerimento")).equals(String.valueOf(etNumRequerimento.getText()))) {
                                    Log.d(TAG, mFirestore.collection("Usuario").document("numRequerimento").getId());
                                    Log.d(TAG, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                                    var1 = true;
                                    ctrl = true;
                                    break;
                                }
                                else{
                                    ctrl = false;
                                    var1 = false;
                                    Log.d(TAG, "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
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

                            if(ctrl==true){
                               //Toast.makeText(getApplicationContext(), "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //Toast.makeText(getApplicationContext(),"Dados incorretos.Verifique e tente novamente." , Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });


                //Var2
                mFirestore.collection("EquipeManutencao").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() ) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(String.valueOf(document.getData().get("numRequerimento")).equals(String.valueOf(etNumRequerimento.getText()))) {
                                    Log.d(TAG, mFirestore.collection("EquipeManutencao").document("numRequerimento").getId());
                                    Log.d(TAG, "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                                    var2 = true;
                                    ctrl = true;
                                    break;
                                }
                                else{
                                    ctrl = false;
                                    var2 = false;
                                }
                            }
                            if(ctrl==true){
                                //Toast.makeText(getApplicationContext(), "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //Toast.makeText(getApplicationContext(),"Dados incorretos.Verifique e tente novamente." , Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

                Log.d(TAG, "Var1 antes: "+var1);
                Log.d(TAG, "Var2 antes: "+var2);

                if((var1)&&(var2)){
                    Toast.makeText(getApplicationContext(), "Var 1 e Var 2", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "LOG DOS VARS");
                    Log.d(TAG, "var1: "+var1);
                    Log.d(TAG, "var2: "+var2);

                    String texto = etNumRequerimento.getText().toString();
                    Intent intent = new Intent(AcompanhamEquipeTecnica.this, MapsActivity.class);
                    Bundle b = new Bundle();
                    b.putString("localizacao", texto);
                    Log.d(TAG,""+b);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });
    }

}