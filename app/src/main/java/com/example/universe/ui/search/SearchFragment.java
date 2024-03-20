package com.example.universe.ui.search;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import java.util.ArrayList;
        import java.util.List;

        import com.example.universe.R;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import java.util.ArrayList;
        import java.util.List;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // Find the Spinner in the inflated layout
        Spinner spinner = rootView.findViewById(R.id.categories_search);

        // Define your list of categories
        List<String> categoriesList = new ArrayList<>();
        categoriesList.add("History");
        categoriesList.add("Thriller");
        categoriesList.add("Sci-Fi");
        categoriesList.add("Fantasy");
        categoriesList.add("Romance");
        categoriesList.add("Adventure");
        categoriesList.add("Self-help");
        categoriesList.add("Essay");
        categoriesList.add("Comic/Graphic novel");
        categoriesList.add("Manga");
        categoriesList.add("Children");
        categoriesList.add("Poetry");
        categoriesList.add("Cook-book");
        categoriesList.add("Travel");
        categoriesList.add("Other");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoriesList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set the prompt text programmatically
        spinner.post(new Runnable() {
            @Override
            public void run() {
                spinner.setPrompt("Select a category");
            }
        });

        return rootView;
    }
}
