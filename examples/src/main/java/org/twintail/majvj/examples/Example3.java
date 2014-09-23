package org.twintail.majvj.examples;

import android.util.Log;

import org.twintail.majvj.MajVj;
import org.twintail.majvj.MajVjActivity;
import org.twintail.majvj.MajVjClient;
import org.twintail.majvj.MajVjProgram;

import java.nio.FloatBuffer;

public class Example3 extends MajVjActivity implements MajVjClient {

    private String TAG = "Example3";
    private MajVj mMv;
    private MajVjProgram mMvp;

    protected MajVjClient createClient() {
        return this;
    }

    public void onCreated(MajVj mv, int width, int height) {
        mMv = mv;
        mMvp = mv.createProgram();
        if (mMvp.link(getString(R.string.vec4CoordThroughVertexShader),
                      getString(R.string.fillRedFragmentShader)))
            Log.i(TAG, "MajVjProgram succeeded to link shaders.");

        float[] coords = {
                 0f,  1f, 1f, 1f,
                 1f, -1f, 1f, 1f,
                -1f, -1f, 1f, 1f
        };
        FloatBuffer coordBuffer = mMv.createFloatBufferFrom(coords);
        if (mMvp.setVertexAttributeBuffer("aCoord", 4, coordBuffer))
            Log.i(TAG, "aCoord is set.");
        else
            Log.e(TAG, "setVertexAttributeFloatBuffer failed.");
    }

    public void onResized(int width, int height) {
    }

    public void onDraw() {
        mMv.clearColorBuffer(1f, 1f, 1f, 1f);
        mMv.clearDepthBuffer();
        mMvp.drawArrays(MajVjProgram.TRIANGLES, 0, 3);
    }
}
