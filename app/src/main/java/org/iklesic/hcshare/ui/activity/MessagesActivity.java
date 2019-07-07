package org.iklesic.hcshare.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.iklesic.hcshare.R;
import org.iklesic.hcshare.model.Message;
import org.iklesic.hcshare.model.User;
import org.iklesic.hcshare.ui.adapter.MessagesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessagesActivity extends AppCompatActivity {

    public static final String RECEIVER_ID_EXTRA = "reciever_id_extra";

    @BindView(R.id.messages_list)
    RecyclerView messagesList;
    @BindView(R.id.message_background)
    ImageView messageBackground;
    @BindView(R.id.send_message)
    Button sendMessage;
    @BindView(R.id.send_text)
    EditText sendText;

    private String receiver;
    private String messageFrom;
    private String sender;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            sender = firebaseAuth.getCurrentUser().getUid();
        }

        messagesAdapter = new MessagesAdapter(sender);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        messagesList.setLayoutManager(linearLayoutManager);
        messagesList.setAdapter(messagesAdapter);


        if (getIntent().getExtras() != null && getIntent().getExtras().getString(RECEIVER_ID_EXTRA) != null) {
            receiver = getIntent().getExtras().getString(RECEIVER_ID_EXTRA);
        }

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    messageFrom = user.getName();
                }
            }
        });



        firebaseFirestore.collection("messages").orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e == null){

                    if (queryDocumentSnapshots != null) {

                        List<Message> messages = new ArrayList<>();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots
                        ) {
                            Message message = document.toObject(Message.class);
                            if ((message.getUserTo().equals(receiver) && message.getUserFrom().equals(sender) || (message.getUserTo().equals(sender) && message.getUserFrom().equals(receiver))))
                            {
                                messages.add(message);
                            }
                        }

                        messagesAdapter.setMessages(messages);
                        messagesList.scrollToPosition(messagesAdapter.getItemCount());

                    }

                }
            }
        });
    }

    @OnClick(R.id.send_message)
    public void onSendClicked(){
        if (sendText.getText().length() != 0){
            String id = UUID.randomUUID().toString();

            Message message = new Message(id, sendText.getText().toString(), null, sender, receiver, messageFrom);

            firebaseFirestore.collection("messages").document(id).set(message).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_send_failed), Toast.LENGTH_SHORT).show();
                }
            });

            sendText.setText("");
        }
    }
}
