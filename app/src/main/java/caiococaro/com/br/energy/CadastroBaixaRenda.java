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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CadastroBaixaRenda extends AppCompatActivity {

    private static final String TABLE_USUARIO = "Usuario";
    private FirebaseFirestore mFirestore;

    String nome = "", end = "", numCliente = "456";

    Bundle bundle = new Bundle();

   /* bundle = getIntent().getExtras();

        if (bundle != null) {
        numCliente = bundle.getString(NAME_CONSUMO);

    }*/

    EditText etNIS = null;
    EditText etNumCliente = null;

    TextView tvNomeCliente = null;
    TextView tvEndCliente = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_baixa_renda);

        etNIS = (EditText) findViewById(R.id.etNIS);
        etNumCliente = (EditText) findViewById(R.id.etNumCliente);
        tvNomeCliente = (TextView) findViewById(R.id.tvnomeCliente);
        tvEndCliente= (TextView) findViewById(R.id.tvendCliente);

        mFirestore = FirebaseFirestore.getInstance();

        pesquisa();

        /*etNumCliente.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvNomeCliente.setText("Eu");
                tvEndCliente.setText("Rua alguma");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/


    }

        public void Enviar (View view){


            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

            dialogBuilder
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //atualizar

                            Intent intent = new Intent(CadastroBaixaRenda.this, MenuPrincipal.class);
                            startActivity(intent);
                        }
                    });

            dialogBuilder.setMessage("Solicitação enviada para análise com sucesso!");

            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

        }

        public void pesquisa(){

            mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {

                            if (String.valueOf(numCliente).equals(String.valueOf(document.getData().get("numCliente")))) {
                                nome = String.valueOf(document.getData().get("nome"));
                                end = String.valueOf(document.getData().get("endereco"));

                                Toast.makeText(getApplicationContext(),""+nome,Toast.LENGTH_LONG).show();

                                if (nome != "" && end != "") {

                                    tvEndCliente.setText(String.valueOf(end));
                                    tvNomeCliente.setText(String.valueOf(nome));
                                }
                            }

                        }
                    }
                }
            });

        }
}

