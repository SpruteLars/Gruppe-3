package no.hiof.emilie.efinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profil:
                                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                return true;
                            case R.id.action_instillinger:
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                return true;
                        }
                        return false;
                    }
                }
        );

        /* Vurdere om vi skal endre Bot Nav til Bot App Bar
         * BottomAppBar bottomAppBar = (BottomAppBar) findViewById(R.id.bottom_navigation);
         */

        //Størrelse på ikoner i Bot Nav
        for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
            final View iconView = bottomNavigationMenuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            //Høyde & bredde
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics);
        }
    }
}