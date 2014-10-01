package org.twintail.majvj;

public interface MajVj {
    public static final int ZERO = android.opengl.GLES20.GL_ZERO;
    public static final int ONE = android.opengl.GLES20.GL_ONE;
    public static final int SRC_COLOR = android.opengl.GLES20.GL_SRC_COLOR;
    public static final int ONE_MINUS_SRC_COLOR = android.opengl.GLES20.GL_ONE_MINUS_SRC_COLOR;
    public static final int DST_COLOR = android.opengl.GLES20.GL_DST_COLOR;
    public static final int ONE_MINUS_DST_COLOR = android.opengl.GLES20.GL_ONE_MINUS_DST_COLOR;
    public static final int SRC_ALPHA = android.opengl.GLES20.GL_SRC_ALPHA;
    public static final int ONE_MINUS_SRC_ALPHA = android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
    public static final int DST_ALPHA = android.opengl.GLES20.GL_DST_ALPHA;
    public static final int ONE_MINUS_DST_ALPHA = android.opengl.GLES20.GL_ONE_MINUS_DST_ALPHA;
    public static final int CONSTANT_COLOR = android.opengl.GLES20.GL_CONSTANT_COLOR;
    public static final int ONE_MINUS_CONSTANT_COLOR = android.opengl.GLES20.GL_ONE_MINUS_CONSTANT_COLOR;
    public static final int CONSTANT_ALPHA = android.opengl.GLES20.GL_CONSTANT_ALPHA;
    public static final int ONE_MINUS_CONSTANT_ALPHA = android.opengl.GLES20.GL_ONE_MINUS_CONSTANT_ALPHA;
    public static final int SRC_ALPHA_SATURATE = android.opengl.GLES20.GL_SRC_ALPHA_SATURATE;

    public long getElapsedTime();
    public long getDeltaTime();

    public void clearColorBuffer(float r, float g, float b, float a);
    public void clearDepthBuffer();
    public void setAlphaBlend(boolean enable, int src, int dst);
    public void setLineWidth(float width);

    public java.nio.FloatBuffer createFloatBuffer(int length);
    public java.nio.FloatBuffer createFloatBufferFrom(float[] values);

    public MajVjProgram createProgram();

    public MajVj2D get2DInterface();
}
