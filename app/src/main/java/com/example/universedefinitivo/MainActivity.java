package com.example.universedefinitivo;

import androidx.appcompat.app.AppCompatActivity;
import com.example.universedefinitivo.databinding.ActivityMainBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemReselectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home_button:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.search_button:
                    replaceFragment(new SearchFragment());
                    break;

                case R.id.map_button:
                    replaceFragment(new MapFragment());
                    break;

                case R.id.profile_button:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}