package org.iklesic.hcshare.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Chat;
import org.iklesic.hcshare.model.Message;
import org.iklesic.hcshare.ui.activity.MessagesActivity;
import org.iklesic.hcshare.ui.adapter.ChatAdapter;
import org.iklesic.hcshare.ui.listner.ChatListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatsFragment extends Fragment implements ChatListener {

    @BindView(R.id.chat_list)
    RecyclerView chatList;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ChatAdapter chatAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        chatAdapter = new ChatAdapter(this);

        chatList.setAdapter(chatAdapter);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        if (firebaseAuth.getCurrentUser() != null){

            String user = firebaseAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("messages").whereEqualTo("userTo", user).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                    if (e==null){

                        if (queryDocumentSnapshots != null){

                            List<Chat> chats = new ArrayList<>();

                            for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                                Message message = document.toObject(Message.class);

                                Chat chat = new Chat(message.getUserFrom(),message.getNameFrom());

                                boolean isInList = false;

                                for (int i = 0; i < chats.size(); i++){
                                    if (chat.getId().equals(chats.get(i).getId())){
                                        isInList = true;
                                        break;
                                    }
                                }

                                if (!isInList){
                                    chats.add(chat);
                                }

                            }

                            chatAdapter.setChats(chats);

                        }

                    }

                }
            });

        }

    }

    @Override
    public void onChatItemClicked(String receiver) {
        Intent intent = new Intent(getContext(), MessagesActivity.class);
        Bundle bundle  =new Bundle();
        bundle.putString(MessagesActivity.RECEIVER_ID_EXTRA, receiver);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
