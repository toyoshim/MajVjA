package org.twintail.majvj;

import java.io.InputStream;

public class MajVjProgram {

    private MajVj mMv;
    private int mVertexShader;
    private int mFragmentShader;

    public MajVjProgram(MajVj mv) {
        mMv = mv;
    }

    public void shutdown() {
        setVertexShader(0);
        setFragmentShader(0);
    }

    private void setVertexShader(int id) {
        if (mVertexShader != 0)
            mMv.deleteShader(mVertexShader);
        this.mVertexShader = id;
    }

    private void setFragmentShader(int id) {
        if (mFragmentShader != 0)
            mMv.deleteShader(mFragmentShader);
        this.mFragmentShader = id;
    }

    public boolean loadVertexShader(String shader) {
        int id = mMv.createVertexShader(shader);
        setVertexShader(id);
        return id != 0;
    }

    public boolean loadVertexShader(InputStream shader) {
        int id = mMv.createVertexShader(shader);
        setVertexShader(id);
        return id != 0;
    }

    public boolean loadFragmentShader(String shader) {
        int id = mMv.createFragmentShader(shader);
        setFragmentShader(id);
        return id != 0;
    }

    public boolean loadFragmentShader(InputStream shader) {
        int id = mMv.createFragmentShader(shader);
        setFragmentShader(id);
        return id != 0;
    }

    public boolean loadShaders(String vertexShader, String fragmentShader) {
        return loadVertexShader(vertexShader) && loadFragmentShader(fragmentShader);
    }

    public boolean loadShaders(InputStream vertexShader, InputStream fragmentShader) {
        return loadVertexShader(vertexShader) && loadFragmentShader(fragmentShader);
    }
}
