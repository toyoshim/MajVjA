package org.twintail.majvj;

public interface MajVj {
    public long getElapsedTime();
    public long getDeltaTime();
    public java.nio.FloatBuffer createFloatBuffer(int length);
    public java.nio.FloatBuffer createFloatBufferFrom(float[] values);
    public void clearColorBuffer(float r, float g, float b, float a);
    public void clearDepthBuffer();
    public MajVjProgram createProgram();
    public MajVj2D get2DInterface();
}
