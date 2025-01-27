package com.example.catflix_android.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.catflix_android.Activities.HomePageActivity;
import com.example.catflix_android.Activities.ProfileActivity;
import com.example.catflix_android.R;

public class HeaderFragment extends Fragment {

    public HeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_header, container, false);

        // Set up click listeners for "Home" and "Profile"
        TextView homeLink = rootView.findViewById(R.id.home_link);
        TextView profileLink = rootView.findViewById(R.id.profile_link);

        homeLink.setOnClickListener(v -> {
            Intent homeIntent = new Intent(getActivity(), HomePageActivity.class);
            startActivity(homeIntent);
        });

        profileLink.setOnClickListener(v -> {
            Intent profileIntent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(profileIntent);
        });

        return rootView;
    }
}
