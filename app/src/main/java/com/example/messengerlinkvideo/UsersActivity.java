package com.example.messengerlinkvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENTUSER = "currentUser";
    private UsersActivityViewModel viewModel;
    private UsersActivityViewModelFactory viewModelFactory;
    private RecyclerView recyclerViewUsers;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        initViews();
        CurrentUser currentUser = (CurrentUser) getIntent().getSerializableExtra(EXTRA_CURRENTUSER);
        if (currentUser != null) {
            String accessToken = currentUser.getToken().getAccess();
            String refreshToken = currentUser.getToken().getRefresh();
            viewModelFactory = new UsersActivityViewModelFactory(accessToken, refreshToken);
            viewModel = new ViewModelProvider(this, viewModelFactory).get(UsersActivityViewModel.class);
        }
        observeViewModel();
        adapter.setOnUserClickListener(new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                if (currentUser != null) {
                    Intent intent = ChatActivity.newIntent(UsersActivity.this,
                            currentUser.getId(),
                            user.getId(),
                            user.getLogin(),
                            currentUser.getToken().getAccess(),
                            currentUser.getToken().getRefresh());
                    startActivity(intent);
                }
            }
        });

    }

    private void observeViewModel(){
        viewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });
    }
    private void initViews(){
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        adapter = new UsersAdapter();
        recyclerViewUsers.setAdapter(adapter);
    }

    public static Intent newIntent(Context context, CurrentUser currentUser) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_CURRENTUSER, currentUser);
        return intent;
    }
}