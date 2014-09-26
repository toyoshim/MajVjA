package org.twintail.majvj;

import java.nio.FloatBuffer;
import java.util.Random;

final class MajVj2DImpl implements MajVj2D {

    private final String TAG = "MajVj2DImpl";
    private MajVj mMv;
    private int mWidth;
    private int mHeight;
    private float mDivX;
    private float mSubX;
    private float mDivY;
    private float mSubY;
    private Random mRandom;
    private MajVjProgram mBasicProgram;
    private FloatBuffer mLineBuffer;
    private float[] mBackground = { 0.8f, 0.8f, 0.8f, 1.0f };
    private float[] mStroke = { 1.0f, 1.0f, 1.0f, 1.0f };
    private float[] mDivColor = { 255.0f, 255.0f, 255.0f, 255.0f };

    private final String basicVertexShader =
            "precision mediump float;\n" +
            "attribute vec2 aCoord;\n" +
            "void main() {\n" +
            "  gl_Position = vec4(aCoord, 1.0, 1.0);\n" +
            "}";
    private final String basicFragmentShader =
            "precision mediump float;\n" +
            "uniform vec4 uColor;\n" +
            "void main() {\n" +
            "  gl_FragColor = uColor;\n" +
            "}";

    public MajVj2DImpl(MajVj mv) {
        mMv = mv;
        mBasicProgram = mMv.createProgram();
        mBasicProgram.loadAndLink(basicVertexShader, basicFragmentShader);
        mLineBuffer = mMv.createFloatBuffer(4);
        mRandom = new Random();
    }

    public void setWindowSize(int width, int height) {
        mWidth = width;
        mHeight = height;

        mDivX = (mWidth - 1) / 2;
        mSubX = 1.0f;
        mDivY = (mHeight - 1) / 2;
        mSubY = 1.0f;
    }

    @Override
    public float random(float high) {
        return mRandom.nextFloat() * high;
    }

    @Override
    public void background(float gray) {
        background(gray, gray, gray);
    }

    @Override
    public void background(float gray, float alpha) {
        background(gray, gray, gray, alpha);
    }

    @Override
    public void background(float v1, float v2, float v3) {
        mBackground[0] = V1(v1);
        mBackground[1] = V2(v2);
        mBackground[2] = V3(v3);
        mMv.clearDepthBuffer();
        mMv.clearColorBuffer(mBackground[0], mBackground[1], mBackground[2], mBackground[3]);
    }

    @Override
    public void background(float v1, float v2, float v3, float alpha) {
        mBackground[3] = Alpha(alpha);
        background(v1, v2, v3);
    }

    @Override
    public void stroke(float gray) {
        stroke(gray, gray, gray);
    }

    @Override
    public void stroke(float gray, float alpha) {
        stroke(gray, gray, gray, alpha);
    }

    @Override
    public void stroke(float v1, float v2, float v3) {
        mStroke[0] = V1(v1);
        mStroke[1] = V2(v2);
        mStroke[2] = V3(v3);
    }

    @Override
    public void stroke(float v1, float v2, float v3, float alpha) {
        stroke(v1, v2, v3);
        mStroke[3] = Alpha(alpha);
    }

    @Override
    public void line(float x1, float y1, float x2, float y2) {
        mLineBuffer.put(X(x1));
        mLineBuffer.put(Y(y1));
        mLineBuffer.put(X(x2));
        mLineBuffer.put(Y(y2));
        mLineBuffer.position(0);
        mBasicProgram.setVertexAttributeBuffer("aCoord", 2, mLineBuffer);
        mBasicProgram.setUniform("uColor", mStroke);
        mBasicProgram.drawArrays(MajVjProgram.LINES, 0, 2);
    }

    private float X(float x) {
        return x / mDivX - mSubX;
    }

    private float Y(float y) {
        return y / mDivY - mSubY;
    }

    private float V1(float v1) {
        return v1 / mDivColor[0];
    }

    private float V2(float v2) {
        return v2 / mDivColor[1];
    }

    private float V3(float v3) {
        return v3 / mDivColor[2];
    }

    private float Alpha(float alpha) {
        return alpha / mDivColor[3];
    }
}
