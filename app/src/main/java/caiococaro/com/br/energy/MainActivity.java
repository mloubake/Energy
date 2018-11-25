package caiococaro.com.br.energy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    //TAG do Log do Console
    private static final String TAG = "";

    //Tabelas do FireStore
    private static final String TABLE_USUARIO = "Usuario";

    //Campos do Firebase
    private static final String FIELD_CPF_CNPJ = "cpfCnpj";
    private static final String FIELD_NUM_CLIENTE = "numCliente";
    private static final String FIELD_TOKEN_ACESSO = "tokenAcesso";
    private static final String FIELD_NUM_REQUERIMENTO = "numRequerimento";
    private static final String FIELD_NOME_CLIENTE = "nomeCliente";
    private static final String FIELD_ENDERECO_CLIENTE = "enderecoCliente";

    //Variáveis da Bundle
    private static final String VAR_BUNDLE_NUM_CLIENTE = "NumeroCliente";
    private static final String VAR_BUNDLE_TOKEN_ACESSO = "TokenAcesso";
    private static final String VAR_BUNDLE_NUM_REQUERIMENTO = "NumRequerimento";
    private static final String VAR_BUNDLE_NOME = "Nome";
    private static final String VAR_BUNDLE_ENDERECO = "Endereco";
    private static final String VAR_BUNDLE_CPF_CNPJ = "CpfCnpj";

    //Texts da classe
    private static final String TEXT_FAZENDO_LOGIN = "Fazendo login...";
    private static final String TEXT_LOGIN_EFETUADO_SUCESSO = "Login efetuado com sucesso.";
    private static final String TEXT_DADOS_INCORRETOS = "Dados incorretos.Verifique e tente novamente.";
    private static final String TEXT_ERRO_CARREGAR_DADOS_CLIENTE = "Falha ao recuperar os dados do cliente";

    //Declaração da Inicialiação do FireStore
    private FirebaseFirestore mFirestore;

    boolean ctrl = false;

    public String tokenAcesso, numRequerimento, nomeCliente, enderecoCliente;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();

        final Button button = findViewById(R.id.btnEntrar);
        final EditText etCpfCnpj = findViewById(R.id.etCpfCnpj);
        final EditText etNumCliente = findViewById(R.id.etNumCliente);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

            }
        });

        //Inicialização do FireStore
        mFirestore = FirebaseFirestore.getInstance();

        //Botão do Login
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditTexts passando para Strings para serem usadas no Map
                final String cpfCnpj = etCpfCnpj.getText().toString();
                final String numCliente = etNumCliente.getText().toString();

                //Sinaliza para o usuário que está carregando
                progress = new ProgressDialog(MainActivity.this);
                progress.setMessage(TEXT_FAZENDO_LOGIN);
                progress.show();

                //Recuperando os dados
                mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful() ) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if(String.valueOf(document.getData().get(FIELD_CPF_CNPJ)).equals(String.valueOf(etCpfCnpj.getText()))
                                        && String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)).equals(String.valueOf(etNumCliente.getText()))) {

                                    tokenAcesso = document.getData().get(FIELD_TOKEN_ACESSO).toString();
                                    numRequerimento = document.getData().get(FIELD_NUM_REQUERIMENTO).toString();
                                    nomeCliente = document.getData().get(FIELD_NOME_CLIENTE).toString();
                                    enderecoCliente = document.getData().get(FIELD_ENDERECO_CLIENTE).toString();

                                    //Passa os dados para a próxima tela através do Bundle
                                    Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(VAR_BUNDLE_NUM_CLIENTE, numCliente);
                                    bundle.putString(VAR_BUNDLE_CPF_CNPJ, cpfCnpj);
                                    bundle.putString(VAR_BUNDLE_TOKEN_ACESSO, tokenAcesso);
                                    bundle.putString(VAR_BUNDLE_NUM_REQUERIMENTO, numRequerimento);
                                    bundle.putString(VAR_BUNDLE_NOME, nomeCliente);
                                    bundle.putString(VAR_BUNDLE_ENDERECO, enderecoCliente);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    ctrl = true;
                                    return;
                                } else{
                                    ctrl = false;
                                }
                            }
                            //If para o Toast funcionar
                            if(ctrl==true){
                                Toast.makeText(getApplicationContext(), TEXT_LOGIN_EFETUADO_SUCESSO, Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(getApplicationContext(),TEXT_DADOS_INCORRETOS , Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this, TEXT_ERRO_CARREGAR_DADOS_CLIENTE, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
