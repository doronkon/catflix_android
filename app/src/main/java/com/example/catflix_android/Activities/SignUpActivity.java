package com.example.catflix_android.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.catflix_android.Entities.User;
import com.example.catflix_android.R;
import com.example.catflix_android.Utils;
import com.example.catflix_android.ViewModels.UserViewModel;

public class SignUpActivity extends AppCompatActivity {

    private Uri selectedImageUri;
    private ImageView imgSelected;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText nameEditText = findViewById(R.id.name);
        final EditText displayNameEditText = findViewById(R.id.displayName);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText emailEditText = findViewById(R.id.email);
        final Button singUpButton = findViewById(R.id.signUpBTN);

        // Image components
        Button btnTakePicture = findViewById(R.id.btnCameraImage);
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        imgSelected = findViewById(R.id.imgSelected);

        // Initialize gallery image picker
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        imgSelected.setImageURI(uri); // Display the selected image
                    }
                });

        // Initialize camera image capture
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        if (bitmap != null) {
                            // Save bitmap to MediaStore and retrieve its URI
                            selectedImageUri = Uri.parse(MediaStore.Images.Media.insertImage(
                                    getContentResolver(), bitmap, "Captured Image", null));
                            imgSelected.setImageURI(selectedImageUri); // Display the captured image
                        }
                    }
                });

        // Button to open the image picker
        btnChooseImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        // Button to open the camera
        btnTakePicture.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResultLauncher.launch(intent);
        });

        // Sign-up button logic
        singUpButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String displayName = displayNameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String image = selectedImageUri==null? null: Utils.imageUriToBase64(this, selectedImageUri);
            User userCreate = new User(null, image, email, password, displayName, name, false);

            if (!name.isEmpty() && !password.isEmpty() && !displayName.isEmpty() && !email.isEmpty()) {
                UserViewModel model = new UserViewModel(this, this);
                MutableLiveData<User> userResponse = new MutableLiveData<>();
                userResponse.observe(this, user -> {
                    if (user != null) {
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        System.out.println("case 404,400,no network");
                    }
                });
                model.signUp(userResponse, userCreate);
            } else {
                System.out.println("aaa");
            }
        });
    }
}
