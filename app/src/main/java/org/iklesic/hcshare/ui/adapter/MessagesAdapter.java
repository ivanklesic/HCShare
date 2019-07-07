package org.iklesic.hcshare.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Message;
import org.iklesic.hcshare.ui.viewholder.MessagesViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesViewHolder> {

    private List<Message> messages = new ArrayList<>();
    private String userId;

    public MessagesAdapter(String userId) {
        this.userId = userId;
    }

    public void setMessages(List<Message> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessagesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder messagesViewHolder, int i) {
        messagesViewHolder.setMessaage(messages.get(i));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getUserFrom().equals(userId)){
            return R.layout.item_message_from;
        }else {
            return R.layout.item_message_to;
        }
    }
}
