package org.twintail.majvj.examples;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

import org.twintail.majvj.MajVj;
import org.twintail.majvj.MajVjActivity;
import org.twintail.majvj.MajVjClient;
import org.twintail.majvj.MajVjProgram;
import org.twintail.majvj.examples.R;

import java.io.IOException;
import java.io.InputStream;

public class Example2 extends MajVjActivity implements MajVjClient {

    private String TAG = "Example2";

    protected MajVjClient createClient() {
        return this;
    }

    public void onCreated(MajVj mv, int width, int height) {
        GLES20.glClearColor(0f, 0f, 1f, 1f);

        // Create, and compile a shader from String.
        String vertexShader =
                "attribute mediump vec4 aCoord;\n" +
                "void main() {\n" +
                "  gl_Position = aCoord;\n" +
                "}";
        int shader1 = mv.createVertexShader(vertexShader);
        Log.i(TAG, "shader1: " + Integer.toString(shader1));

        // Create, and compile a shader from R.string (See also res/values/shaders.xml)
        int shader2 = mv.createVertexShader(getString(R.string.vec4CoordThroughVertexShader));
        Log.i(TAG, "shader2: " + Integer.toString(shader2));

        // Create, and compile a shader from a file stored in assets (See also assets/shaders/)
        AssetManager assets = getResources().getAssets();
        InputStream stream = null;
        try {
            stream = assets.open("shaders/vec4CoordThrough.vs");
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        int shader3 = mv.createVertexShader(stream);
        Log.i(TAG, "shader3: " + Integer.toString(shader3));

        // Create, and compile a fragment shader, too.
        int fragmentShader = mv.createFragmentShader(getString(R.string.fillRedFragmentShader));
        Log.i(TAG, "fragmentShader: " + Integer.toString(fragmentShader));

        // Link shaders, or we can simply pass InputStream or String as shaders.
        int program = mv.createProgram(shader1, fragmentShader);
        Log.i(TAG, "program' " + Integer.toString(program));

        // Then, delete program and shaders when it is not needed any more.
        mv.deleteProgram(program);
        mv.deleteShader(shader1);
        mv.deleteShader(shader2);
        mv.deleteShader(shader3);

        // Or use MajVjProgram class.
        MajVjProgram mvp = mv.createProgram();
        mvp.loadShaders(
                getString(R.string.vec4CoordThroughVertexShader),
                getString(R.string.fillRedFragmentShader));
        mvp.shutdown();
    }

    public void onResized(int width, int height) {
    }

    public void onDraw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }
}
