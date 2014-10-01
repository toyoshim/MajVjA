package org.twintail.majvj;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.util.Random;

final class MajVj2DImpl implements MajVj2D {

    private final String TAG = "MajVj2DImpl";

    private final String basicVertexShader =
            "precision mediump float;\n" +
            "attribute vec2 aCoord;\n" +
            "uniform vec2 uPosition;\n" +
            "uniform vec2 uSize;\n" +
            "void main() {\n" +
            "  vec2 position = uPosition + aCoord * uSize;\n" +
            "  gl_Position = vec4(position, 1.0, 1.0);\n" +
            "}";

    private final String basicFragmentShader =
            "precision mediump float;\n" +
            "uniform vec4 uColor;\n" +
            "void main() {\n" +
            "  gl_FragColor = uColor;\n" +
            "}";

    private int mEllipseResolution = 64;
    private int mHeight;
    private int mWidth;
    private float mDivX;
    private float mDivY;
    private float mStrokeWeight = 1.0f;
    private float mSubX;
    private float mSubY;
    private float[] mBackground = { 0.8f, 0.8f, 0.8f, 1.0f };
    private float[] mBasePosition = { 0.0f, 0.0f };
    private float[] mBaseSize = { 1.0f, 1.0f };
    private float[] mDivColor = { 255.0f, 255.0f, 255.0f, 255.0f };
    private float[] mFill = { 1.0f, 1.0f, 1.0f, 1.0f };
    private float[] mStroke = { 1.0f, 1.0f, 1.0f, 1.0f };
    private float[] mPosition = { 0.0f, 0.0f };
    private float[] mSize = { 1.0f, 1.0f };
    private FloatBuffer mEllipseCoords;
    private FloatBuffer mLineBuffer;
    private MajVj mMv;
    private MajVjProgram mBasicProgram;
    private Random mRandom;

    public MajVj2DImpl(MajVj mv) {
        mMv = mv;
        mBasicProgram = mMv.createProgram();
        mBasicProgram.loadAndLink(basicVertexShader, basicFragmentShader);
        mLineBuffer = mMv.createFloatBuffer(4);
        mRandom = new Random();

        mEllipseCoords = mMv.createFloatBuffer(mEllipseResolution * 2 + 2);
        mEllipseCoords.put(0f);
        mEllipseCoords.put(0f);
        for (int i = 0; i < mEllipseResolution; ++i) {
            mEllipseCoords.put((float)Math.cos(Math.PI * 2 * i / (mEllipseResolution - 1)));
            mEllipseCoords.put((float)Math.sin(Math.PI * 2 * i / (mEllipseResolution - 1)));
        }
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
    public void strokeWeight(float weight) {
        mStrokeWeight = weight;
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
    public void fill(float gray) {
        fill(gray, gray, gray);
    }
    @Override
    public void fill(float gray, float alpha) {
        fill(gray, gray, gray, alpha);
    }

    @Override
    public void fill(float v1, float v2, float v3) {
        mFill[0] = V1(v1);
        mFill[1] = V2(v2);
        mFill[2] = V3(v3);
    }

    @Override
    public void fill(float v1, float v2, float v3, float alpha) {
        fill(v1, v2, v3);
        mFill[3] = Alpha(alpha);
    }

    @Override
    public void stroke(float v1, float v2, float v3, float alpha) {
        stroke(v1, v2, v3);
        mStroke[3] = Alpha(alpha);
    }

    @Override
    public void line(float x1, float y1, float x2, float y2) {
        GLES20.glLineWidth(mStrokeWeight);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mLineBuffer.put(X(x1));
        mLineBuffer.put(Y(y1));
        mLineBuffer.put(X(x2));
        mLineBuffer.put(Y(y2));
        mLineBuffer.position(0);
        mBasicProgram.setVertexAttributeBuffer("aCoord", 2, mLineBuffer);
        mBasicProgram.setUniform("uSize", mBaseSize);
        mBasicProgram.setUniform("uPosition", mBasePosition);
        mBasicProgram.setUniform("uColor", mStroke);
        mBasicProgram.drawArrays(MajVjProgram.LINES, 0, 2);
    }

    @Override
    public void ellipse(float x, float y, float width, float height) {
        GLES20.glLineWidth(mStrokeWeight);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mEllipseCoords.position(0);
        mBasicProgram.setVertexAttributeBuffer("aCoord", 2, mEllipseCoords);
        mSize[0] = Width(width);
        mSize[1] = Height(height);
        mBasicProgram.setUniform("uSize", mSize);
        mPosition[0] = X(x);
        mPosition[1] = Y(y);
        mBasicProgram.setUniform("uPosition", mPosition);
        mBasicProgram.setUniform("uColor", mFill);
        mBasicProgram.drawArrays(MajVjProgram.TRIANGLE_FAN, 0, mEllipseResolution + 1);

        mEllipseCoords.position(2);
        mBasicProgram.setVertexAttributeBuffer("aCoord", 2, mEllipseCoords);
        mBasicProgram.setUniform("uColor", mStroke);
        mBasicProgram.drawArrays(MajVjProgram.LINE_LOOP, 0, mEllipseResolution);
    }


    private float X(float x) {
        return x / mDivX - mSubX;
    }

    private float Y(float y) {
        return y / mDivY - mSubY;
    }

    private float Width(float width) {
        return width / mWidth;
    }

    private float Height(float height) {
        return height / mHeight;
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
