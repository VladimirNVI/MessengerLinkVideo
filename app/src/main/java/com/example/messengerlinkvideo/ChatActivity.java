package com.example.messengerlinkvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";
    private static final String EXTRA_OTHER_USER_NAME = "other_user_name";
    private static final String EXTRA_ACCESS_TOKEN = "access_token";
    private static final String EXTRA_REFRESH_TOKEN = "refresh_token";

    private TextView textViewTitle;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;
    private MessagesAdapter messagesAdapter;

    private int currentUserId;
    private int otherUserId;
    private String otherUserName;
    private String accessToken;
    private String refreshToken;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        initExtra();


        viewModelFactory = new ChatViewModelFactory(currentUserId,otherUserId, accessToken, refreshToken);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

        textViewTitle.setText(otherUserName);

        messagesAdapter = new MessagesAdapter(currentUserId);
        recyclerViewMessages.setAdapter(messagesAdapter);

        observeViewModel();
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                viewModel.sendMessage(message);
            }
        });



    }

    private void observeViewModel(){
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messagesAdapter.setMessages(messages);
                recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(ChatActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getMessageSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean sent) {
                if (sent){
                    editTextMessage.setText("");
                }
            }
        });

    }


    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
    }

    private void initExtra(){

        currentUserId = getIntent().getIntExtra(EXTRA_CURRENT_USER_ID,0);
        otherUserId = getIntent().getIntExtra(EXTRA_OTHER_USER_ID,0);
        otherUserName = getIntent().getStringExtra(EXTRA_OTHER_USER_NAME);
        accessToken = getIntent().getStringExtra(EXTRA_ACCESS_TOKEN);
        refreshToken = getIntent().getStringExtra(EXTRA_REFRESH_TOKEN);

    }

    public static Intent newIntent(Context context, int currentUserId, int otherUserId, String otherUserName, String accessToken, String refreshToken ){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId);
        intent.putExtra(EXTRA_OTHER_USER_NAME, otherUserName);
        intent.putExtra(EXTRA_ACCESS_TOKEN, accessToken);
        intent.putExtra(EXTRA_REFRESH_TOKEN, refreshToken);
        return intent;
    }

}