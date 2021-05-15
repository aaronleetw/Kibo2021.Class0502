package com.aaron.class0502;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Map;
enum MyMethod {
    sharpenImage, blurImage, createNewImage
}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!OpenCVLoader.initDebug())  Log.d("kibo","opencv init error");
        else                            Log.d("kibo","opencv init OK");
//        int[] bgr = {1,2,3}; // blue, green, red

        Button  btn = findViewById(R.id.button),
                blurBtn = findViewById(R.id.blurButton),
                sharpenBtn = findViewById(R.id.sharpenBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actMethod(MyMethod.createNewImage);
            }
        });
        blurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actMethod(MyMethod.blurImage);
            }
        });
        sharpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actMethod(MyMethod.sharpenImage);
            }
        });
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        Mat src = new Mat();
        Utils.bitmapToMat(bMap, src);
        double[] bgr = src.get(200,200);
        Log.d("kibo", ".\nb:" + bgr[0] + "\ng:" + bgr[1] + "\nr:" + bgr[2]);
    }

    void actMethod(MyMethod method) {
        switch (method) {
            case sharpenImage:
                sharpenImage();
                break;
            case blurImage:
                blurImage();
                break;
            case createNewImage:
                createNewImage();
                break;
        }
    }

    private void sharpenImage() {
        ImageView imgV = findViewById(R.id.imageView);
        // get matrix on ImageView
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        Mat src = new Mat();
        Utils.bitmapToMat(bMap, src);

        // sharpen
        Mat kernel = new Mat(3,3, CvType.CV_16SC1);
        kernel.put(0,0,0,-1,0,-1,5,-1,0,-1,0);
        Imgproc.filter2D(src, src, src.depth(), kernel);

        // put back
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bitmap);
        imgV.setImageBitmap(bitmap);
    }

    private void blurImage() {
        ImageView imgV = findViewById(R.id.imageView);
        // get matrix on ImageView
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        Mat src = new Mat();
        Utils.bitmapToMat(bMap, src);
        // blur
        Imgproc.GaussianBlur(src, src, new Size(55,55), 0);
        // put matrix on ImageView
        Bitmap bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bitmap);
        imgV.setImageBitmap(bitmap);
    }

    private void createNewImage() {
        ImageView imgV = findViewById(R.id.imageView);
        int width = 120, height = 120;
        Mat mat = new Mat(width, height, CvType.CV_8UC3);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                double [] bgr = {0, i+j, 0};
                mat.put(i, j, bgr);
            }
        Bitmap bitmap = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
        imgV.setImageBitmap(bitmap);
    }
}
