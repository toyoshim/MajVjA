package org.twintail.majvj;

import java.nio.FloatBuffer;

public interface MajVj {
    public FloatBuffer createFloatBuffer(int length);
    public FloatBuffer createFloatBufferFrom(float[] values);
    public void clearColorBuffer(float r, float g, float b, float a);
    public void clearDepthBuffer();
    public MajVjProgram createProgram();
}
