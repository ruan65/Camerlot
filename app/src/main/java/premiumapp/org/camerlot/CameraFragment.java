package premiumapp.org.camerlot;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CameraFragment extends Fragment {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;

    private Camera mCamera;

    private int mCameraId;

    public CameraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_camera, container, false);

        mSurfaceView = (SurfaceView) root.findViewById(R.id.surface_view_in_fragment);

        mHolder = mSurfaceView.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        HolderCallback holderCallback = new HolderCallback();

        mHolder.addCallback(holderCallback);

        return root;
    }

    public static Camera getCameraInstance(Context ctx, int camIndex) {

        Camera camera = null;
        try {
            camera = Camera.open(camIndex);
        } catch (Exception e) {
            Toast.makeText(ctx, "Camera in use or doesn't exists", Toast.LENGTH_LONG).show();
        }
        return camera;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!getUserVisibleHint()) {
            return;
        }

        try {
            mCamera = getCameraInstance(getActivity(), mCameraId);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera in use or doesn't exists", Toast.LENGTH_LONG).show();
        }

        if (mCamera != null) {
            definePreview();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null) {

            mCamera.release();
            mCamera = null;
        }
    }

    private void definePreview() {

        Point displaySize = new Point();

        getActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);

        boolean landscapeMode = displaySize.x > displaySize.y;

        Camera.Size cameraSize = mCamera.getParameters().getPreviewSize();

        RectF rDisplay = new RectF();
        RectF rCamera = new RectF();

        rDisplay.set(0, 0, displaySize.x, displaySize.y);

        if (landscapeMode) {

            rCamera.set(0, 0, cameraSize.width, cameraSize.height);
        } else {
            rCamera.set(0, 0, cameraSize.height, cameraSize.width);
        }

        Matrix matrix = new Matrix();
        // I want full screen
        matrix.setRectToRect(rDisplay, rCamera, Matrix.ScaleToFit.START);
        matrix.invert(matrix);
        matrix.mapRect(rCamera);

        mSurfaceView.getLayoutParams().height = (int) rCamera.bottom;
        mSurfaceView.getLayoutParams().width = (int) rCamera.right;
    }

    private void defineOrientation(int cameraId) {

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int rotDegree = 0;

        switch (rotation) {

            case Surface.ROTATION_90:
                rotDegree = 90;
                break;
            case Surface.ROTATION_180:
                rotDegree = 180;
                break;
            case Surface.ROTATION_270:
                rotDegree = 270;
                break;
        }

        int calcDegree = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {

            calcDegree = (360 - rotDegree + info.orientation);

        } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {

            calcDegree = (360 - rotDegree - info.orientation);
            calcDegree += 360;
        }

        calcDegree %= 360;

        mCamera.setDisplayOrientation(calcDegree);
    }

    public void setmCameraId(int mCameraId) {
        this.mCameraId = mCameraId;
    }

    private class HolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            try {

                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();

            } catch (IOException | NullPointerException e) {
                Log.d(getClass().getName(), "Error setting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            if (holder.getSurface() == null) return;

            try {
                mCamera.stopPreview();

                defineOrientation(mCameraId);

                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (IOException | NullPointerException e) {
                Log.d(getClass().getName(), "Error starting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // ignore at this moment
        }
    }
}
