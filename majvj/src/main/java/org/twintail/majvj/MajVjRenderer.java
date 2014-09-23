package org.twintail.majvj;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MajVjRenderer implements Renderer, MajVj {

    private String TAG = "MajVj";
    private int mWidth;
    private int mHeight;
    private boolean mCreated;
    private MajVjClient mClient;

    public MajVjRenderer(MajVjClient client) {
        mClient = client;
        mCreated = false;
    }

    @Override
    public void onSurfaceCreated(GL10 notUsed, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 notUsed, int width, int height) {
        if (mCreated && mWidth == width && mHeight == height)
            return;
        mWidth = width;
        mHeight = height;
        if (!mCreated) {
            mClient.onCreated(this, mWidth, mHeight);
            mCreated = true;
        } else {
            mClient.onResized(mWidth, mHeight);
        }
    }

    @Override
    public void onDrawFrame(GL10 notUsed) {
        mClient.onDraw();
    }

    private int createShader(int type, String shader) {
        int id = GLES20.glCreateShader(type);
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR || id == 0)
            throw new AssertionError("glCreateShader failed");

        GLES20.glShaderSource(id, shader);
        GLES20.glCompileShader(id);
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == GLES20.GL_TRUE)
            return id;

        String log = GLES20.glGetShaderInfoLog(id);
        Log.e(TAG, "Shader compilation failed: " + log);
        GLES20.glDeleteShader(id);
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
        }
        return createShader(type, builder.toString());
    }

    @Override
    public int createVertexShader(String shader) {
        return createShader(GLES20.GL_VERTEX_SHADER, shader);
    }

    @Override
    public int createVertexShader(InputStream stream) {
        return createShader(GLES20.GL_VERTEX_SHADER, stream);
    }

    @Override
    public int createFragmentShader(String shader) {
        return createShader(GLES20.GL_FRAGMENT_SHADER, shader);
    }

    @Override
    public int createFragmentShader(InputStream stream) {
        return createShader(GLES20.GL_FRAGMENT_SHADER, stream);
    }

    @Override
    public int createProgram(String vertexShader, String fragmentShader) {
        int vertexShaderId = createVertexShader(vertexShader);
        int fragmentShaderId = createFragmentShader(fragmentShader);
        // TODO: We should remember these IDs to delete when we delete the program.
        return createProgram(vertexShaderId, fragmentShaderId);
    }

    @Override
    public int createProgram(InputStream vertexShader, InputStream fragmentShader) {
        int vertexShaderId = createVertexShader(vertexShader);
        int fragmentShaderId = createFragmentShader(fragmentShader);
        // TODO: We should remember these IDs to delete when we delete the program.
        return createProgram(vertexShaderId, fragmentShaderId);
    }

    @Override
    public int createProgram(int vertexShader, int fragmentShader) {
        int id = GLES20.glCreateProgram();
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR || id == 0)
            throw new AssertionError("glCreateProgram failed");

        GLES20.glAttachShader(id, vertexShader);
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
            GLES20.glDeleteProgram(id);
            throw new AssertionError("glAttachShader failed on a vertex shader");
        }

        GLES20.glAttachShader(id, fragmentShader);
        if (GLES20.glGetError() != GLES20.GL_NO_ERROR) {
            GLES20.glDeleteProgram(id);
            throw new AssertionError("glAttachShader failed on a fragment shader");
        }

        GLES20.glLinkProgram(id);
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(id, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == GLES20.GL_TRUE)
            return id;

        String log = GLES20.glGetProgramInfoLog(id);
        Log.e(TAG, "Program link failed: " + log);
        GLES20.glDeleteProgram(id);
        return 0;
    }
}