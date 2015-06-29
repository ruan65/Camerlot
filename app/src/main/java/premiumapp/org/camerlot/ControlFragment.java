package premiumapp.org.camerlot;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ControlFragment extends Fragment {

    interface CameraControlListener {
        void onCameraRequest();
    }

    private CameraControlListener mControlListener;

    public ControlFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_control, container, false);

        root.findViewById(R.id.btnCameraRequest).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mControlListener.onCameraRequest();
            }
        });
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
}
