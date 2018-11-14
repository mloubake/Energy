package caiococaro.com.br.energy;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CadastroClienteVital extends AppCompatActivity {

    private static final String TAG = "";
//    private static final int CAMERA_PIC_REQUEST = "";

    //Keys da Bundle (São os dados (ou valores) da bundle da Activity anterior)
    public static final String KEY_NUM_CLIENTE = "NumeroCliente";
    public static final String KEY_NUM_REQUERIMENTO = "NumRequerimento";

    //Campos do Firebase
    private static final String FIELD_CPF_CNPJ = "cpfCnpj";
    private static final String FIELD_NUM_CLIENTE = "numCliente";

    String nomePaciente, equipamentos, CRM;


    FirebaseFirestore mFirestore;

    Map<String, String> clienteMap;

    //Valores passados entre os bundles
    String valorNumCliente;
    String valorToken;
    String valorNumRequerimento;

    Bundle bundle = new Bundle();

     EditText etNomePaciente;
     TextView tvNumPaciente;
     EditText etCrmMedico;
     EditText etEquipamento;
     Button btnFoto;
     Button btnAtualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente_vital);



        //Recebendo a bundle da MainActivity com vários valores
        bundle = getIntent().getExtras();

        //Setando os valores do bundle da MainActivity para MenuPrincipal para depois setar os bundles das outras Activities
        if(bundle != null ){
            valorNumCliente = bundle.getString(KEY_NUM_CLIENTE);
            Log.d(TAG, "VALORNUMCLIENTE 1: " + valorNumCliente);
            valorNumRequerimento = bundle.getString(KEY_NUM_REQUERIMENTO);
            Log.d(TAG, "MENUPRINCIPALBUNDLES: "+valorNumCliente + " / " + valorToken + " / " + valorNumRequerimento) ;
        }
        Log.d(TAG, "VALORNUMCLIENTE 2: " + valorNumCliente);

          etNomePaciente = findViewById(R.id.etNomePaciente);
          tvNumPaciente = findViewById(R.id.tvNumPaciente);
          etCrmMedico = findViewById(R.id.etCRMMedico);
          etEquipamento = findViewById(R.id.etEquipamento);
          btnFoto = findViewById(R.id.btnFoto);
          btnAtualizar = findViewById(R.id.btnEnviar);

        tvNumPaciente.setText(String.valueOf("Número do cliente: "+valorNumCliente));

        mFirestore = FirebaseFirestore.getInstance();
        //Usar ambos os BDs, linkando numCliente e cpfCnpj de cada banco
        //BD para cliente vital, caso fique estranho, adicionar mais campos no BD usuario

        final String crmMedico = etCrmMedico.getText().toString();

        mFirestore.collection("ClienteVital").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (String.valueOf(valorNumCliente).equals(String.valueOf(document.getData().get("numCliente")))) {

                            usuarioCadastrado();

                        }

                    }
                }
            }

        });

    btnFoto.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        }
    });

//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (requestCode == CAMERA_PIC_REQUEST) {
//                Bitmap image = (Bitmap) data.getExtras().get("data");
//                ImageView imageview = (ImageView) findViewById(R.id.ImageView01); //sets imageview as the bitmap
//                imageview.setImageBitmap(image);
//            }
//        }


    }


    public void Enviar (View view){


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //atualizar

                        criaDocumento();

                        Intent intent = new Intent(CadastroClienteVital.this, MenuPrincipal.class);
                        startActivity(intent);
                    }
                });

        dialogBuilder.setMessage("Solicitação enviada para análise com sucesso!");

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    void criaDocumento(){

        nomePaciente = etNomePaciente.getText().toString();
        equipamentos = etEquipamento.getText().toString();
        CRM = etCrmMedico.getText().toString();

        Map<String, Object> data = new HashMap<>();

        data.put("nomePaciente",nomePaciente);
        data.put("numCliente",valorNumCliente);
        data.put("equipamentos",equipamentos);
        data.put("CRM",CRM);

        mFirestore.collection("ClienteVital").add(data);

    }

    void usuarioCadastrado(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder
                .setMessage("Cadastro de cliente vital já solicitado!")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //atualizar

                        Intent intent = new Intent(CadastroClienteVital.this, MenuPrincipal.class);
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }



}


