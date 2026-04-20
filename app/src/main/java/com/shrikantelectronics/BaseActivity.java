package com.shrikantelectronics;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        applyThemeFromGlobal();  // Set theme before super.onCreate
        super.onCreate(savedInstanceState);

    }


    //This code is repeated in MaiinActivity as base activity not working with MainActivity

    private void applyThemeFromGlobal() {
        switch (GlobalClass.APPTHEME) {
            case "AppTheme":
                setTheme(R.style.AppTheme);

                break;
            default:
                setTheme(R.style.AppTheme);
                break;
        }

    }



}

