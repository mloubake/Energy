package caiococaro.com.br.energy;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroBaixaRenda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_baixa_renda);
    }

    public void Enviar(View view) {

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
}
