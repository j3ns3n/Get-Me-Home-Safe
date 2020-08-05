package com.jensen.getmehomesafe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    private Button openMapButton;
    private Button planJourneyButton;
    private Button panicSettingsButton;
    private Button panicButton;
    private Switch panicSwitch;

    public boolean PANIC_BUTTON_ENABLED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openMapButton = findViewById(R.id.home_open_map);
        planJourneyButton = findViewById(R.id.home_plan_journey);
        panicSettingsButton = findViewById(R.id.home_panic_settings_button);
        panicButton = findViewById(R.id.home_panic_button);
        panicSwitch = findViewById(R.id.home_panic_switch);

        // Initial Background Colour
        panicButton.setBackgroundColor(Color.RED);

        // Triggered when the switch is flipped
        panicSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton switchView, boolean isChecked) {
                if (isChecked) {
                    // Enables the panic button and sets its background colour to green
                    PANIC_BUTTON_ENABLED = true;
                    panicButton.setBackgroundColor(Color.GREEN);
                    panicSwitch.setText(getString(R.string.home_switch_panic_on));
                } else {
                    // Disables the panic button and sets its background colour to red
                    PANIC_BUTTON_ENABLED = false;
                    panicButton.setBackgroundColor(Color.RED);
                    panicSwitch.setText(getString(R.string.home_switch_panic_off));
                }
            }
        });

        openMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    requestPermissions(new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  }, 1000);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivity(intent);
                }
            }
        });

        planJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: ADD JOURNEY SCREEN
                Toast.makeText(getApplicationContext(), "COMING SOON!", Toast.LENGTH_SHORT).show();
            }
        });

        panicSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: ADD PANIC SETTINGS SCREEN
            }
        });

        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: ADD PANIC BUTTON FUNCTIONALITY
            }
        });
    }

}
