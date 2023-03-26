package com.example.chatbot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot.R;
import com.example.chatbot.modal.MessageModal;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    private ArrayList<MessageModal> messageModalList;
    private Context context;

    public MessageAdapter(ArrayList<MessageModal> messageModalList, Context context) {
        this.messageModalList = messageModalList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_message, parent, false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_message, parent, false);
                return new BotViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModal messageModal = messageModalList.get(position);

        switch (messageModal.getSender()) {
            case "user":
                ((UserViewHolder) holder).userText.setText(messageModal.getMessage());
                break;
            case "bot":
                ((BotViewHolder) holder).botText.setText(messageModal.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageModalList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (messageModalList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView userText;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userText = itemView.findViewById(R.id.userText);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {

        TextView botText;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);

            botText = itemView.findViewById(R.id.botText);
        }
    }
}
