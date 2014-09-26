package org.twintail.majvj;

public interface MajVjProgram {
    public static final int POINTS = android.opengl.GLES20.GL_POINTS;
    public static final int LINES = android.opengl.GLES20.GL_LINES;
    public static final int LINE_STRIP = android.opengl.GLES20.GL_LINE_STRIP;
    public static final int LINE_LOOP = android.opengl.GLES20.GL_LINE_LOOP;
    public static final int TRIANGLES = android.opengl.GLES20.GL_TRIANGLES;
    public static final int TRIANGLE_STRIP = android.opengl.GLES20.GL_TRIANGLE_STRIP;
    public static final int TRIANGLE_FAN = android.opengl.GLES20.GL_TRIANGLE_FAN;

    public void shutdown();
    public boolean loadVertexShader(String shader);
    public boolean loadFragmentShader(String shader);
    public boolean link();
    public boolean loadAndLink(String vertexShader, String fragmentShader);
    public boolean setVertexAttributeBuffer(String name, int dimension, java.nio.Buffer buffer);
    public boolean setVertexAttribute(String name, int dimension, float[] values);
    public boolean setUniform(String name, float value);
    public boolean setUniform(String name, float[] values);
    public void drawArrays(int mode, int first, int count);
}
