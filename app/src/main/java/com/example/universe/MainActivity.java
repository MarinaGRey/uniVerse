package com.example.universe;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.universe.databinding.ActivityMainBinding;
import com.example.universe.ui.home.HomeFragment;
import com.example.universe.ui.map.MapFragment;
import com.example.universe.ui.profile.ProfileFragment;
import com.example.universe.ui.search.SearchFragment;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Replace the initial fragment
        replaceFragment(new HomeFragment());

        // Set listener for BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home_button:
                    replaceFragment(new HomeFragment());
                    return true;

                case R.id.search_button:
                    replaceFragment(new SearchFragment());
                    return true;

                case R.id.map_button:
                    replaceFragment(new MapFragment());
                    return true;

                case R.id.profile_button:
                    replaceFragment(new ProfileFragment());
                    return true;
            }
            return false;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
