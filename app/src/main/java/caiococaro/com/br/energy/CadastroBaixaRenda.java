package caiococaro.com.br.energy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static caiococaro.com.br.energy.MenuPrincipal.KEY_CPF_CNPJ;
import static caiococaro.com.br.energy.MenuPrincipal.KEY_ENDERECO;
import static caiococaro.com.br.energy.MenuPrincipal.KEY_NOME;

public class CadastroBaixaRenda extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    private static final String TAG = "";

    //Tabelas do FireStore
    private static final String TABLE_BAIXA_RENDA = "BaixaRenda";
    private static final String TABLE_USUARIO = "Usuario";

    //Keys da Bundle (São os dados (ou valores) da bundle da Activity anterior)
    public static final String KEY_NUM_CLIENTE = "NumeroCliente";
    public static final String KEY_CPFCNPJ = "cpfCnpj";
    public static final String KEY_NUM_REQUERIMENTO = "NumRequerimento";

    //Campos do Firebase
    private static final String FIELD_CPF_CNPJ = "cpfCnpj";
    private static final String FIELD_ENDERECO = "endereco";
    private static final String FIELD_IS_BAIXA_RENDA_CADASTRADO = "isBaixaRendaCadastrado";
    private static final String FIELD_NOME = "nome";
    private static final String FIELD_NUM_CLIENTE = "numCliente";
    private static final String FIELD_NIS = "NIS";



    String nomeCliente, numCliente, endCliente, CPF, NIS;

    Bundle bundle = new Bundle();

    EditText etNIS;
    TextView tvNumCliente, tvNomeCliente,
            tvEndCliente, tvCpfCnpj;

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

        etNIS = findViewById(R.id.etNIS);
        tvNumCliente = findViewById(R.id.TVNumCliente);
        tvNomeCliente = findViewById(R.id.tvnomeCliente);
        tvCpfCnpj = findViewById(R.id.tvcpfCliente);
        tvEndCliente = findViewById(R.id.tvendCliente);

        tvNumCliente.setText(String.valueOf("Número do cliente: " + numCliente));
        tvCpfCnpj.setText(String.valueOf("CPF: " + CPF));
        tvNomeCliente.setText(String.valueOf("Nome: " + nomeCliente));
        tvEndCliente.setText(String.valueOf("Endereço: " + endCliente));

        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (String.valueOf(numCliente).equals(String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)))) {
                            if (document.getData().get(FIELD_IS_BAIXA_RENDA_CADASTRADO).equals(true)) {
                                usuarioCadastrado();
                            } else {
                                //Procura se o documento existe e tem aquele número dentro desse documento
                                mFirestore.collection(TABLE_BAIXA_RENDA).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                if (document.exists() &&
                                                        String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)).equals(numCliente)
                                                        && document.getData().get("isBaixaRendaAndamento").equals(true)) {

                                                    aguardandoAnalise();
                                                    etNIS.setText(String.valueOf(document.getData().get(FIELD_NIS)));
                                                    tvNomeCliente.setText(String.valueOf(document.getData().get(FIELD_NOME)));
                                                    tvCpfCnpj.setText(String.valueOf(document.getData().get(FIELD_CPF_CNPJ)));
                                                    tvEndCliente.setText(String.valueOf(document.getData().get(FIELD_ENDERECO)));
                                                }
                                                return;
                                            }
                                        }
                                    }
                                });
                            }
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

    public void aguardandoAnalise (){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        dialogBuilder.setMessage("Análise em andamento...");

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

}

