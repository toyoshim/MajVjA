package org.twintail.majvj.examples;

import android.util.Log;

import org.twintail.majvj.MajVj;
import org.twintail.majvj.MajVjActivity;
import org.twintail.majvj.MajVjClient;
import org.twintail.majvj.MajVjProgram;

public class Example4 extends MajVjActivity implements MajVjClient {

    private final String TAG = "Example4";
    private MajVj mMv;
    private MajVjProgram mMvp;
    private int mWidth;
    private int mHeight;

    protected MajVjClient createClient() {
        return this;
    }

    public void onCreated(MajVj mv, int width, int height) {
        mMv = mv;
        mMvp = mv.createProgram();
        mMvp.loadAndLink(readAssetAsString("shaders/glslSandbox.vs"),
                         readAssetAsString("shaders/church.fs"));
        final float[] coords = {
                -1f, -1f,
                 1f, -1f,
                 1f,  1f,
                -1f,  1f
        };
        mMvp.setVertexAttribute("aCoord", 2, coords);
        mMvp.setUniform("time", 0f);
        final float[] mouse = { 0.5f, 0.5f };
        mMvp.setUniform("mouse", mouse);
        onResized(width, height);

        mMv.clearDepthBuffer();
    }

    public void onResized(int width, int height) {
        mWidth = width;
        mHeight = height;
        float[] resolution = { mWidth, mHeight };
        mMvp.setUniform("resolution", resolution);
        Log.i(TAG, "resize: " + mWidth + "x" + mHeight);
    }

    public void onDraw() {
        mMvp.setUniform("time", mMv.getElapsedTime() / 1000f);
        mMvp.drawArrays(MajVjProgram.TRIANGLE_FAN, 0, 4);
    }
}
