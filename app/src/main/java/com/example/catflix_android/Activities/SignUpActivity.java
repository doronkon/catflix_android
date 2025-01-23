package com.example.catflix_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.catflix_android.Entities.User;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.UserViewModel;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        final EditText nameEditText = findViewById(R.id.name);
        final EditText displayNameEditText = findViewById(R.id.displayName);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText emailEditText = findViewById(R.id.email);
        final Button singUpButton = findViewById(R.id.signUpBTN);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        singUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String displayName = displayNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            User userCreate = new User(null,null,email,password,displayName,name,false);

            if (!name.isEmpty() && !password.isEmpty() && !displayName.isEmpty() && !email.isEmpty()) {

                UserViewModel model = new UserViewModel(this,this);
                MutableLiveData<User> userResponse= new MutableLiveData<>();
                userResponse.observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user != null) {
                            System.out.println("Kill me");
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                        } else {
                            System.out.println("ima shelha");
                        }
                    }
                });
                model.signUp(userResponse,userCreate);

            } else {
                System.out.println("aaa");
            }
        });
    }
}