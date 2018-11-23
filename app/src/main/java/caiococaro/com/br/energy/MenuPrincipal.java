package caiococaro.com.br.energy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    //TAG do Log do Console
    private static final String TAG = "";

    //Keys da Bundle (São os dados (ou valores) da bundle da Activity anterior)
    //(Está passando o dado numCliente para essa tela)
    private static final String VAR_BUNDLE_NUM_CLIENTE = "NumeroCliente";
    private static final String VAR_BUNDLE_CPF_CNPJ = "CpfCnpj";
    private static final String VAR_BUNDLE_NUM_REQUERIMENTO = "NumRequerimento";
    private static final String VAR_BUNDLE_NOME= "Nome";
    private static final String VAR_BUNDLE_ENDERECO = "Endereco";
    private static final String VAR_BUNDLE_TOKEN_ACESSO = "TokenAcesso";

    //Valores passados entre os bundles
    String valorNumCliente, valorToken, valorNumRequerimento,
            valorNome, valorEndereco, valorCpfCnpj;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        //Recebendo a bundle da MainActivity com vários valores
        bundle = getIntent().getExtras();

        //Setando os valores do bundle da MainActivity para MenuPrincipal para depois setar os bundles das outras Activities
        if(bundle != null ){
            valorNumCliente = bundle.getString(VAR_BUNDLE_NUM_CLIENTE);
            valorToken = bundle.getString(VAR_BUNDLE_TOKEN_ACESSO);
            valorNumRequerimento = bundle.getString(VAR_BUNDLE_NUM_REQUERIMENTO);
            valorNome = bundle.getString(VAR_BUNDLE_NOME);
            valorCpfCnpj = bundle.getString(VAR_BUNDLE_CPF_CNPJ);
            valorEndereco = bundle.getString(VAR_BUNDLE_ENDERECO);
        }

        Button btnConsumo = findViewById(R.id.btnConsumo);
        final Button btnAcompanhamento = findViewById(R.id.btnAcompanhamento);
        final Button btnCadastroVital = findViewById(R.id.btnCadastroVital);
        Button btnCadastroBaixaRenda = findViewById(R.id.btnCadastroBaixaRenda);

        //Repassa alguns dados para a Activity ConsumoTempoReal
        btnConsumo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intentConsumo = new Intent(MenuPrincipal.this, ConsumoTempoReal.class);

              //Enviando bundle para ConsumoTempoReal
              Bundle bundleConsumo = new Bundle();
              bundleConsumo.putString(VAR_BUNDLE_NUM_CLIENTE, valorNumCliente);
              intentConsumo.putExtras(bundleConsumo);
              startActivity(intentConsumo);
          }
      });

        //Repassa alguns dados para a Activity AcompanhamEquipeTecnica
        btnAcompanhamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAcompanhamento= new Intent(MenuPrincipal.this, AcompanhamEquipeTecnica.class);

                //Enviando bundle para AcompanhamentoEquipeTecnica
                Bundle bundleAcompanhamento = new Bundle();
                bundleAcompanhamento.putString(VAR_BUNDLE_NUM_REQUERIMENTO, valorNumRequerimento);
                bundleAcompanhamento.putString(VAR_BUNDLE_NUM_CLIENTE, valorNumCliente);
                bundleAcompanhamento.putString(VAR_BUNDLE_NOME, valorNome);
                bundleAcompanhamento.putString(VAR_BUNDLE_ENDERECO, valorEndereco);
                intentAcompanhamento.putExtras( bundleAcompanhamento);
                startActivity(intentAcompanhamento);
            }
        });

        //Repassa alguns dados para a Activity CadastroClienteVital
        btnCadastroVital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentClienteVital = new Intent(MenuPrincipal.this, CadastroClienteVital.class);

                //Enviando bundle para CadastroClienteVital
                Bundle bundleClienteVital = new Bundle();
                bundleClienteVital.putString(VAR_BUNDLE_NUM_CLIENTE, valorNumCliente);
                bundleClienteVital.putString(VAR_BUNDLE_NUM_REQUERIMENTO, valorNumRequerimento);
                bundleClienteVital.putString(VAR_BUNDLE_CPF_CNPJ, valorCpfCnpj);
                intentClienteVital.putExtras( bundleClienteVital);
                startActivity(intentClienteVital);
            }
        });

        //Repassa alguns dados para a Activity CadastroBaixaRenda
        btnCadastroBaixaRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBaixaRenda= new Intent(MenuPrincipal.this, CadastroBaixaRenda.class);

                //Enviando bundle para CadastroBaixaRenda
                Bundle bundleBaixaRenda = new Bundle();
                bundleBaixaRenda.putString(VAR_BUNDLE_NUM_CLIENTE, valorNumCliente);
                bundleBaixaRenda.putString(VAR_BUNDLE_NOME, valorNome);
                bundleBaixaRenda.putString(VAR_BUNDLE_ENDERECO, valorEndereco);
                bundleBaixaRenda.putString(VAR_BUNDLE_CPF_CNPJ, valorCpfCnpj);
                intentBaixaRenda.putExtras(bundleBaixaRenda);
                startActivity(intentBaixaRenda);
            }
        });
    }
}

