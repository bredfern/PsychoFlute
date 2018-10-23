package com.jacknice.psychoflute2;

import java.io.File;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.csounds.CsoundObj;
import com.csounds.CsoundObjListener;
import com.csounds.bindings.CsoundBinding;
import com.csounds.examples.BaseCsoundActivity;
import com.jacknice.psychoflute2.R;

import csnd6.CsoundMYFLTArray;
import csnd6.controlChannelType;

public class MainActivity extends BaseCsoundActivity implements
        CsoundObjListener, CsoundBinding {

    public View multiTouchView;

    int touchIds[] = new int[5];
    float touchX[] = new float[5];
    float touchY[] = new float[5];
    CsoundMYFLTArray touchXPtr[] = new CsoundMYFLTArray[5];
    CsoundMYFLTArray touchYPtr[] = new CsoundMYFLTArray[5];

    protected int getTouchIdAssignment() {
        for (int i = 0; i < touchIds.length; i++) {
            if (touchIds[i] == -1) {
                return i;
            }
        }
        return -1;
    }

    protected int getTouchId(int touchId) {
        for (int i = 0; i < touchIds.length; i++) {
            if (touchIds[i] == touchId) {
                return i;
            }
        }
        return -1;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < touchIds.length; i++) {
            touchIds[i] = -1;
            touchX[i] = -1;
            touchY[i] = -1;
        }

        multiTouchView = new View(this);
        multiTouchView.setBackgroundResource(R.drawable.bg);

        multiTouchView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction() & MotionEvent.ACTION_MASK;
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:

                        for (int i = 0; i < event.getPointerCount(); i++) {
                            int pointerId = event.getPointerId(i);
                            int id = getTouchId(pointerId);

                            if (id == -1) {

                                id = getTouchIdAssignment();

                                if (id != -1) {
                                    touchIds[id] = pointerId;
                                    touchX[id] = event.getX(i)
                                            / multiTouchView.getWidth();
                                    touchY[id] = 1 - (event.getY(i) / multiTouchView
                                            .getHeight());

                                    if (touchXPtr[id] != null) {
                                        touchXPtr[id].SetValue(0, touchX[id]);
                                        touchYPtr[id].SetValue(0, touchY[id]);

                                        csoundObj.sendScore(String.format("i1.%d 0 -2 %d", id, id));

                                    }
                                }
                            }

                        }

                        break;
                    case MotionEvent.ACTION_MOVE:

                        for (int i = 0; i < event.getPointerCount(); i++) {
                            int pointerId = event.getPointerId(i);
                            int id = getTouchId(pointerId);

                            if (id != -1) {
                                touchX[id] = event.getX(i)
                                        / multiTouchView.getWidth();
                                touchY[id] = 1 - (event.getY(i) / multiTouchView
                                        .getHeight());
                            }

                            double Rand = Math.random();
                            int num = ((int) Rand);
                            num = num *100;
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP: {
                        int activePointerIndex = event.getActionIndex();
                        int pointerId = event.getPointerId(activePointerIndex);

                        int id = getTouchId(pointerId);
                        if (id != -1) {
                            touchIds[id] = -1;
                            csoundObj.sendScore(String.format("i-1.%d 0 0 %d", id, id));
                        }

                    }
                    break;
                }
                return true;
            }

        });

        setContentView(multiTouchView);

        String csd = getResourceFileAsString(R.raw.multitouch_xy);
        File f = createTempFile(csd);

        csoundObj.addBinding(this);

        csoundObj.startCsound(f);
    }


    public void csoundObjStarted(CsoundObj csoundObj) {}

    public void csoundObjCompleted(CsoundObj csoundObj) {}

    // VALUE CACHEABLE

    public void setup(CsoundObj csoundObj) {
        for (int i = 0; i < touchIds.length; i++) {
            touchXPtr[i] = csoundObj.getInputChannelPtr(
                    String.format("touch.%d.x", i),
                    controlChannelType.CSOUND_CONTROL_CHANNEL);
            touchYPtr[i] = csoundObj.getInputChannelPtr(
                    String.format("touch.%d.y", i),
                    controlChannelType.CSOUND_CONTROL_CHANNEL);
        }
    }

    public void updateValuesToCsound() {
        for (int i = 0; i < touchX.length; i++) {
            touchXPtr[i].SetValue(0, touchX[i]);
            touchYPtr[i].SetValue(0, touchY[i]);
        }

    }

    public void updateValuesFromCsound() {
    }

    public void cleanup() {
        for (int i = 0; i < touchIds.length; i++) {
            touchXPtr[i].Clear();
            touchXPtr[i] = null;
            touchYPtr[i].Clear();
            touchYPtr[i] = null;
        }
    }

}