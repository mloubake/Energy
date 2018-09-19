package caiococaro.com.br.energy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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


public class AtualizaConsumo extends android.support.v4.app.DialogFragment {
    private FirebaseFirestore db;
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
                    Dialog leitura = AtualizaConsumo.this.getDialog();
                    Log.d(TAG, "Valor do editText: " + leitura);
                         /*
                        db = FirebaseFirestore.getInstance();
                         DocumentReference update = db.collection("Usuario").document("padrão");
                         String leitura = leituraAtualMedidor.getText().toString();
                         update
                                .update("leituraAtual", leitura);*/
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