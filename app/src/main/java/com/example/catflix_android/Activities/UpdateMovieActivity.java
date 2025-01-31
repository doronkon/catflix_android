package com.example.catflix_android.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.Entities.Movie;
import com.example.catflix_android.Entities.User;
import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;
import com.example.catflix_android.Utils;
import com.example.catflix_android.ViewModels.CategoryViewModel;
import com.example.catflix_android.ViewModels.MovieViewModel;
import com.example.catflix_android.ViewModels.UserViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UpdateMovieActivity extends AppCompatActivity {
    private Uri selectedImageUri;
    private Uri selectedVideoUri;

    private ActivityResultLauncher<String> imagePickerLauncher;
    private ActivityResultLauncher<String> videoPickerLauncher;
    private MovieViewModel model;
    private CategoryViewModel categoryViewModel;
    private HashMap<String, String> categoryMap = new HashMap<>(); // Map category name to ID
    private String selectedCategoryId;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movie);
        // Add the fragment dynamically to the container (header_container)
        if (savedInstanceState == null) {
            HeaderFragment headerFragment = new HeaderFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.header_container, headerFragment); // Replace the container with the fragment
            transaction.commit();
        }

        String movieId = getIntent().getStringExtra("movie_id");
        String movieName = getIntent().getStringExtra("movie_name");
        TextView textViewEditingMovie = findViewById(R.id.textViewEditingMovie);
        textViewEditingMovie.setText("You are editing: " + movieName);



        final EditText nameEditText = findViewById(R.id.editTextName);
        final EditText directorEditText = findViewById(R.id.editTextDirector);
        final EditText actorsEditText = findViewById(R.id.editTextActors);
        final EditText descriptionEditText = findViewById(R.id.editTextDescription);
        final EditText minimalAgeEditText = findViewById(R.id.editTextMinimalAge);
        final RadioGroup catflixOriginalGroup = findViewById(R.id.radioGroupCatflixOriginal);
        final Button uploadMovieBtn = findViewById(R.id.buttonUploadMovie);

        model = new MovieViewModel(this, this);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedImageUri = uri;
                Toast.makeText(this, "Image selected: " + uri, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });

        videoPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedVideoUri = uri;
                Toast.makeText(this, "Video selected: " + uri, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.buttonUploadThumbnail).setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        findViewById(R.id.buttonUploadVideo).setOnClickListener(v -> videoPickerLauncher.launch("video/*"));

        uploadMovieBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String director = directorEditText.getText().toString().trim();
            String actors = actorsEditText.getText().toString().trim();
            String desc = descriptionEditText.getText().toString().trim();
            String minimal = minimalAgeEditText.getText().toString().trim();
            boolean isCatflixOriginal = catflixOriginalGroup.getCheckedRadioButtonId() == R.id.radioTrue;


            String image = selectedImageUri == null ?  null : Utils.imageUriToBase64(this, selectedImageUri);
            String video = selectedVideoUri == null  ?  null : Utils.videoUriToBase64(this, selectedVideoUri);
            //hard coded category switch to dropDown list
            Movie movie = new Movie(movieId, video, minimal, isCatflixOriginal, desc, image, null, actors, director, selectedCategoryId, null, name);

            model.editMovie(movie);
        });

        //category drop down list start
        categoryViewModel = new CategoryViewModel(this,this);
        Spinner dropdownList = findViewById(R.id.dropdownList);

        // Initially empty list
        List<String> items = new ArrayList<>();

        // Adapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_layout,  // Use your custom layout for the dropdown items
                items
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_layout);
        dropdownList.setAdapter(adapter);
        categoryViewModel.getCategories().observe(this,categories-> {
            if (categories != null)
            {
                List<String> categoryNames = new ArrayList<>();
                for (Category category : categories) {
                    categoryNames.add(category.getName());
                    categoryMap.put(category.getName(), category.get_id());
                }
                items.clear();
                items.addAll(categoryNames);
                adapter.notifyDataSetChanged(); // Notify adapter of data change

            }
        });
        // Handle selection
        dropdownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategoryName = parent.getItemAtPosition(position).toString();
                selectedCategoryId = categoryMap.get(selectedCategoryName); // Get the corresponding ID
                Toast.makeText(UpdateMovieActivity.this, "Selected ID: " + selectedCategoryId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = null;
            }
        });
        categoryViewModel.fetchCategories();


    }
}