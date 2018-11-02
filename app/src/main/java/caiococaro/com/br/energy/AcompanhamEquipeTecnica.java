package caiococaro.com.br.energy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class AcompanhamEquipeTecnica extends AppCompatActivity {

    //TAG do Log do Console
    private static final String TAG = "";

    //Tabelas do FireStore
    private static final String TABLE_USUARIO = "Usuario";
    private static final String TABLE_EQUIPE_MANUTENCAO = "EquipeManutencao";

    //Campos do Firebase
    private static final String FIELD_NUM_REQUERIMENTO = "numRequerimento";
    private static final String FIELD_TOKEN_ACESSO = "tokenAcesso";

    //Keys da Bundle
    private static final String KEY_NUM_CLIENTE = "NumeroCliente";
    private static final String KEY_TOKEN_ACESSO = "TokenAcesso";
    private static final String KEY_NUM_REQUERIMENTO = "NumRequerimento";

    //Names para cada Bundle Específica
    private static final String NAME_ACOMPANHAMENTO = "Acompanhamento";
    private static final String CHANNEL_ID = "ID Pessoal";


    FirebaseFirestore mFirestore;

    Bundle bundle = new Bundle();
    String numRequerimento;


    int statusPedido;
    double porcStatusPedido;
    ProgressBar mProgresBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acompanham_equipe_tecnica);


        /*
        MessageReceiver receiver = new MessageReceiver(new Message());

        Intent intent = new Intent(AcompanhamEquipeTecnica.this, TimerService.class);
        intent.putExtra("time", 10);
        intent.putExtra("receiver", receiver);
        startService(intent);
        */

        bundle = getIntent().getExtras();
        if (bundle != null) {
            numRequerimento = bundle.getString(NAME_ACOMPANHAMENTO);
            Log.d("", "TOKENACESSO: " + numRequerimento);
        }

        final ImageView img0 = findViewById(R.id.imagem0);
        final ImageView img1 = findViewById(R.id.imagem1);
        final ImageView img2 = findViewById(R.id.imagem2);
        final ImageView img3 = findViewById(R.id.imagem3);
        final ImageView img4 = findViewById(R.id.imagem4);
        final ImageView img5 = findViewById(R.id.imagem5);
        final ImageView img6 = findViewById(R.id.imagem6);


        /*
        public class Message{
            public void displayMessage(int resultCode, Bundle resultData){
                String message = resultData.getString("message");
                Toast.makeText(AcompanhamEquipeTecnica.this, resultCode + " " + message, Toast.LENGTH_SHORT).show();
            }
        }
        */

        //boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("ALARM_DISPARADO"), PendingIntent.FLAG_NO_CREATE) == null);




        //acompanhamentoBD(img0, img1, img2, img3, img4, img5, img6);




    }

