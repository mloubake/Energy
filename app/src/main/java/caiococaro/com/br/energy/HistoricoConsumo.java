package caiococaro.com.br.energy;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class HistoricoConsumo extends AppCompatActivity {

    boolean ctrl;
    int dia, mes, ano;


    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_consumo);

        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection("EquipeManutencao").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() ) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if( (String.valueOf(document.getData().get("cpfCnpj"))).equals(0)) {
                            dia = (int) document.getData().get("dia");
                            mes = (int) document.getData().get("mes");
                            ano = (int) document.getData().get("ano");
                            break;
                        }
                        else{
                            ctrl = false;
                        }
                    }
                    if(ctrl==true){
                        //Toast.makeText(getApplicationContext(), "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //Toast.makeText(getApplicationContext(),"Dados incorretos.Verifique e tente novamente." , Toast.LENGTH_LONG).show();
                    }
                }
                else{
                }
            }
        });





        Calendar c = new GregorianCalendar();


        final ListView listView = (ListView) findViewById(R.id.idLista);

        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(dia);
        arrayList.add(mes);
        arrayList.add(ano);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);
    }
}
