package org.twintail.majvj;

import android.renderscript.Float2;

public class MajVjCoordinateHelper {

    private int mWidth;
    private int mHeight;
    private int mShortSize;
    private int mLongSize;
    private float mHalfWidth;
    private float mHalfHeight;
    private float mHalfShortSize;
    private float mHalfLongSize;

    public void setWindowSize(int width, int height) {
        mWidth = width;
        mHeight = height;
        mShortSize = Math.min(width, height);
        mLongSize = Math.max(width, height);
        mHalfWidth = (width - 1) / 2;
        mHalfHeight = (height - 1) / 2;
        mHalfShortSize = (mShortSize - 1) / 2;
        mHalfLongSize = (mLongSize - 1) / 2;
    }

    public int getWindowWidth() {
        return mWidth;
    }

    public int getWindowHeight() {
        return mHeight;
    }

    public int getShortSize() {
        return mShortSize;
    }

    public int getLongSize() {
        return mLongSize;
    }

    public Float2 Window(Float2 position) {
        // (0, 0) - (width - 1, height - 1) => (-1, -1) - (1, 1)
        position.x = position.x / mHalfWidth - 0.5f;
        position.y = position.y / mHalfHeight - 0.5f;
        return position;
    }

    public Float2 Short(Float2 position) {
        // (0, 0) - (min(width, height) - 1, min(width, height) - 1) => (-1, -1) - (1, 1)
        position.x = position.x / mHalfShortSize - 0.5f;
        position.y = position.y / mHalfShortSize - 0.5f;
        return position;
    }

    public Float2 Long(Float2 position) {
        // (0, 0) - (max(width, height) - 1, max(width, height) - 1) => (-1, -1) - (1, 1)
        position.x = position.x / mHalfLongSize - 0.5f;
        position.y = position.y / mHalfLongSize - 0.5f;
        return position;
    }
}
