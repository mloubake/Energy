package caiococaro.com.br.energy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CadastroClienteVital extends AppCompatActivity {

//    private static final int CAMERA_PIC_REQUEST = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente_vital);

        EditText etNomePaciente = findViewById(R.id.etNomePaciente);
        EditText etNumPaciente = findViewById(R.id.etNumPaciente);
        EditText etCrmMedico = findViewById(R.id.etCRMMedico);
        final Button btnFoto = findViewById(R.id.btnFoto);


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
