package org.twintail.majvj;

import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MajVjRenderer implements Renderer, MajVj {

    private int mWidth;
    private int mHeight;
    private boolean mCreated = false;
    private MajVjClient mClient;

    public MajVjRenderer(MajVjClient client) {
        mClient = client;
    }

    @Override
    public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 notUsed, int width, int height) {
        if (mCreated && mWidth == width && mHeight == height)
            return;
        mWidth = width;
        mHeight = height;
        if (!mCreated) {
            mClient.onCreated(this, mWidth, mHeight);
            mCreated = true;
        } else {
            mClient.onResized(mWidth, mHeight);
        }
    }

    @Override
    public void onDrawFrame(GL10 notUsed) {
        mClient.onDraw();
    }

   @Override
    public MajVjProgram createProgram() {
        return new MajVjProgram();
    }
}