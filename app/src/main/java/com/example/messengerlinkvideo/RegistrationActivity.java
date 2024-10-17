package com.example.messengerlinkvideo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonSignUp;

    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        setupClickListeners();
        observeViewModel();
    }

    private void setupClickListeners() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = editTextLogin.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                viewModel.registration(login,password);
            }
        });

    }


    private void observeViewModel(){
        viewModel.getCurrentUser().observe(this, new Observer<CurrentUser>() {
            @Override
            public void onChanged(CurrentUser currentUser) {
                Log.d("LoginActivity", currentUser.getToken().toString());
                Intent intent = UsersActivity.newIntent(RegistrationActivity.this, currentUser);
                startActivity(intent);
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                Toast.makeText(RegistrationActivity.this, String.format(getString(R.string.error_message_reg),error), Toast.LENGTH_SHORT).show();

            }
        });
        viewModel.getIsRegistration().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean IsRegistration) {
                if (IsRegistration) {
                    Toast.makeText(RegistrationActivity.this, R.string.success_reg, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }



    private void initViews(){
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

    }

    public static Intent newIntent (Context context){
        return new Intent(context, RegistrationActivity.class);
    }

}