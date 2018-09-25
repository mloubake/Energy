package caiococaro.com.br.energy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

import caiococaro.com.br.energy.R;

import static android.support.constraint.Constraints.TAG;

public class AtualizaConsumo extends android.support.v4.app.DialogFragment{

    private static final String LEITURA = "Leitura";

    //TAG do Log do Console
    private static final String TAG = "";


    //final EditText leituraAtualMedidor = (EditText) view.findViewById(R.id.leituraAtualMedidor);

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_atualizar, null));

        builder
                .setPositiveButton("Atualizar",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //Atualizar
                        String leituraAtual = leituraAtualMedidor.getText().toString();

                        Intent it = new Intent(AtualizaConsumo.this.getContext(), ConsumoTempoReal.class);
                        it.putExtra("leituraAtualizada", String.valueOf(leituraAtual));
                        startActivity(it);
                    /*Intent intent = new Intent(AtualizaConsumo.this, ConsumoTempoReal.class);
                    /Bundle bundle = new Bundle();
                    bundle.putString(LEITURA, String.valueOf(leitura));
                    intent.putExtras(bundle);
                    startActivity(intent);*/
                    }
            });
        builder
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //cancelar atualização
                    }
                });

        return builder.create();
    }
}