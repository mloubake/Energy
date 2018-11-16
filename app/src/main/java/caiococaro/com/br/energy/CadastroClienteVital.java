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

    //Tabelas do FireStore
    private static final String TABLE_USUARIO = "Usuario";
    private static final String TABLE_CLIENTE_VITAL = "ClienteVital";

    //Keys da Bundle (São os dados (ou valores) da bundle da Activity anterior)
    public static final String KEY_NUM_CLIENTE = "NumeroCliente";
    public static final String KEY_CPFCNPJ = "cpfCnpj";
    public static final String KEY_NUM_REQUERIMENTO = "NumRequerimento";

    //Campos do Firebase
    private static final String FIELD_CPF_CNPJ = "cpfCnpj";
    private static final String FIELD_CRM_MEDICO = "crmMedico";
    private static final String FIELD_EQUIPAMENTO = "equipamento";
    private static final String FIELD_IS_CLIENTE_VITAL_CADASTRADO = "isClienteVitalCadastrado";
    private static final String FIELD_NOME_PACIENTE = "nomePaciente";
    private static final String FIELD_NUM_CLIENTE = "numCliente";
    private static final String FIELD_NUM_PACIENTE = "numPaciente";

    String nomePaciente, equipamentos, CRM;



    FirebaseFirestore mFirestore;

    //Valores passados entre os bundles
    String valorNumCliente;
    String valorCpfCnpj;
    String valorToken;
    String valorNumRequerimento;

    Bundle bundle = new Bundle();

    EditText etNomePaciente;
    TextView tvNumPaciente;
    EditText etCrmMedico;
    EditText etEquipamento;
    Button btnFoto;
    Button btnAtualizar;

    String myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente_vital);



        //Recebendo a bundle da MainActivity com vários valores
        bundle = getIntent().getExtras();

        //Setando os valores do bundle da MainActivity para MenuPrincipal para depois setar os bundles das outras Activities
        if(bundle != null ){
            valorNumCliente = bundle.getString(KEY_NUM_CLIENTE);
            valorCpfCnpj = bundle.getString(KEY_CPFCNPJ);
            valorNumRequerimento = bundle.getString(KEY_NUM_REQUERIMENTO);
            Log.d(TAG, "VALORNUMCLIENTE 1: " + valorNumCliente);
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

//        mFirestore.collection(TABLE_CLIENTE_VITAL).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, "myID myID: "+ myId);
//                        if (String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)).equals(valorNumCliente)) {
//                            if(document.getData().get(FIELD_IS_CLIENTE_VITAL_CADASTRADO).equals(true)) {
//                                usuarioCadastrado();
//                            }
//                        return;
//                        }
//                    }
//                }
//            }
//        });

        mFirestore.collection(TABLE_USUARIO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        if(String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)).equals(valorNumCliente)){
                            if(document.getData().get(FIELD_IS_CLIENTE_VITAL_CADASTRADO).equals(true)){
                                usuarioCadastrado();
                            } else{
                                //Procura se o documento existe e tem aquele número dentro desse documento
                                mFirestore.collection(TABLE_CLIENTE_VITAL).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for (DocumentSnapshot document : task.getResult()){
                                                if(document.exists() &&
                                                        String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)).equals(valorNumCliente)){
                                                    aguardandoAnalise();
                                                    etNomePaciente.setText( String.valueOf(document.getData().get(FIELD_NOME_PACIENTE)));
                                                    etEquipamento.setText( String.valueOf(document.getData().get(FIELD_EQUIPAMENTO)));
                                                    etCrmMedico.setText( String.valueOf(document.getData().get(FIELD_CRM_MEDICO)));
                                                }
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


        //Todo: Verificar a câmera nos despositivos
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
                        criarDocumento(myId);

                        Intent intent = new Intent(CadastroClienteVital.this, MenuPrincipal.class);
                        startActivity(intent);
                    }
                });

        dialogBuilder.setMessage("Solicitação enviada para análise com sucesso!");

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    void criarDocumento(String myId){

        nomePaciente = etNomePaciente.getText().toString();
        equipamentos = etEquipamento.getText().toString();
        CRM = etCrmMedico.getText().toString();

        Map<String, Object> data = new HashMap<>();

        data.put(FIELD_NOME_PACIENTE,nomePaciente);
        data.put(FIELD_NUM_CLIENTE,valorNumCliente);
        data.put(FIELD_EQUIPAMENTO,equipamentos);
        data.put(FIELD_CRM_MEDICO,CRM);
        //Campos que vão ser criados no BD para não ter trabalho extra nas futuras manutenções
        data.put(FIELD_IS_CLIENTE_VITAL_CADASTRADO, false);
        data.put(FIELD_NUM_PACIENTE, null);
        data.put(FIELD_CPF_CNPJ, valorCpfCnpj);

        mFirestore.collection(TABLE_CLIENTE_VITAL).add(data);
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

    public void aguardandoAnalise (){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        dialogBuilder.setMessage("Análise em andamento...");

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
}


