package org.twintail.majvj;

import java.io.InputStream;

public interface MajVj {
    // Low level APIs
    public int createVertexShader(String shader);
    public int createVertexShader(InputStream stream);
    public int createFragmentShader(String shader);
    public int createFragmentShader(InputStream stream);
    public void deleteShader(int shader);
    public int createProgram(int vertexShader, int fragmentShader);
    public void deleteProgram(int program);

    // High level APIs
    public MajVjProgram createProgram();
}
