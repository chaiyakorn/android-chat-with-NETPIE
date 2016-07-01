package chat.netpie.netpiechatbeta1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public String nameplayer;
    public EditText etnamechat;
    public Button btstartchat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        bindwegit();
        setevent();
    }
    private void bindwegit() {

        etnamechat=(EditText)findViewById(R.id.et_name);
        btstartchat=(Button)findViewById(R.id.btbegin);
    }

    private void setevent() {

     btstartchat.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (isConnectingToInternet()==true){
                 if(etnamechat.getText().toString().isEmpty()){
                     Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_LONG).show();
                 }else{
                     nameplayer=etnamechat.getText().toString();

                     Intent intent = new Intent(getApplicationContext(), chat.class);

                     intent.putExtra("name",nameplayer);
                     startActivity(intent);
                     finish();
                     Log.i("set name", "name  : " + nameplayer);
                 }
             }else
             {
                 Toast.makeText(getApplicationContext(), "You are offline please connect internet !!!!", Toast.LENGTH_LONG).show();


             }

         }
     });


    }
    public  boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


}
