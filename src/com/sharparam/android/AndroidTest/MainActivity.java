package com.sharparam.android.AndroidTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private int clickCount;
    private TextView countLabel;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        clickCount = 0;

        final Resources res = getResources();

        countLabel = (TextView) findViewById(R.id.count_label);
        countLabel.setText(String.format(res.getString(R.string.count_label), clickCount));

        findViewById(R.id.count_button).setOnClickListener(this);
        findViewById(R.id.set_button).setOnClickListener(this);
        findViewById(R.id.activity_button).setOnClickListener(this);
        findViewById(R.id.frame_button).setOnClickListener(this);
    }

    private AlertDialog getNameDialog(String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.name_dialog_title))
               .setMessage(String.format(getResources().getString(R.string.name_dialog_message), name))
               .setCancelable(true)
               .setPositiveButton(android.R.string.ok, null);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.count_button:
                clickCount++;
                countLabel.setText(String.format(getResources().getString(R.string.count_label), clickCount));
                break;
            case R.id.set_button:
                EditText field = (EditText) findViewById(R.id.name_field);
                Editable text = field.getText();
                if (text == null)
                    return;
                String name = text.toString();
                if (name == null || name.length() == 0)
                    return;
                field.setText(getResources().getString(R.string.empty));
                AlertDialog dialog = getNameDialog(name);
                dialog.show();
                break;
            case R.id.activity_button:
                Intent intent = new Intent(this, ButtonActivity.class);
                startActivity(intent);
                break;
            case R.id.frame_button:
                Intent frameIntent = new Intent(this, FrameActivity.class);
                startActivity(frameIntent);
                break;
        }
    }
}
