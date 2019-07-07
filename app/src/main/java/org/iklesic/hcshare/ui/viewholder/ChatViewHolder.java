package org.iklesic.hcshare.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Chat;
import org.iklesic.hcshare.ui.listner.ChatListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.chat_name)
    TextView chatName;
    @BindView(R.id.chat_arrow)
    ImageView chatArrow;
    @BindView(R.id.chat_item)
    ConstraintLayout chatItem;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    private Chat chat;
    private ChatListener listener;

    public void setChatItem(Chat chat, ChatListener listener){
        this.chat = chat;
        this.listener = listener;
        chatName.setText(chat.getName());
    }

    @OnClick(R.id.chat_item)
    public void onChatClicked(){
        listener.onChatItemClicked(chat.getId());
    }
}
