package com.sharparam.android.AndroidTest;

import android.os.Bundle;
import android.view.MenuItem;

/**
 * Created by on 2014-01-31.
 *
 * @author Sharparam
 */
public class ButtonActivity extends BackenabledActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttons);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}