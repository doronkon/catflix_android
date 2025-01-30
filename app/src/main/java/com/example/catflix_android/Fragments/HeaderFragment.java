package com.example.catflix_android.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.catflix_android.Activities.AdminPageActivity;
import com.example.catflix_android.Activities.CategoryActivity;
import com.example.catflix_android.Activities.CategoryMoviesActivity;
import com.example.catflix_android.Activities.DeleteMovieActivity;
import com.example.catflix_android.Activities.HomePageActivity;
import com.example.catflix_android.Activities.ProfileActivity;
import com.example.catflix_android.Activities.UpdateMovieActivity;
import com.example.catflix_android.DataManager;
import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CategoryViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HeaderFragment extends Fragment {
    private CategoryViewModel categoryViewModel;
    private HashMap<String, String> categoryMap = new HashMap<>(); // Map category name to ID
    private String selectedCategoryId;

    public HeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        boolean isAdmin = DataManager.getIsAdmin();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_header, container, false);

        // Set up click listeners for "Home" and "Profile"
        TextView homeLink = rootView.findViewById(R.id.home_link);
        TextView profileLink = rootView.findViewById(R.id.profile_link);
        TextView adminLink = rootView.findViewById(R.id.admin);

        homeLink.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getActivity(), HomePageActivity.class);
            startActivity(homeIntent);
        });

        profileLink.setOnClickListener(v -> {
            Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(profileIntent);
        });
        if(!isAdmin){
            adminLink.setVisibility(View.VISIBLE);
            adminLink.setOnClickListener(v -> {
                Intent adminIntent = new Intent(getActivity(), AdminPageActivity.class);
                startActivity(adminIntent);
            });
        }

        //category drop down list
        categoryViewModel = new CategoryViewModel(requireContext(),this);
        Spinner dropdownList =  rootView.findViewById(R.id.dropdownList);

        // Initially empty list
        List<String> items = new ArrayList<>();

        // Adapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item, // Layout for each item
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownList.setAdapter(adapter);
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(),categories-> {
            if (categories != null)
            {
                List<String> categoryNames = new ArrayList<>();
                categoryNames.add("Categories");
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
                if (position != 0) {
                    String selectedCategoryName = parent.getItemAtPosition(position).toString();
                    selectedCategoryId = categoryMap.get(selectedCategoryName); // Get the corresponding ID
                    if (selectedCategoryId != null) { // Avoid null values
                        Intent intent = new Intent(requireContext(), CategoryMoviesActivity.class);
                        intent.putExtra("category_id", selectedCategoryId);
                        intent.putExtra("category_name", selectedCategoryName);
                        startActivity(intent);
                    } else {
                        Toast.makeText(requireContext(), "Category not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = null;
            }
        });
        categoryViewModel.fetchCategories();


        return rootView;
    }
}
