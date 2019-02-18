package com.kongqw;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.FaceUtil;

import java.util.ArrayList;

/**
 * Created by kqw on 2016/7/13.
 * RobotCameraView
 */
public class ObjectDetectingView extends BaseCameraView {

    private static final String TAG = "ObjectDetectingView";
    private ArrayList<ObjectDetector> mObjectDetects;

    private MatOfRect mObject;

    @Override
    public void onOpenCVLoadSuccess() {
        Log.i(TAG, "onOpenCVLoadSuccess: ");

        mObject = new MatOfRect();

        mObjectDetects = new ArrayList<>();

    }

    @Override
    public void onOpenCVLoadFail() {
        Log.i(TAG, "onOpenCVLoadFail: ");
    }

    public ObjectDetectingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private Mat face1;
    private boolean first = true;

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        // 子线程（非UI线程）
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        for (ObjectDetector detector : mObjectDetects) {
            // 检测目标
            Rect[] object = detector.detectObject(mGray, mObject);
            for (Rect rect : object) {
                Imgproc.rectangle(mRgba, rect.tl(), rect.br(), detector.getRectColor(), 3);
                if (first) {
                    face1 = mRgba;
                    FaceUtil.saveImage(getContext(), mRgba, rect, "detect_face");
                    first = false;
                } else {
                    FaceUtil.saveImage(getContext(), mRgba, rect, "detect_face_1");
                }
            }

            if (onFaceDetectedListener != null && object.length != 0) {
                final Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
                post(new Runnable() {
                    @Override
                    public void run() {
                        onFaceDetectedListener.onFaceDetected(bitmap,mRgba);
                    }
                });
            }
        }
        return mRgba;
    }

    /**
     * 添加检测器
     *
     * @param detector 检测器
     */
    public synchronized void addDetector(ObjectDetector detector) {
        if (!mObjectDetects.contains(detector)) {
            mObjectDetects.add(detector);
        }
    }

    /**
     * 移除检测器
     *
     * @param detector 检测器
     */
    public synchronized void removeDetector(ObjectDetector detector) {
        if (mObjectDetects.contains(detector)) {
            mObjectDetects.remove(detector);
        }
    }

    private onFaceDetectedListener onFaceDetectedListener;

    public interface onFaceDetectedListener {
        void onFaceDetected(Bitmap bitmap, Mat mRgba);
    }

    public void setOnFaceDetectedListener(onFaceDetectedListener onFaceDetectedListener) {
        this.onFaceDetectedListener = onFaceDetectedListener;
    }

    public void removeFaceDetectListener() {
        onFaceDetectedListener = null;
    }
}
