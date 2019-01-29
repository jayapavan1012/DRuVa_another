package com.nextinnovation.pt.barcodescanner.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.activity.BaseCameraActivity;
import com.nextinnovation.pt.barcodescanner.activity.MainActivity;

public class CameraFragment extends Fragment implements View.OnClickListener {
    private Button btnCapture ;
    private CaptureRequest captureRequest ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_camera, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCapture = view.findViewById(R.id.btnCapture);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        try {
            captureRequest = (CaptureRequest) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement retryConnectionListener");
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCapture :
                 //Pass the click event to activity to start the scanner .
                captureRequest.captureRequest();
                System.out.println("I am inside the case statement");
                break ;
        }
    }

    public interface CaptureRequest{
        void captureRequest();
    }

}
