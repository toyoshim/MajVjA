package org.twintail.majvj;

import android.renderscript.Float2;

public class MajVjCoordinateHelper {

    private int mWidth;
    private int mHeight;
    private float mHalfWidth;
    private float mHalfHeight;
    private float mRatioX;
    private float mRatioY;

    public void setWindowSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        mHalfWidth = (width - 1) / 2;
        mHalfHeight = (height - 1) / 2;
        float aspect = width / height;
        if (aspect > 1) {
            mRatioX = aspect;
            mRatioY = 1f;
        } else {
            mRatioX = 1f;
            mRatioY = 1f / aspect;
        }
    }

    public int getWindowWidth() {
        return mWidth;
    }

    public int getWindowHeight() {
        return mHeight;
    }

    public Float2 Window(Float2 position) {
        position.x = position.x / mHalfWidth - 0.5f;
        position.y = position.y / mHalfHeight - 0.5f;
        return position;
    }

    public Float2 InnerSquare(Float2 position) {
        position.x = position.x / mRatioX;
        position.y = position.y / mRatioY;
        return position;
    }

    public Float2 OuterSquare(Float2 position) {
        position.x = position.x * mRatioX;
        position.y = position.y * mRatioY;
        return position;
    }
}
