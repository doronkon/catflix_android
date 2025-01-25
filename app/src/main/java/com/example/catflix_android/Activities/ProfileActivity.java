package com.example.catflix_android.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView textBoxDisplayName = findViewById(R.id.textDisplayName);
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
                        .into(imageView);
            } else {
                //onFailure/404,403....
            }
        });
    }
}