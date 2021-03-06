package com.example.sdi.loglibtest;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.example.sdi.mylibrary.PhoneLog;


public class MainActivity extends ActionBarActivity {

    PhoneLog log = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PhoneLog.setLogName("SimpleLogFile.txt");
        log = PhoneLog.getInstance();
        log.append("activity cretead");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener{

        public PlaceholderFragment() {
            PhoneLog.getInstance().append("placeholder created");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            rootView.findViewById(R.id.id_button_clickme).setOnClickListener(this);
            rootView.findViewById(R.id.id_button_change_log_and_save_prev).setOnClickListener(this);
            rootView.findViewById(R.id.id_button_change_log_and_delete_prev).setOnClickListener(this);
            rootView.findViewById(R.id.id_button_clear_folder).setOnClickListener(this);

            PhoneLog.writeToNewNewLog("phone info.txt", false);
            PhoneLog.getInstance().savePhoneSize(this.getActivity());
            PhoneLog.getInstance().saveSDKVersion();
            PhoneLog.getInstance().saveDevice();

            PhoneLog.writeToNewNewLog("simple log.txt", false);
            PhoneLog.getInstance().append("onCreateView");

            return rootView;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch(id){
                case R.id.id_button_clickme:
                    PhoneLog.getInstance().append("button clicked");
                    break;
                case R.id.id_button_change_log_and_save_prev:
                    boolean deletePrev = false;
                    PhoneLog.writeToNewNewLog("NewLog.txt", deletePrev);
                    break;
                case R.id.id_button_change_log_and_delete_prev:
                    PhoneLog.writeToNewNewLog("NewLog2.txt", true);
                    break;
                case R.id.id_button_clear_folder:
                    PhoneLog.clearLogFolder();
                    break;
            }
        }
    }
}
