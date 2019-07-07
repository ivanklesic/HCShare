package org.iklesic.hcshare.ui.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.message)
    TextView message;

    public MessagesViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setMessaage(Message message){
        this.message.setText(message.getText());
    }
}
