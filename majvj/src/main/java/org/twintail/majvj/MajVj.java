package org.twintail.majvj;

import java.io.InputStream;

public interface MajVj {
    public int createVertexShader(String shader);
    public int createVertexShader(InputStream stream);
    public int createFragmentShader(String shader);
    public int createFragmentShader(InputStream stream);
    public int createProgram(String vertexShader, String fragmentShader);
    public int createProgram(InputStream vertexShader, InputStream fragmentShader);
    public int createProgram(int vertexShader, int fragmentShader);
    // TODO: provide deleteShader and deleteProgram.
}
