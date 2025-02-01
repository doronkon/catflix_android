package com.example.catflix_android.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.catflix_android.Activities.AdminPageActivity;
import com.example.catflix_android.Activities.CategoryMoviesActivity;
import com.example.catflix_android.Activities.HomePageActivity;
import com.example.catflix_android.Activities.LoginActivity;
import com.example.catflix_android.Activities.ProfileActivity;
import com.example.catflix_android.Activities.SearchActivity;
import com.example.catflix_android.DataManager;
import com.example.catflix_android.Entities.Category;
import com.example.catflix_android.R;
import com.example.catflix_android.ViewModels.CategoryViewModel;
import com.example.catflix_android.ViewModels.CurrentUserViewModel;
import com.example.catflix_android.ViewModels.LocalDataViewModel;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HeaderFragment extends Fragment {
    private LocalDataViewModel localDataViewModel;

    private CurrentUserViewModel currentUserViewModel2;

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

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPrefs", 0);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", true); // Default: Dark Mode

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_header, container, false);

        // Set up click listeners for "Home" and "Profile"
        TextView homeLink = rootView.findViewById(R.id.home_link);
        ImageButton logoutBTN = rootView.findViewById(R.id.logoutBtn);

        Button themeToggleBtn = rootView.findViewById(R.id.themeToggleBtn);

        // Set initial icon based on current mode
        if (isDarkMode) {
            themeToggleBtn.setText("️☀️"); // Moon icon for Dark Mode
        } else {
            themeToggleBtn.setText("🌙"); // Sun icon for Light Mode
        }

        themeToggleBtn.setOnClickListener(v -> {
            boolean newMode = !isDarkMode; // Toggle mode
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDarkMode", newMode);
            editor.apply();

            // Apply theme change
            if (newMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Restart entire app to apply theme across all activities
            Intent intent = new Intent(requireActivity(), HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish(); // Close current activity
        });



        homeLink.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getActivity(), HomePageActivity.class);
            startActivity(homeIntent);
        });

        TextView adminLink = rootView.findViewById(R.id.admin);

        if (isAdmin) {
            adminLink.setVisibility(View.VISIBLE);
            adminLink.setOnClickListener(v -> {
                Intent adminIntent = new Intent(getActivity(), AdminPageActivity.class);
                startActivity(adminIntent);
            });
        } else {
            adminLink.setVisibility(View.GONE);  // Hide the TextView when isAdmin is false
        }

        //category drop down list
        categoryViewModel = new CategoryViewModel(requireContext(),getViewLifecycleOwner());
        localDataViewModel = new LocalDataViewModel(requireContext(),getViewLifecycleOwner());
        logoutBTN.setOnClickListener(v->{
            //
            localDataViewModel.getDeleteComplete().observe(getViewLifecycleOwner(),val->{
                if(val)
                {
                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    requireActivity().finish(); // Close the current activity
                }
            });
            localDataViewModel.deleteRoom();

        });

        Spinner dropdownList =  rootView.findViewById(R.id.dropdownList);

        // Initially empty list
        List<String> items = new ArrayList<>();

        // Adapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.custom_spinner_layout, // Layout for each item
                items
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_layout);
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

        //pfp and logout
        currentUserViewModel2 = new CurrentUserViewModel(requireContext(),getViewLifecycleOwner());
        ImageView imageView = rootView.findViewById(R.id.imageView2);

        imageView.setOnClickListener(v->{
            Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(profileIntent);
        });

        currentUserViewModel2.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                String url = user.getImage();
                if(url.charAt(0)=='.')
                {
                    url=url.substring(url.lastIndexOf('/')+1);
                }
                String imageUrl = "http://10.0.2.2:8080/media/userLogos/" + url;



                Glide.with(requireActivity())
                        .load(imageUrl)
                        .skipMemoryCache(true)  // Skip memory cache
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip disk cache
                        .into(imageView);
            } else {
                //onFailure/404,403....
            }
        });
        Button btn = rootView.findViewById(R.id.searchBtn);

        btn.setOnClickListener(v -> {
            Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
            startActivity(searchIntent);
        });

        return rootView;
    }
}
