package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatbot.adapter.MessageAdapter;
import com.example.chatbot.modal.MessageModal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chats;
    private EditText editMessage;
    private ImageButton sentButton;

    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";

    private RequestQueue requestQueue;

    private ArrayList<MessageModal> messageModalList;

    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setListeners();
    }

    private void init() {
        chats = findViewById(R.id.chats);
        editMessage = findViewById(R.id.editMessage);
        sentButton = findViewById(R.id.sentButton);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.getCache().clear();

        messageModalList = new ArrayList<>();

        messageAdapter = new MessageAdapter(messageModalList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

        chats.setLayoutManager(linearLayoutManager);
        chats.setAdapter(messageAdapter);
    }

    private void setListeners() {
        sentButton.setOnClickListener(view -> {
            if (editMessage.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter your message...", Toast.LENGTH_SHORT).show();
                return;
            }

            sendMessage(editMessage.getText().toString());
            editMessage.setText("");
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sendMessage(String userMessage) {
        messageModalList.add(new MessageModal(userMessage, USER_KEY));
        messageAdapter.notifyDataSetChanged();

        String api = "http://api.brainshop.ai/get?bid=156451&key=RdjOJWhMeR2JDdOW&uid=[uid]&msg=[msg]" + userMessage;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api, null, response -> {
            try {
                String botResponse = response.getString("cnt");
                messageModalList.add(new MessageModal(botResponse, BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();

                messageModalList.add(new MessageModal("No response", BOT_KEY));
                messageAdapter.notifyDataSetChanged();
            }
        }, error -> {
            messageModalList.add(new MessageModal("Sorry no response found", BOT_KEY));
            Toast.makeText(MainActivity.this, "No response from the bot", Toast.LENGTH_SHORT).show();
        });

        queue.add(jsonObjectRequest);
    }
}