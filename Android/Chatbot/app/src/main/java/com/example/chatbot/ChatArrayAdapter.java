package com.example.chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ChatArrayAdapter extends ArrayAdapter<ResponseMessage> {

    private TextView chatText;
    private List<ResponseMessage> messageList = new ArrayList<ResponseMessage>(); // stores number of messages
    private Context context;

    @Override
    public void add(ResponseMessage object) {
        messageList.add(object);
        super.add(object);
    }

    // Constructor - take lite
    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    // returns total number of messages
    public int getCount() {
        return this.messageList.size();
    }

    // returns message at an index
    public ResponseMessage getItem(int index) {
        return this.messageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ResponseMessage messageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (messageObj.isMe()) {
            row = inflater.inflate(R.layout.item_send, parent, false);
        } else {
            row = inflater.inflate(R.layout.item_receive, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(messageObj.getTextMessage());
        return row;
    }
}
