package chat.netpie.netpiechatbeta1;

import android.app.VoiceInteractor;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by himanshusoni on 06/09/15.
 */
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private static final int MY_MESSAGE = 0, OTHER_MESSAGE = 1, online = 2, offline = 3;
    String sander,reiver;
    chat c=new chat();

    public ChatMessageAdapter(Context context, List<ChatMessage> data) {
        super(context, R.layout.activity_sander, data);

    }

    @Override
    public int getViewTypeCount() {
        // my message, other message, my image, other image
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage item = getItem(position);

        if (item.isMine() && !item.isImage()) return MY_MESSAGE;
        else if (!item.isMine() && !item.isImage()) return OTHER_MESSAGE;
        else if (item.isMine() && item.isImage()) return online;
        else return offline;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == MY_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_sander, parent, false);

            TextView textView = (TextView) convertView.findViewById(R.id.sandertext);
            TextView name =(TextView)convertView.findViewById(R.id.namesand);
            name.setText(c.name);    //setnamesander
            textView.setText(getItem(position).getContent());

        } else if (viewType == OTHER_MESSAGE) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_reciver, parent, false);
            TextView name =(TextView)convertView.findViewById(R.id.namerecive);
             name.setText(c.namerecive);    //setnamesander
            TextView textView = (TextView) convertView.findViewById(R.id.recivetext);
            textView.setText(getItem(position).getContent());
        }
        else if (viewType == online) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_staust, parent, false);
            TextView name =(TextView)convertView.findViewById(R.id.statustext);
            name.setText(getItem(position).getContent()+" is online");
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_staust, parent, false);
            TextView name =(TextView)convertView.findViewById(R.id.statustext);
            name.setText(getItem(position).getContent()+"is offline");
        }
        /*
        convertView.findViewById(R.id.chatMessageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "onClick", Toast.LENGTH_LONG).show();
            }
        });
*/



        return convertView;
    }



}