/*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ScheduleJob(View v){
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "ScheduleJob:");
        } else {
            Log.d(TAG, "ScheduleJob: Failed");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }

*/

    public PendingIntent acompanhamentoBD(final ImageView img0, final ImageView img1, final ImageView img2,
                                          final ImageView img3, final ImageView img4, final ImageView img5,
                                          final ImageView img6){
        mFirestore = FirebaseFirestore.getInstance();

        mFirestore.collection(TABLE_EQUIPE_MANUTENCAO).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete (@NonNull Task < QuerySnapshot > task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if (String.valueOf(document.getData().get(FIELD_NUM_REQUERIMENTO)).equals(String.valueOf(numRequerimento))) {
                            Log.d("", "STATUS-PEDIDO: " + String.valueOf(document.getData().get("statusPedido")));
                            statusPedido = Integer.valueOf(String.valueOf(document.getData().get("statusPedido")));
                            switch (statusPedido) {
                                case 0:
                                    Log.d("", "***STATUS-PEDIDO: " + statusPedido);
                                    porcStatusPedido = 0;
                                    img0.setImageResource(R.drawable.yellow_circle_icon);
                                    break;
                                case 1:
                                    Log.d("", "***STATUS-PEDIDO: " + statusPedido);
                                    porcStatusPedido = 16.666666667;
                                    img0.setImageResource(R.drawable.yellow_circle_icon);
                                    img1.setImageResource(R.drawable.yellow_circle_icon);
                                    sendNotification(AcompanhamEquipeTecnica.this);
                                    break;
                                case 2:
                                    Log.d("", "***STATUS-PEDIDO: " + statusPedido);
                                    porcStatusPedido = 33.333333333;
                                    img0.setImageResource(R.drawable.yellow_circle_icon);
                                    img1.setImageResource(R.drawable.yellow_circle_icon);
                                    img2.setImageResource(R.drawable.yellow_circle_icon);
                                    sendNotification(AcompanhamEquipeTecnica.this);
                                    break;
                                case 3:
                                    Log.d("", "***STATUS-PEDIDO: " + statusPedido);
                                    porcStatusPedido = 50;
                                    img0.setImageResource(R.drawable.yellow_circle_icon);
                                    img1.setImageResource(R.drawable.yellow_circle_icon);
                                    img2.setImageResource(R.drawable.yellow_circle_icon);
                                    img3.setImageResource(R.drawable.yellow_circle_icon);
                                    sendNotification(AcompanhamEquipeTecnica.this);
                                    break;
                                case 4:
                                    Log.d("", "***STATUS-PEDIDO: " + statusPedido);
                                    porcStatusPedido = 66.666666667;
                                    img0.setImageResource(R.drawable.yellow_circle_icon);
                                    img1.setImageResource(R.drawable.yellow_circle_icon);
                                    img2.setImageResource(R.drawable.yellow_circle_icon);
                                    img3.setImageResource(R.drawable.yellow_circle_icon);
                                    img4.setImageResource(R.drawable.yellow_circle_icon);
                                    sendNotification(AcompanhamEquipeTecnica.this);
                                    break;
                                case 5:
                                    Log.d("", "***STATUS-PEDIDO: " + statusPedido);
                                    porcStatusPedido = 83.333333333;
                                    img0.setImageResource(R.drawable.yellow_circle_icon);
                                    img1.setImageResource(R.drawable.yellow_circle_icon);
                                    img2.setImageResource(R.drawable.yellow_circle_icon);
                                    img3.setImageResource(R.drawable.yellow_circle_icon);
                                    img4.setImageResource(R.drawable.yellow_circle_icon);
                                    img5.setImageResource(R.drawable.yellow_circle_icon);
                                    sendNotification(AcompanhamEquipeTecnica.this);
                                    break;
                                case 6:
                                    Log.d("", "***STATUS-PEDIDO: " + statusPedido);
                                    porcStatusPedido = 100;
                                    img0.setImageResource(R.drawable.yellow_circle_icon);
                                    img1.setImageResource(R.drawable.yellow_circle_icon);
                                    img2.setImageResource(R.drawable.yellow_circle_icon);
                                    img3.setImageResource(R.drawable.yellow_circle_icon);
                                    img4.setImageResource(R.drawable.yellow_circle_icon);
                                    img5.setImageResource(R.drawable.yellow_circle_icon);
                                    img6.setImageResource(R.drawable.yellow_circle_icon);
                                    sendNotification(AcompanhamEquipeTecnica.this);
                                    break;
                                default:

                                    break;
                            }
                        }


                    }

                    mProgresBar = findViewById(R.id.progress);
                    mProgresBar.setProgress((int) porcStatusPedido);


                }
            }

        });
        return null;
    }



    public void sendNotification(AcompanhamEquipeTecnica view){
        //Get an instance of a NotificationManager
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID );
        mBuilder.setSmallIcon(R.drawable.yellow_circle_icon);
        mBuilder.setContentTitle("Notificação Energy");
        mBuilder.setContentText("O Status do seu agendamento mudou");
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.build();

        //Gets an instance of the NotificationManager service
        NotificationManagerCompat mNotificationManagerCompat = NotificationManagerCompat.from(this);
        mNotificationManagerCompat.notify(001, mBuilder.build());
    }
}




