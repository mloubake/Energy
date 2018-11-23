package caiococaro.com.br.energy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AcompanhamEquipeTecnica extends AppCompatActivity {

    //Tabela do FireStore
    private static final String TABLE_EQUIPE_MANUTENCAO = "EquipeManutencao";

    //Campos do Firebase
    private static final String FIELD_NUM_REQUERIMENTO = "numRequerimento";
    private static final String FIELD_IS_AVALIADO = "isAvaliado";
    private static final String FIELD_STATUS_PEDIDO = "statusPedido";

    //Variáveis da Bundle
    private static final String VAR_BUNDLE_NUM_CLIENTE = "NumeroCliente";
    private static final String VAR_BUNDLE_NUM_REQUERIMENTO = "NumRequerimento";
    private static final String VAR_BUNDLE_NOME = "Nome";
    private static final String VAR_BUNDLE_ENDERECO= "Endereco";

    //Texts da classe
    private static final String TEXT_ENViAR_AVALIACAO = "Enviar Avaliação";
    private static final String TEXT_FECHAR_POPUP = "Fechar popup";

    //Var para chamada do BD
    FirebaseFirestore mFirestore;

    Bundle bundle = new Bundle();

    String numRequerimento, numCliente, nomeCliente, enderecoCliente;

    int statusPedido;

    boolean isAvaliado;

    TextView txtNumCliente, txtNaoSolicitado;

    LinearLayout layoutStatus, layoutLegenda;

    ImageView img1, img2, img3, img4, img5, img6;
    ArrayList<ImageView> imgArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanham_equipe_tecnica);

        //Bundle
        bundle = getIntent().getExtras();
        if (bundle != null) {
            numRequerimento = bundle.getString(VAR_BUNDLE_NUM_REQUERIMENTO);
            numCliente = bundle.getString(VAR_BUNDLE_NUM_CLIENTE);
            nomeCliente = bundle.getString(VAR_BUNDLE_NOME);
            enderecoCliente = bundle.getString(VAR_BUNDLE_ENDERECO);
        }

        txtNumCliente = findViewById(R.id.txtInfoCliente);

        txtNumCliente.setText("  Usuário: " + nomeCliente
                + "\n  Número do Cliente: " + numCliente
                + "\n  Endereço: " + enderecoCliente
                + "\n  Número do Requerimento: " + numRequerimento);

        //Setando a imagem (Drawable checklist.png) de cada ícone do layout
        img1 = findViewById(R.id.imagem1);
        img2 = findViewById(R.id.imagem2);
        img3 = findViewById(R.id.imagem3);
        img4 = findViewById(R.id.imagem4);
        img5 = findViewById(R.id.imagem5);
        img6 = findViewById(R.id.imagem6);
        imgArrayList.add(img1);
        imgArrayList.add(img2);
        imgArrayList.add(img3);
        imgArrayList.add(img4);
        imgArrayList.add(img5);
        imgArrayList.add(img6);
        txtNaoSolicitado = findViewById(R.id.txt_nao_solicitado);

        layoutStatus = findViewById(R.id.layoutStatus);
        layoutLegenda = findViewById(R.id.layoutLegenda);

        //Chamando o Método do BD Acompanhamento Equipe Técnica
        acompanhamentoBD();
    }

    //BD Acompanhamento Equipe Técnica
    public void acompanhamentoBD(){

        //Instanciando o Firebase
        mFirestore = FirebaseFirestore.getInstance();

        //Habilitando novamente a conexão com o Firebase
        mFirestore.enableNetwork();

        //Utilizando a Coleção EquipeManutenção
        mFirestore.collection(TABLE_EQUIPE_MANUTENCAO).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    if (String.valueOf(document.getData().get(FIELD_NUM_REQUERIMENTO)).equals(String.valueOf(numRequerimento))) {

                        //Recuperando o ID do Documento
                        String myId = document.getId();

                        //Setando o valor do campo "statusPedido"  na variável statusPedido
                        statusPedido = Integer.valueOf(String.valueOf(document.getData().get(FIELD_STATUS_PEDIDO)));

                        //Chamando o método
                        showStatus();

                        //Imprimindo a imagem checklist.png nos ícones do layout
                        if(statusPedido >= 1 && statusPedido <= imgArrayList.size()){
                            tintIcon();

                            //POPUP aparecendo quando statusPedido é igual a 6
                            if(statusPedido == 6){
                                isAvaliado = Boolean.valueOf(String.valueOf(document.getData().get(FIELD_IS_AVALIADO)));
                                if(!isAvaliado){
                                    showPopUp(myId);
                                }
                            }
                        } else{
                            hideStatus();
                        }
                    }
                }
            }
        });
    }

    //Seta os ícones corretamente
    //checklist.png para statusPedido atendido
    //carro.png para statusPedido sendo atendido
    //vazio.png para statusPedido que vai ser atendido
    public void tintIcon(){
        for(ImageView iv : imgArrayList){
            int index = imgArrayList.indexOf(iv);
            if (index < statusPedido) {
                imgArrayList.get(index).setImageResource(R.drawable.cheklist);
            } else if (index == statusPedido) {
                imgArrayList.get(index).setImageResource(R.drawable.carro);
            } else{
                imgArrayList.get(index).setImageResource(R.drawable.vazio);
            }
        }
    }

    //Esconde o layout quando statusPedido não está entre os 6 pedidos disponíveis
    private void hideStatus() {
        layoutStatus.setVisibility(View.GONE);
        layoutLegenda.setVisibility(View.GONE);
        txtNaoSolicitado.setVisibility(View.VISIBLE);
    }

    //Mostra o layout quando statusPedido está entre 6 pedidos disponíveis
    private void showStatus() {
        layoutStatus.setVisibility(View.VISIBLE);
        layoutLegenda.setVisibility(View.VISIBLE);
        txtNaoSolicitado.setVisibility(View.GONE);
    }

    //Mostra o popup depois de todos os pedidos serem atendidos
    public void showPopUp(final String myId){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_popup, null);
        dialogBuilder.setView(dialogView);


        dialogBuilder.setPositiveButton(TEXT_ENViAR_AVALIACAO, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                showPopUpAgradecimento();
                updateAvaliacao(myId);
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    //Popup de agradecimento depois da avaliação ter sido feita
    public void showPopUpAgradecimento(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_popup_agradecimento, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton(TEXT_FECHAR_POPUP, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(AcompanhamEquipeTecnica.this, MenuPrincipal.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    //Muda o campo "isAvaliado" para não ter mais avaliação futuramente
    void updateAvaliacao(final String myId) {
        mFirestore.collection(TABLE_EQUIPE_MANUTENCAO).document(String.valueOf(myId)).update(FIELD_IS_AVALIADO, true);
    }

}

