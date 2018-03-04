package com.example.fifol.tohelp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanBarCode extends android.support.v4.app.Fragment {

    SurfaceView cameraView;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;
    Button btnCloseFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bar_scanner_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCloseFrag = view.findViewById(R.id.closeBtn);
        btnCloseFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(ScanBarCode.this);
                fragmentTransaction.commit();
            }
        });
        barcode = new BarcodeDetector.Builder(getActivity()).setBarcodeFormats(Barcode.EAN_13).build();
        if (!barcode.isOperational()) {
            Toast.makeText(getActivity(), "מצטערים אין תמיכה בסריקת הברקוד.", Toast.LENGTH_LONG).show();
            finishFragment(null);
        }
        cameraSource = new CameraSource.Builder(getActivity(), barcode).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedFps(24).setAutoFocusEnabled(true).setRequestedPreviewSize(1920, 1024).build();
        new AlertDialog.Builder(getActivity()).setTitle("סרוק את הברקוד על המוצר.").show();
        setCameraSetting();
        identfiyBarCode();
    }

    //Set camera setting and permission.
    private void setCameraSetting() {
        cameraView = getView().findViewById(R.id.cameraView);
        cameraView.setVisibility(View.VISIBLE);
        cameraView.setZOrderMediaOverlay(true);
        holder = cameraView.getHolder();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraView.getHolder());
                    }else {
                        finishFragment(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            }
        });
    }

    //When found barCode pass data to pickProductActivity and finish .
    private void identfiyBarCode() {
        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCodes = detections.getDetectedItems();
                if (barCodes.size() > 0) {
                    Barcode barcode = barCodes.valueAt(0);
                    Log.d("Result Barcode: ",barcode.displayValue);
                    finishFragment(barcode);
                }
            }
        });
    }
    public void finishFragment(Barcode barcode) {
        //Todo- fix consoume battery power ,may caused by camera still opened.
        if (getActivity() != null) {
            ((MyProductList)getActivity()).getBarCode(barcode);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(ScanBarCode.this);
            fragmentTransaction.commit();
            cameraSource.release();

        }
    }

}
