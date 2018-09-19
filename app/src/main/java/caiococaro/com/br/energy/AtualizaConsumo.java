package caiococaro.com.br.energy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AtualizaConsumo extends Dialog {

    private EditText editTextNome;

    public AtualizaConsumo(View.OnClickListener context) {
        super((Context) context);
    }

    private static final String NAME_CONSUMO_ATUAL = "ConsumoAtual";

    private String leitura_atualizada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_atualizar);
        editTextNome = (EditText) findViewById(R.id.leituraAtualMedidor);

        Button buttonAtualizar = (Button) findViewById(R.id.btnAtualizarConsumo);
        buttonAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), editTextNome.getText().toString(), Toast.LENGTH_SHORT).show();

                leitura_atualizada = editTextNome.getText().toString();

                /*Iniciar activity MenuPrincipal com Bud
                Intent intent = new Intent(AtualizaConsumo.this, ConsumoTempoReal.class);
                Bundle bundle = new Bundle();
                bundle.putString(NAME_CONSUMO_ATUAL, leitura_atualizada);
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });
    }
}


