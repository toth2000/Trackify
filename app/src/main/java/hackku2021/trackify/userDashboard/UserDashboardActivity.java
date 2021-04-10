package hackku2021.trackify.userDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import hackku2021.trackify.R;
import hackku2021.trackify.bottomNavFragment.BottomNavigationHome;
import hackku2021.trackify.bottomNavFragment.BottomNavigationMyAccount;


public class UserDashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        toolbar = findViewById(R.id.useDashBoardToolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        bottomNavFragChange(0, null);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    bottomNavFragChange(item.getItemId(), item);
                    return true;
                }
            };

    void bottomNavFragChange(int id, MenuItem item)
    {
        Fragment selectedFragment = null;

        switch (id)
        {   case R.id.bottom_nav_home:
                selectedFragment = new BottomNavigationHome();
                break;

            case R.id.bottom_nav_my_account:
                selectedFragment = new BottomNavigationMyAccount();
                break;
        }

        if(selectedFragment==null)
            selectedFragment = new BottomNavigationHome();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }
}