package chat.netpie.netpiechatbeta1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import netpie.io.netpiegear.EventListener;
import netpie.io.netpiegear.Microgear;

public class chat extends AppCompatActivity {

    public static String name;
    private Handler handler;
    private Button btsend, btlogout;
    private EditText etmessage;
    private ListView lvmessage;
    public static String namerecive;

    String inputname;
    String inputmessage;
    String appid = "chatnetpie"; //APP_ID
    String key = "VQyqs1UOkUxNUDk"; //KEY
    String secret = "rFSF4QqSBOsxaTuvxiWVUsq7q"; //SECRET
    private ChatMessageAdapter mAdapter;
    String[] recivemessage;

    Microgear microgear = new Microgear(chat.this);
    EventListener eventListener = new EventListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setname();//frist only  set my name
        bindwegit();
        setevent();
        microgear=new Microgear(getApplicationContext());
        microgear.connect(appid, key, secret);
       microgear.subscribe("/chat");
        Log.i("Connected", "Now I'm connected chat");
        microgear.publish("/chat", "I'm new#" + name);
        Log.i("Connected", "Now I'm connected chat on " + name);
        microgear.publish("/chat", "Hi#" + name);
        Log.i("Connected", "Now I'm connected hi on " + name);

        Log.i("Connected", "Context service : " + getApplicationContext());


        mAdapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        lvmessage.setAdapter(mAdapter);


    }
    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private void setname() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            name = bundle.getString("name");
            Toast.makeText(this, " name is " + name, Toast.LENGTH_LONG).show();
            Log.i("set name", "name  : " + name);
        } else {
            Toast.makeText(this, " name is null", Toast.LENGTH_LONG).show();
            Log.i("set name", "name not set ");
        }
    }

    private void setevent() {
        eventListener.setConnectEventListener(new EventListener.OnServiceConnect() {
            @Override
            public void onConnect(Boolean status) {
                if (status == true) {
                    Message msg = handler2.obtainMessage();
                    Log.i("Connected", "Now I'm connected with NETPIE");
                    // Main2Activity.np_status.setText(" Now I'm connected with NETPIE");
                } else {
                    Log.i("NotConnect", "Can't connect to NETPIE");
                }
            }

        });

        eventListener.setMessageEventListener(new EventListener.OnMessageReceived() {
            @Override
            public void onMessage(final String topic, final String message) {
                Message msg = handler2.obtainMessage();
                try {
                    recivemessage = message.toString().split("#");
                    String status = recivemessage[0].toString();
                    inputname = recivemessage[1].toString();
                    inputmessage = recivemessage[2].toString();
                    if (!inputname.equals(name)) {
                        onnotification(inputname, inputmessage);
                    }
                    if (status.equals("Hey")) {
                        onlineMessage(inputname);
                    }
                    if (status.equals("bay")) {
                        offlineMessage(inputname);
                    }
                    Log.i(" befor ", recivemessage[1].toString());
                    if (!inputname.equals(name)) {
                        Log.i(" after ", recivemessage[1].toString());

                        namerecive = recivemessage[1].toString();

                        mimicOtherMessage(recivemessage[2].toString());


                    }


                } catch (Exception e) {

                }
                Log.i("Message", topic + " : " + message);
            }
        });

        eventListener.setPresentEventListener(new EventListener.OnPresent() {
            @Override
            public void onPresent(String name) {
                Message msg = handler2.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("myKey", "New friend Connect :" + name);
                msg.setData(bundle);
                handler.sendMessage(msg);
                Log.i("present", "New friend Connect :" + name);
            }
        });

        eventListener.setAbsentEventListener(new EventListener.OnAbsent() {
            @Override
            public void onAbsent(String name) {
                Message msg = handler2.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("myKey", "Friend lost :" + name);
                msg.setData(bundle);
                handler.sendMessage(msg);
                Log.i("absent", "Friend lost :" + name);
            }
        });

        eventListener.setDisconnectEventListener(new EventListener.OnClose() {
            @Override
            public void onDisconnect(Boolean status) {
                Message msg = handler2.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("myKey", "Disconnected");
                msg.setData(bundle);
                microgear.publish("/chat", "bay#" + name);
                Log.i("disconnect", "Disconnected");
            }
        });

        eventListener.setOnException(new EventListener.OnException() {
            @Override
            public void onException(String error) {
                Message msg = handler2.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("myKey", "Exception : " + error);
                msg.setData(bundle);
                // handler.sendMessage(msg);
                Log.i("exception", "Exception : " + error);
            }
        });

        btsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etmessage.getText().toString().isEmpty()) {
                    return;
                } else {
                    sendMessage(etmessage.getText().toString());
                    etmessage.setText("");
                }
            }
        });

        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microgear.publish("/chat", "bye#" + name);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

                Log.i("Disconnect", "logout");
            }
        });


    }


    private void bindwegit() {
        etmessage = (EditText) findViewById(R.id.et_message);
        btsend = (Button) findViewById(R.id.btn_send);
        btlogout = (Button) findViewById(R.id.btn_logout);
        lvmessage = (ListView) findViewById(R.id.listView);
    }

    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");

        }
    };

    private void sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
        microgear.publish("/chat", "msg#" + name + "#" + message);


    }

    private void onnotification(String name, String message) {
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n = new Notification.Builder(getApplicationContext())

                .setContentTitle(name)
                .setContentText(message)
                .setSmallIcon(R.drawable.netpielogo)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();// ต้องการ api16
        NotificationManager notificationManagera =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
        Log.d("notification", " name  is  " + name);
        Log.d("notification", " message is  " + message);


    }

    private void mimicOtherMessage(String messagerecive) {
        ChatMessage chatMessage = new ChatMessage(messagerecive, false, false);
        mAdapter.add(chatMessage);


    }

    private void onlineMessage(String messagerecive) {
        ChatMessage chatMessage = new ChatMessage(messagerecive, true, true);
        mAdapter.add(chatMessage);

    }

    private void offlineMessage(String messagerecive) {
        ChatMessage chatMessage = new ChatMessage(messagerecive, false, true);
        mAdapter.add(chatMessage);

    }


}
