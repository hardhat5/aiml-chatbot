package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String message;
    ListView list;
    EditText messageText;
    Button submitButton;
    TextView reply;
    String url = "http://192.168.1.7:5000/";

    HashMap data = new HashMap();

    RequestQueue mRequestQueue;
    StringRequest stringRequest;
    String rep;

    String TAG = MainActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        final ChatArrayAdapter chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.item_send);
        final ListView listView = (ListView) findViewById(R.id.chat_history);
        listView.setAdapter(chatArrayAdapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);
        //to scroll the list view to bottom on data change

        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        messageText = (EditText) findViewById(R.id.messageText);
        submitButton = (Button) findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = messageText.getText().toString();

                sendMessage(chatArrayAdapter, message);

                data.put("message",message);
//                messageText.setText("");
                postData(data, chatArrayAdapter);
                messageText.setText("");
            }
        });
    }

    public void postData(HashMap data, final ChatArrayAdapter adapter){

        mRequestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            final String s = response.getString("message");
                              receiveMessage(adapter, s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error: " + error.toString());
                    }
                }
        );

        mRequestQueue.add(jsonobj);

    }

    private boolean receiveMessage(ChatArrayAdapter adapter, String msg) {
        adapter.add(new ResponseMessage(msg, false));
        return true;
    }


    private boolean sendMessage(ChatArrayAdapter adapter, String msg) {
        adapter.add(new ResponseMessage(msg, true));
        return true;
    }
}
