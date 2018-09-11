package caiococaro.com.br.energy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class MyDialog extends android.support.v4.app.DialogFragment {

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



