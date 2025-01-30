package com.example.catflix_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;

public class AdminPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);

        // Add the fragment dynamically to the container (header_container)
        if (savedInstanceState == null) {
            HeaderFragment headerFragment = new HeaderFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.header_container, headerFragment); // Replace the container with the fragment
            transaction.commit();
        }

        Button toDeleteMovie = findViewById(R.id.toDeleteMovieBTN);
        toDeleteMovie.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPageActivity.this, DeleteMovieActivity.class);
            startActivity(intent);
        });

        Button toEditOrDeleteCategory = findViewById(R.id.toEditOrDeleteCategory);
        toEditOrDeleteCategory.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPageActivity.this, EditAndDeleteCategory.class);
            startActivity(intent);
        });

        Button toUploadMovie = findViewById(R.id.toUploadMovie);
        toUploadMovie.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPageActivity.this, UploadMovieActivity.class);
            startActivity(intent);
        });

        Button toUploadCategory = findViewById(R.id.toUploadCategory);
        toUploadCategory.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPageActivity.this, UploadCategoryActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}