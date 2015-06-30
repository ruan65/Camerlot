package premiumapp.org.camerlot;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity
        implements ControlFragment.CameraControlListener {

    private boolean mCameraMode;

    private int mCameraId;

    private MenuItem mFrontMenuButton, mRearMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fr_container, new ControlFragment(), "controlFr")
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mCameraMode) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(getString(R.string.mode), mCameraMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mCameraMode = savedInstanceState.getBoolean(getString(R.string.mode));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (hasFrontCamera()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            mFrontMenuButton = menu.findItem(R.id.front_camera);
            mRearMenuButton = menu.findItem(R.id.back_camera);
            mFrontMenuButton.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ControlFragment controlFragment =
                (ControlFragment) getSupportFragmentManager().findFragmentByTag("controlFr");

        switch (item.getItemId()) {

            case R.id.front_camera:

                mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                mRearMenuButton.setVisible(true);
                mFrontMenuButton.setVisible(false);

                if (controlFragment != null) {
                    controlFragment.animateFAB(15, R.drawable.ic_camera_front_white_36dp);
                }
                break;

            case R.id.back_camera:

                mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                mFrontMenuButton.setVisible(true);
                mRearMenuButton.setVisible(false);

                if (controlFragment != null) {
                    controlFragment.animateFAB(-15, R.drawable.ic_camera_rear_white_36dp);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mCameraMode = false;
        getSupportActionBar().show();
    }

    @Override
    public void onCameraRequest() {

        CameraFragment cameraFragment = new CameraFragment();

        cameraFragment.setmCameraId(mCameraId);

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null) // Close the camera if the "back" button have been pressed
                .replace(R.id.fr_container, cameraFragment)
                .commit();

        getSupportActionBar().hide();

        mCameraMode = true;
    }

    private boolean hasFrontCamera() {

        int cameraCount = Camera.getNumberOfCameras();

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        while (cameraCount > 0) {

            cameraCount--;
            Camera.getCameraInfo(cameraCount, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return true;
            }
        }
        return false;
    }
}

