package caiococaro.com.br.energy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static caiococaro.com.br.energy.MenuPrincipal.KEY_CPF_CNPJ;
import static caiococaro.com.br.energy.MenuPrincipal.KEY_ENDERECO;
import static caiococaro.com.br.energy.MenuPrincipal.KEY_NOME;
import static caiococaro.com.br.energy.MenuPrincipal.KEY_NUM_CLIENTE;

public class CadastroBaixaRenda extends AppCompatActivity {

    private static final String TABLE_USUARIO = "Usuario";
    private FirebaseFirestore mFirestore;

    String nomeCliente, numCliente, endCliente, CPF, NIS;

    Bundle bundle = new Bundle();

    EditText etNIS = null;
    TextView etNumCliente = null;
    TextView tvNomeCliente = null;
    TextView tvEndCliente = null;
    TextView tvCpfCnpj = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_baixa_renda);

        mFirestore = FirebaseFirestore.getInstance();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            numCliente = bundle.getString(KEY_NUM_CLIENTE);
            nomeCliente = bundle.getString(KEY_NOME);
            endCliente = bundle.getString(KEY_ENDERECO);
            CPF = bundle.getString(KEY_CPF_CNPJ);
        }

        etNIS = (EditText) findViewById(R.id.etNIS);
        etNumCliente = (TextView) findViewById(R.id.TVNumCliente);
        tvNomeCliente = (TextView) findViewById(R.id.tvnomeCliente);
        tvCpfCnpj = (TextView) findViewById(R.id.tvcpfCliente);
        tvEndCliente = (TextView) findViewById(R.id.tvendCliente);

        etNumCliente.setText(String.valueOf("Número do cliente: " + numCliente));
        tvCpfCnpj.setText(String.valueOf("CPF: " + CPF));
        tvNomeCliente.setText(String.valueOf("Nome: " + nomeCliente));
        tvEndCliente.setText(String.valueOf("Endereço: " + endCliente));

        mFirestore.collection("BaixaRenda").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (String.valueOf(numCliente).equals(String.valueOf(document.getData().get("numCliente")))) {

                          usuarioCadastrado();

                            }

                    }
                }
            }

        });

    }

        public void Enviar (View view){


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            dialogBuilder
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //atualizar

                            criaDocumento();

                            Intent intent = new Intent(CadastroBaixaRenda.this, MenuPrincipal.class);
                            startActivity(intent);
                        }
                    });

            dialogBuilder.setMessage("Solicitação enviada para análise com sucesso!");

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

        }

        void usuarioCadastrado(){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            dialogBuilder
                    .setMessage("Cadastro de baixa renda já solicitado!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //atualizar

                            Intent intent = new Intent(CadastroBaixaRenda.this, MenuPrincipal.class);
                            startActivity(intent);
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

        }

        void criaDocumento(){

            NIS = String.valueOf(etNIS.getText());

            Map<String, Object> data = new HashMap<>();

            data.put("NIS",NIS);
            data.put("numCliente",numCliente);
            data.put("nome",nomeCliente);
            data.put("cpfCnpj",CPF);
            data.put("endereco",endCliente);

            mFirestore.collection("BaixaRenda").add(data);

        }


}

