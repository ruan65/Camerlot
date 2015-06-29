package premiumapp.org.camerlot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity
        implements ControlFragment.CameraControlListener {


    private ControlFragment mControlFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mControlFragment == null) {
            mControlFragment = new ControlFragment();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fr_container, mControlFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCameraRequest() {

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null) // Close the camera if the "back" button have been pressed
                .replace(R.id.fr_container, new CameraFragment())
                .commit();
    }
}

