package caiococaro.com.br.energy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //Button btnConsumo = (Button) findViewById(R.id.btnConsumo);
        //Button btnHistConsumo = (Button) findViewById(R.id.btnHistConsumo);
        //Button btnReclamacao = (Button) findViewById(R.id.btnReclamacao);
        //Button btnAcompanhamento = (Button) findViewById(R.id.btnAcompanhamento);
        //Button btnCadastroVital = (Button) findViewById(R.id.btnCadastroVital);
        //Button btnCadastroBaixaRenda = (Button) findViewById(R.id.btnCadastroBaixaRenda);
        Button btnConsumo = (Button) findViewById(R.id.btnConsumo);
        Button btnHistConsumo = (Button) findViewById(R.id.btnHistConsumo);
        Button btnReclamacao = (Button) findViewById(R.id.btnReclamacao);
        Button btnAcompanhamento = (Button) findViewById(R.id.btnAcompanhamento);
        Button btnCadastroVital = (Button) findViewById(R.id.btnCadastroVital);
        Button btnCadastroBaixaRenda = (Button) findViewById(R.id.btnCadastroBaixaRenda);


        btnConsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, ConsumoTempoReal.class);
                startActivity(intent);
            }
        });

        btnHistConsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, HistoricoConsumo.class);
                startActivity(intent);
            }
        });

        btnReclamacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, ReclamacaoEspecial.class);
                startActivity(intent);
            }
        });

        btnAcompanhamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, AcompanhamEquipeTecnica.class);
                startActivity(intent);
            }
        });

        btnCadastroVital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, CadastroClienteVital.class);
                startActivity(intent);
            }
        });

        btnCadastroBaixaRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, CadastroBaixaRenda.class);
                startActivity(intent);
            }
        });
    }
}

