package org.iklesic.hcshare.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Chat;
import org.iklesic.hcshare.ui.listner.ChatListener;
import org.iklesic.hcshare.ui.viewholder.ChatViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<Chat> chats = new ArrayList<>();
    private ChatListener listener;

    public ChatAdapter(ChatListener listener) {
        this.listener = listener;
    }

    public void setChats(List<Chat> chats) {
        this.chats.clear();
        this.chats.addAll(chats);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChatViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        chatViewHolder.setChatItem(chats.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
