package com.example.catflix_android.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.catflix_android.Entities.User;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.UserViewModel;
import com.example.catflix_android.Utils;

public class SignUpActivity extends AppCompatActivity {

    private Uri selectedImageUri;
    private ImageView imgSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI elements
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        imgSelected = findViewById(R.id.imgSelected);

        final EditText nameEditText = findViewById(R.id.name);
        final EditText displayNameEditText = findViewById(R.id.displayName);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText emailEditText = findViewById(R.id.email);
        final Button signUpButton = findViewById(R.id.signUpBTN);

        // Register activity result for image selection
        ActivityResultCallback<Uri> activityResultCallback = result -> {
            if (result != null) {
                selectedImageUri = result;
                imgSelected.setImageURI(result);  // Show the selected image in the ImageView
            }
        };

        // Use the ActivityResultContracts.GetContent to open the image picker
        registerForActivityResult(new ActivityResultContracts.GetContent(), activityResultCallback);

        // Set up image picker logic
        btnChooseImage.setOnClickListener(view -> {
            // Open the image picker
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivity(intent); // Using startActivity directly now
        });

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // SignUp button listener
        signUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String displayName = displayNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            // Validate fields
            if (!name.isEmpty() && !password.isEmpty() && !displayName.isEmpty() && !email.isEmpty()) {
                // Convert selected image to Base64 (if present)
                String base64Image = null;
                if (selectedImageUri != null) {
                    base64Image = Utils.imageUriToBase64(this, selectedImageUri);
                }
                // Create User object
                User userCreate = new User(null, base64Image, email, password, displayName, name, false);



                // Handle the user creation with ViewModel
                UserViewModel model = new UserViewModel(this, this);
                MutableLiveData<User> userResponse = new MutableLiveData<>();
                userResponse.observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user != null) {
                            // Navigate to login screen if sign up is successful
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // Send the user data to ViewModel (and Base64 image if any)
                model.signUp(userResponse, userCreate);
            } else {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
