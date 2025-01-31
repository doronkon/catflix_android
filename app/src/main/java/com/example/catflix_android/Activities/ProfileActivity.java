package com.example.catflix_android.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CurrentUserViewModel;
import com.example.catflix_android.ViewModels.UserViewModel;

public class ProfileActivity extends AppCompatActivity {
    private CurrentUserViewModel currentUserViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);


        // Add the fragment dynamically to the container (header_container)
        if (savedInstanceState == null) {
            HeaderFragment headerFragment = new HeaderFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.header_container, headerFragment); // Replace the container with the fragment
            transaction.commit();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView textBoxDisplayName = findViewById(R.id.textDisplayName);
        Button editProfileBtn = findViewById(R.id.editProfileButton);
        editProfileBtn.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        currentUserViewModel = new CurrentUserViewModel(this,this);


        currentUserViewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                textBoxDisplayName.setText("HEY THERE, "+user.getDisplayName());
                ImageView imageView = findViewById(R.id.imageView);
                String url = user.getImage();
                if(url.charAt(0)=='.')
                {
                    url=url.substring(url.lastIndexOf('/')+1);
                }
                String imageUrl = "http://10.0.2.2:8080/media/userLogos/" + url;


                Glide.with(ProfileActivity.this)
                        .load(imageUrl)
                        .skipMemoryCache(true)  // Skip memory cache
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
                        .into(imageView);
            } else {
                //onFailure/404,403....
            }
        });
    }
}