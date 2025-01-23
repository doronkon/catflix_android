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

import com.example.catflix_android.DataManager;
import com.example.catflix_android.DataTypes.LoginResponse;
import com.example.catflix_android.DataTypes.LoginUser;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find views
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);

        // Enable Edge-to-Edge
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UserViewModel

        // Set OnClickListener for loginButton
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
//(this).get(UserViewModel.class)

            if (!username.isEmpty() && !password.isEmpty()) {
                LoginUser loginUser = new LoginUser(username,password);

                UserViewModel model = new UserViewModel(this,this);
                MutableLiveData<LoginResponse> loggedUser= new MutableLiveData<>();
                loggedUser.observe(this, new Observer<LoginResponse>() {
                    @Override
                    public void onChanged(LoginResponse loginResponse) {
                        if (loginResponse != null) {
                            String x = DataManager.getCurrentUserId();

                            // Handle the response, update UI with loginResponse data
                            // For example:
                            String userId = loginResponse.getId();
                            String token = loginResponse.getToken();
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            System.out.println("ima shelha");
                            // Handle error or failure case
                        }
                    }
                });
                model.login(loggedUser,loginUser);

            } else {
                System.out.println("aaa");
                // Handle empty fields (e.g., show a Toast)
                // Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
