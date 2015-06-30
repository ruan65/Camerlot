package premiumapp.org.camerlot;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;

public class ControlFragment extends Fragment {

    interface CameraControlListener {
        void onCameraRequest();
    }

    private CameraControlListener mControlListener;

    private FloatingActionButton mBtnCameraRequest;

    private int iconImageResource = -1;

    public ControlFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_control, container, false);

        root.findViewById(R.id.btn_camera_request).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mControlListener.onCameraRequest();
            }
        });

        mBtnCameraRequest = (FloatingActionButton) root.findViewById(R.id.btn_camera_request);

        if (iconImageResource != -1) {
            mBtnCameraRequest.setImageResource(iconImageResource);
        }
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mControlListener = (CameraControlListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() +
                    "must implement ControlFragment.CameraControlListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mControlListener = null;
    }

    public void animateFAB(int trans, int iconId) {

        iconImageResource = iconId;

        mBtnCameraRequest.setImageResource(iconId);

        mBtnCameraRequest.animate()
                .translationY(trans)
                .setDuration(700)
                .setInterpolator(new BounceInterpolator())
                .start();
    }
}
