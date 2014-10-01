package org.twintail.majvj;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

final class MajVjRenderer implements Renderer, MajVj {

    private int mWidth;
    private int mHeight;
    private boolean mCreated = false;
    private MajVjClient mClient;
    private MajVj2DImpl m2D;
    private long mStartTime;
    private long mElapsedTime;
    private long mDeltaTime;

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
        mStartTime = System.currentTimeMillis();
        mElapsedTime = 0;
        mDeltaTime = 0;
        if (m2D != null)
            m2D.setWindowSize(mWidth, mHeight);
        if (!mCreated) {
            mClient.onCreated(this, mWidth, mHeight);
            mCreated = true;
        } else {
            mClient.onResized(mWidth, mHeight);
        }
        GLES20.glViewport(0, 0, mWidth, mHeight);
    }

    @Override
    public void onDrawFrame(GL10 notUsed) {
        long now = System.currentTimeMillis();
        long elapsedTime = now - mStartTime;
        mDeltaTime = elapsedTime - mElapsedTime;
        mElapsedTime = elapsedTime;
        mClient.onDraw();
        GLES20.glFlush();
    }

    @Override
    public long getElapsedTime() {
        return mElapsedTime;
    }

    @Override
    public long getDeltaTime() {
        return mDeltaTime;
    }

    @Override
    public void clearColorBuffer(float r, float g, float b, float a) {
        GLES20.glClearColor(r, g, b, a);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void clearDepthBuffer() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public FloatBuffer createFloatBuffer(int length) {
        return ByteBuffer.allocateDirect(length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    @Override
    public FloatBuffer createFloatBufferFrom(float[] values) {
        FloatBuffer buffer = createFloatBuffer(values.length);
        buffer.put(values).position(0);
        return buffer;
    }

    @Override
    public void setAlphaBlend(boolean enable, int src, int dst) {
        if (enable) {
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(src, dst);
            GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        } else {
            GLES20.glDisable(GLES20.GL_BLEND);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }
    }

    @Override
    public void setLineWidth(float width) {
        GLES20.glLineWidth(width);
    }

    @Override
    public MajVjProgram createProgram() {
        return new MajVjProgramImpl(this);
    }

    @Override
    public MajVj2D get2DInterface() {
        if (m2D == null) {
            m2D = new MajVj2DImpl(this);
            m2D.setWindowSize(mWidth, mHeight);
        }
        return m2D;
    }
}