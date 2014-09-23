package org.twintail.majvj;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.Buffer;
import java.nio.FloatBuffer;

public class MajVjProgram {

    public static final int POINTS = GLES20.GL_POINTS;
    public static final int LINES = GLES20.GL_LINES;
    public static final int LINE_STRIP = GLES20.GL_LINE_STRIP;
    public static final int LINE_LOOP = GLES20.GL_LINE_LOOP;
    public static final int TRIANGLES = GLES20.GL_TRIANGLES;
    public static final int TRIANGLE_STRIP = GLES20.GL_TRIANGLE_STRIP;
    public static final int TRIANGLE_FAN = GLES20.GL_TRIANGLE_FAN;

    private final String TAG = "MajVjProgram";
    private MajVj mMv;
    private int mVertexShader;
    private int mFragmentShader;
    private int mProgram;

    public MajVjProgram(MajVj mv) {
        mMv = mv;
    }

    public void shutdown() {
        setVertexShader(0);
        setFragmentShader(0);
        if (mProgram != 0) {
            GLES20.glUseProgram(0);
            GLES20.glDeleteProgram(mProgram);
            mProgram = 0;
        }
    }

    public boolean loadVertexShader(String shader) {
        int id = createVertexShader(shader);
        setVertexShader(id);
        return id != 0;
    }

    public boolean loadFragmentShader(String shader) {
        int id = createFragmentShader(shader);
        setFragmentShader(id);
        return id != 0;
    }

    public boolean link(String vertexShader, String fragmentShader) {
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

    public boolean setVertexAttributeBuffer(String name, int dimension, Buffer buffer) {
        int id = getAttributeLocation(name);
        if (id < 0)
            return false;
        use();
        GLES20.glEnableVertexAttribArray(id);
        GLES20.glVertexAttribPointer(id, dimension, GLES20.GL_FLOAT, false, 0, buffer);
        return true;
    }

    public boolean setVertexAttribute(String name, int dimension, float[] values) {
        FloatBuffer buffer = mMv.createFloatBufferFrom(values);
        return setVertexAttributeBuffer(name, dimension, buffer);
    }

    public boolean setUniform(String name, float value) {
        int id = getUniformLocation(name);
        if (id < 0)
            return false;
        use();
        GLES20.glUniform1f(id, value);
        return true;
    }

    public boolean setUniform(String name, float[] values) {
        int id = getUniformLocation(name);
        if (id < 0)
            return false;
        use();
        switch (values.length) {
            case 1:
                GLES20.glUniform1fv(id, 1, values, 0);
                break;
            case 2:
                GLES20.glUniform2fv(id, 1, values, 0);
                break;
            case 3:
                GLES20.glUniform3fv(id, 1, values, 0);
                break;
            case 4:
                GLES20.glUniform4fv(id, 1, values, 0);
                break;
            default:
                Log.e(TAG, "unsupported uniform length: " + values.length);
                return false;
        }
        return true;
    }

    public void drawArrays(int mode, int first, int count) {
        use();
        GLES20.glDrawArrays(mode, first, count);
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

    private int createVertexShader(String shader) {
        return createShader(GLES20.GL_VERTEX_SHADER, shader);
    }

    private int createFragmentShader(String shader) {
        return createShader(GLES20.GL_FRAGMENT_SHADER, shader);
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

    private int getAttributeLocation(String name) {
        int location = GLES20.glGetAttribLocation(mProgram, name);
        if (location < 0)
            Log.e(TAG, "Failed to find an attribute location for " + name);
        return location;
    }

    private int getUniformLocation(String name) {
        int location = GLES20.glGetUniformLocation(mProgram, name);
        if (location < 0)
            Log.e(TAG, "Failed to find an uniform location for " + name);
        return location;
    }

    public void use() {
        if (mProgram == 0) {
            Log.e(TAG, "use() is called even mProgram is not set.");
            return;
        }
        GLES20.glUseProgram(mProgram);
    }
}
