package com.example.catflix_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import com.example.catflix_android.Fragments.HeaderFragment;
import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CategoryViewModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditAndDeleteCategory extends AppCompatActivity {

    private CategoryViewModel categoryViewModel;
    private HashMap<String, String> categoryMap = new HashMap<>(); // Map category name to ID
    private String selectedCategoryId;

    private String selectedCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_and_delete_category);

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
                selectedCategoryName = parent.getItemAtPosition(position).toString();
                selectedCategoryId = categoryMap.get(selectedCategoryName); // Get the corresponding ID
                Toast.makeText(EditAndDeleteCategory.this, "Selected Category: " + selectedCategoryName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = null;
            }
        });
        categoryViewModel.fetchCategories();

        Button deleteCategoryBTN = findViewById(R.id.deleteSelectedCategoryBTN);
        deleteCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with the actual category ID you want to delete
                deleteCategory(selectedCategoryId);
            }
        });

        Button editCategoryBTN = findViewById(R.id.editSelectedCategoryBTN);

        editCategoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAndDeleteCategory.this, EditCategoryPageActivity.class);
                intent.putExtra("selectedCategoryId", selectedCategoryId);
                intent.putExtra("selectedCategoryName", selectedCategoryName);
                startActivity(intent);
            }
        });
    }
    public void deleteCategory(String categoryId){
        categoryViewModel.deleteCategory(categoryId);
    }
}