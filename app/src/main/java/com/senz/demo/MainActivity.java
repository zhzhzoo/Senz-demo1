package com.senz.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.senz.sdk.SenzManager;
import com.senz.sdk.Senz;

import java.util.List;


public class MainActivity extends Activity {

    public static String TAG = MainActivity.class.getSimpleName();
    private SenzManager mSenzManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSenzManager = new SenzManager(this);
    }

    protected void onResume() {
        super.onResume();

        final TextView discovered = (TextView) findViewById(R.id.discovered);
        final TextView left = (TextView) findViewById(R.id.left);

        try {
            mSenzManager.startTelepathy(new SenzManager.TelepathyCallback() {
                private String buildOutput(List<Senz> senzes) {
                    StringBuilder sb = new StringBuilder(30);
                    for (Senz senz : senzes)
                        sb.append(String.format("type: %s, subtype: %s", senz.type(), senz.subType()));
                    return sb.toString();
                }

                @Override
                public void onDiscover(List<Senz> senzes) {
                    discovered.setText(buildOutput(senzes));
                }

                @Override
                public void onLeave(List<Senz> senzes) {
                    left.setText(buildOutput(senzes));
                }
            });
        }
        catch (Exception e) {
            Log.e(TAG, "unable to start telepathy", e);
        }
    }

    @Override
    protected void onPause() {
        try {
            mSenzManager.stopTelepathy();
        }
        catch (Exception e) {
            Log.e(TAG, "unable to stop telepathy", e);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mSenzManager.end();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
