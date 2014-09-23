package org.twintail.majvj;

import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MajVjProgram {

    private String TAG = "MajVjProgram";
    private int mVertexShader;
    private int mFragmentShader;
    private int mProgram;

    public MajVjProgram() {
    }

    public void shutdown() {
        setVertexShader(0);
        setFragmentShader(0);
        if (mProgram != 0) {
            GLES20.glDeleteProgram(mProgram);
            mProgram = 0;
        }
    }

    public boolean loadVertexShader(String shader) {
        int id = createVertexShader(shader);
        setVertexShader(id);
        return id != 0;
    }

    public boolean loadVertexShader(InputStream shader) {
        int id = createVertexShader(shader);
        setVertexShader(id);
        return id != 0;
    }

    public boolean loadFragmentShader(String shader) {
        int id = createFragmentShader(shader);
        setFragmentShader(id);
        return id != 0;
    }

    public boolean loadFragmentShader(InputStream shader) {
        int id = createFragmentShader(shader);
        setFragmentShader(id);
        return id != 0;
    }

    public boolean link(String vertexShader, String fragmentShader) {
        return loadVertexShader(vertexShader) && loadFragmentShader(fragmentShader) && link();
    }

    public boolean link(InputStream vertexShader, InputStream fragmentShader) {
        return loadVertexShader(vertexShader) && loadFragmentShader(fragmentShader) && link();
    }

    public boolean link(String vertexShader, InputStream fragmentShader) {
        return loadVertexShader(vertexShader) && loadFragmentShader(fragmentShader) && link();
    }

    public boolean link(InputStream vertexShader, String fragmentShader) {
        return loadVertexShader(vertexShader) && loadFragmentShader(fragmentShader) && link();
    }

    public boolean link() {
        if (mProgram != 0)
            GLES20.glDeleteProgram(mProgram);

        int id = GLES20.glCreateProgram();
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR || id == 0) {
            Log.e(TAG, "glCreateProgram failed");
            return false;
        }

        GLES20.glAttachShader(id, mVertexShader);
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
            GLES20.glDeleteProgram(id);
            Log.e(TAG, "glAttachShader failed on a vertex shader");
            return false;
        }

        GLES20.glAttachShader(id, mFragmentShader);
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
            GLES20.glDeleteProgram(id);
            Log.e(TAG, "glAttachShader failed on a fragment shader");
            return false;
        }

        GLES20.glLinkProgram(id);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(id, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == GLES20.GL_TRUE) {
            mProgram = id;
            return true;
        }

        String log = GLES20.glGetProgramInfoLog(id);
        Log.e(TAG, "Program link failed: " + log);
        GLES20.glDeleteProgram(id);
        return false;
    }

    private int createShader(int type, String shader) {
        int id = GLES20.glCreateShader(type);
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR || id == 0) {
            Log.e(TAG, "glCreateShader failed.");
            return 0;
        }

        GLES20.glShaderSource(id, shader);
        GLES20.glCompileShader(id);
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == GLES20.GL_TRUE)
            return id;

        String log = GLES20.glGetShaderInfoLog(id);
        Log.e(TAG, "Shader compilation failed: " + log);
        deleteShader(id);
        return 0;
    }

    private int createShader(int type, InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null)
                builder.append(line);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return 0;
        }
        return createShader(type, builder.toString());
    }

    private int createVertexShader(String shader) {
        return createShader(GLES20.GL_VERTEX_SHADER, shader);
    }

    private int createVertexShader(InputStream stream) {
        return createShader(GLES20.GL_VERTEX_SHADER, stream);
    }

    private int createFragmentShader(String shader) {
        return createShader(GLES20.GL_FRAGMENT_SHADER, shader);
    }

    private int createFragmentShader(InputStream stream) {
        return createShader(GLES20.GL_FRAGMENT_SHADER, stream);
    }

    private void deleteShader(int shader) {
        GLES20.glDeleteShader(shader);
    }

    private void setVertexShader(int id) {
        if (mVertexShader != 0)
            deleteShader(mVertexShader);
        this.mVertexShader = id;
    }

    private void setFragmentShader(int id) {
        if (mFragmentShader != 0)
            deleteShader(mFragmentShader);
        this.mFragmentShader = id;
    }
}
