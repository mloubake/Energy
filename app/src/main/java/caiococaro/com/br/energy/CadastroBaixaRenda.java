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

public class CadastroBaixaRenda extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    //Tabelas do FireStore
    private static final String TABLE_BAIXA_RENDA = "BaixaRenda";
    private static final String TABLE_USUARIO = "Usuario";

    //Variáveis da Bundle
    private static final String VAR_BUNDLE_NUM_CLIENTE = "NumeroCliente";
    private static final String VAR_BUNDLE_CPF_CNPJ = "CpfCnpj";
    private static final String VAR_BUNDLE_NOME = "Nome";
    private static final String VAR_BUNDLE_ENDERECO = "Endereco";

    //Campos do Firebase
    private static final String FIELD_CPF_CNPJ = "cpfCnpj";
    private static final String FIELD_ENDERECO = "endereco";
    private static final String FIELD_IS_BAIXA_RENDA_ANDAMENTO = "isBaixaRendaAndamento";
    public static final String FIELD_IS_BAIXA_RENDA_CADASTRADO = "isBaixaRendaCadastrado";
    private static final String FIELD_NOME_CLIENTE = "nomeCliente";
    private static final String FIELD_NUM_CLIENTE = "numCliente";
    private static final String FIELD_NIS = "NIS";

    //Texts da classe
    private static final String TEXT_ANALISE_ANDAMENTO = "Análise em andamento...";
    private static final String TEXT_CADASTRO_BAIXA_RENDA_SOLICITADO = "Cadastro de baixa renda já solicitado!";
    private static final String TEXT_CPF = "CPF: ";
    private static final String TEXT_ENDERECO = "Endereço: ";
    private static final String TEXT_OK = "Ok";
    private static final String TEXT_NOME = "Nome: ";
    private static final String TEXT_NUMERO_CLIENTE = "Número do cliente: ";
    private static final String TEXT_SOLICITACAO = "Solicitação enviada para análise com sucesso!";


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
            numCliente = bundle.getString(VAR_BUNDLE_NUM_CLIENTE);
            nomeCliente = bundle.getString(VAR_BUNDLE_NOME);
            endCliente = bundle.getString(VAR_BUNDLE_ENDERECO);
            CPF = bundle.getString(VAR_BUNDLE_CPF_CNPJ);
        }

        etNIS = findViewById(R.id.etNIS);
        tvNumCliente = findViewById(R.id.TVNumCliente);
        tvNomeCliente = findViewById(R.id.tvnomeCliente);
        tvCpfCnpj = findViewById(R.id.tvcpfCliente);
        tvEndCliente = findViewById(R.id.tvendCliente);

        tvNumCliente.setText(String.valueOf(TEXT_NUMERO_CLIENTE + numCliente));
        tvCpfCnpj.setText(String.valueOf(TEXT_CPF + CPF));
        tvNomeCliente.setText(String.valueOf(TEXT_NOME + nomeCliente));
        tvEndCliente.setText(String.valueOf(TEXT_ENDERECO + endCliente));

        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (String.valueOf(numCliente).equals(String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)))) {
                            if(document.getData().get(FIELD_IS_BAIXA_RENDA_CADASTRADO).equals(true)) {
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
                                                        && document.getData().get(FIELD_IS_BAIXA_RENDA_ANDAMENTO).equals(true)) {

                                                    aguardandoAnalise();
                                                    etNIS.setText(String.valueOf(document.getData().get(FIELD_NIS)));
                                                    tvNomeCliente.setText(String.valueOf(document.getData().get(FIELD_NOME_CLIENTE)));
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
                    .setPositiveButton(TEXT_OK, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //atualizar
                            criaDocumento();

                            Intent intent = new Intent(CadastroBaixaRenda.this, MenuPrincipal.class);
                            startActivity(intent);
                        }
                    });

            dialogBuilder.setMessage(TEXT_SOLICITACAO);

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

        }

        void usuarioCadastrado(){

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            dialogBuilder
                    .setMessage(TEXT_CADASTRO_BAIXA_RENDA_SOLICITADO)
                    .setPositiveButton(TEXT_OK, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {

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

            data.put(FIELD_NIS,NIS);
            data.put(FIELD_NUM_CLIENTE,numCliente);
            data.put(FIELD_NOME_CLIENTE,nomeCliente);
            data.put(FIELD_CPF_CNPJ,CPF);
            data.put(FIELD_ENDERECO,endCliente);

            mFirestore.collection(TABLE_BAIXA_RENDA).add(data);

        }

    public void aguardandoAnalise (){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder
                .setPositiveButton(TEXT_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        dialogBuilder.setMessage(TEXT_ANALISE_ANDAMENTO);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

}

