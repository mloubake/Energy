package caiococaro.com.br.energy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        /*Button btnConsumo = (Button) findViewById(R.id.btnConsumo);
        /*Button btnHistConsumo = (Button) findViewById(R.id.btnHistConsumo);
        Button btnReclamacao = (Button) findViewById(R.id.btnReclamacao);
        Button btnAcompanhamento = (Button) findViewById(R.id.btnAcompanhamento);
        Button btnCadastroVital = (Button) findViewById(R.id.btnCadastroVital);
        Button btnCadastroBaixaRenda = (Button) findViewById(R.id.btnCadastroBaixaRenda);*/

            //Button[] btnList = {btnConsumo};
            ListView listView = (ListView) findViewById(R.id.lista);

            String[] arrayFuncionalidades = new String[]{"Consumo Tempo Real", "Histórico de Consumo", "Reclamações Especiais",
                                                "Acompanhamento de Manutenção da Equipe Técnica",
                                                "Cadastro de Acompanhamento de Cliente Vital", "Cadastro de Baixa Renda"};

            //ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listString);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.activity_list_item, android.R.id.text1, arrayFuncionalidades);

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0){
                        Intent intent = new Intent(view.getContext(), ConsumoTempoReal.class);
                        startActivity(intent);
                    }
                    if(position == 1){
                        Intent intent = new Intent(view.getContext(), HistoricoConsumo.class);
                        startActivity(intent);
                    }
                    if(position == 2){
                        Intent intent = new Intent(view.getContext(), ReclamacaoEspecial.class);
                        startActivity(intent);
                    }
                    if(position == 3){
                        Intent intent = new Intent(view.getContext(), AcompanhamEquipeTecnica.class);
                        startActivity(intent);
                    }
                    if(position == 4){
                        Intent intent = new Intent(view.getContext(), CadastroClienteVital.class);
                        startActivity(intent);
                    }
                    if(position == 5){
                        Intent intent = new Intent(view.getContext(), CadastroBaixaRenda.class);
                        startActivity(intent);
                    }
                }*/
            });

    }
}
