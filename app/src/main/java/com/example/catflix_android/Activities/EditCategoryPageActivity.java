package com.example.catflix_android.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CategoryViewModel;

import java.util.Objects;

public class EditCategoryPageActivity extends AppCompatActivity {
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        categoryViewModel = new CategoryViewModel(this,this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_category_page);


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

        String currentCategoryId = getIntent().getStringExtra("selectedCategoryId");
        String currentCategoryName = getIntent().getStringExtra("selectedCategoryName");

        TextView catName = findViewById(R.id.categoryName);
        catName.setText("You are editing " + currentCategoryName);

        EditText catNameInput = findViewById(R.id.newCategoryName);

        Spinner booleanSpinner = findViewById(R.id.booleanSpinner);


        String[] booleanOptions = {"True", "False"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_layout,  // Use your custom layout for the dropdown items
                booleanOptions
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_layout);

        booleanSpinner.setAdapter(adapter);

        Button editCat = findViewById(R.id.editSelectedCategoryBTN);
        editCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCatNameInput = catNameInput.getText().toString();
                String selectedValue = booleanSpinner.getSelectedItem().toString();
                editCategory(currentCategoryId, newCatNameInput, selectedValue);
            }
        });

    }

    public void editCategory(String categoryId, String newName, String flag){
        boolean realFlag;
        realFlag = Objects.equals(flag, "True");
        categoryViewModel.editCategory(categoryId,newName, realFlag);
    }
}