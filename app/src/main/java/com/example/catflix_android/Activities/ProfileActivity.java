package com.example.catflix_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.UserViewModel;

public class ProfileActivity extends AppCompatActivity {

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


        UserViewModel model = new UserViewModel(this,this);
        MutableLiveData<User> userResponse= new MutableLiveData<>();
        userResponse.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    textBoxDisplayName.setText("HEY THERE, "+user.getDisplayName());
                    ImageView imageView = findViewById(R.id.imageView);
                    String url = user.getImage();
                    if(url.charAt(0)=='.')
                    {
                        url=url.substring(url.lastIndexOf('/')+1);
                    }
                    String imageUrl = "http://10.0.2.2:8080/media/userLogos/" + url;


                    Glide.with(com.example.catflix_android.Activities.ProfileActivity.this)
                            .load(imageUrl)
                            .into(imageView);
                } else {
                    //onFailure/404,403....
                }
            }
        });
        model.getUser(userResponse);
    }
}