package caiococaro.com.br.energy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    FirebaseFirestore mFirestore;

    Map<String, String> clienteMap;

    //Valores passados entre os bundles
    String valorNumCliente;
    String valorToken;
    String valorNumRequerimento;

    Bundle bundle = new Bundle();

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

        EditText etNomePaciente = findViewById(R.id.etNomePaciente);
        EditText etNumPaciente = findViewById(R.id.etNumPaciente);
        final EditText etCrmMedico = findViewById(R.id.etCRMMedico);
        EditText etEquipamento = findViewById(R.id.etEquipamento);
        final Button btnFoto = findViewById(R.id.btnFoto);
        final Button btnAtualizar = findViewById(R.id.btnAtualizar);

        mFirestore = FirebaseFirestore.getInstance();
        //Usar ambos os BDs, linkando numCliente e cpfCnpj de cada banco
        //BD para cliente vital, caso fique estranho, adicionar mais campos no BD usuario

        final String crmMedico = etCrmMedico.getText().toString();

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mFirestore.collection("ClienteVital").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful() ) {
                        for (DocumentSnapshot document : task.getResult()) {
                            if(String.valueOf(document.getData().get(FIELD_CPF_CNPJ)).equals(String.valueOf(valorNumCliente))
                                   /* && String.valueOf(document.getData().get(FIELD_NUM_CLIENTE)).equals(String.valueOf(bundle numcliente.getText()))*/) {
                                Log.d(TAG, "VALORNUMCLIENTE 3: " + valorNumCliente);
                                mFirestore.collection("ClienteVital").document().update("crmMedico", crmMedico);
                            }
                        }
                    }
                }
            });
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
}
