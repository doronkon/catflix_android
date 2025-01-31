package com.example.catflix_android.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.catflix_android.DataManager;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;
import com.example.catflix_android.Utils;
import com.example.catflix_android.ViewModels.CurrentUserViewModel;

public class EditProfileActivity extends AppCompatActivity {

    private CurrentUserViewModel currentUserViewModel;
    private Uri selectedImageUri;
    private ImageView imgSelected;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Add the fragment dynamically to the container (header_container)
        if (savedInstanceState == null) {
            HeaderFragment headerFragment = new HeaderFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.header_container, headerFragment); // Replace the container with the fragment
            transaction.commit();
        }
        currentUserViewModel = new CurrentUserViewModel(this,this);
        Button editProfileBtn = findViewById(R.id.editProfileBTN);
        final EditText displayNameEditText = findViewById(R.id.displayName);


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



        currentUserViewModel.getEditEnded().observe(this,val->{
            if(val)
            {
                Toast.makeText(this, "Edited Successfully", Toast.LENGTH_LONG).show();

            }
        });
        editProfileBtn.setOnClickListener(v->{
            String displayName = displayNameEditText.getText().toString().trim();
            String image = selectedImageUri==null? null: Utils.imageUriToBase64(this, selectedImageUri);
            if(DataManager.getCurrentUserId() == null)
            {
                return;
            }
            User userEdit = new User(DataManager.getCurrentUserId(), image, null, null, displayName, null, DataManager.getIsAdmin());
            currentUserViewModel.editUser(userEdit);
        });

    }
}