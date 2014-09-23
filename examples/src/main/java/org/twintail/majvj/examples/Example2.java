package org.twintail.majvj.examples;

import android.opengl.GLES20;
import android.util.Log;

import org.twintail.majvj.MajVj;
import org.twintail.majvj.MajVjActivity;
import org.twintail.majvj.MajVjClient;
import org.twintail.majvj.MajVjProgram;

public class Example2 extends MajVjActivity implements MajVjClient {

    private final String TAG = "Example2";

    protected MajVjClient createClient() {
        return this;
    }

    public void onCreated(MajVj mv, int width, int height) {
        GLES20.glClearColor(0f, 0f, 1f, 1f);

        // Create, compile, and link shaders from String.
        String vertexShader =
                "attribute mediump vec4 aCoord;\n" +
                "void main() {\n" +
                "  gl_Position = aCoord;\n" +
                "}";
        MajVjProgram mvp1 = mv.createProgram();
        if (mvp1.loadVertexShader(vertexShader))
            Log.i(TAG, "MajVjProgram succeeded to load a vertex shader.");
        // R.string is also available. See also res/values/shaders.xml.
        if (mvp1.loadFragmentShader(getString(R.string.fillRedFragmentShader)))
            Log.i(TAG, "MajVjProgram succeeded to load a fragment shader.");
        if (mvp1.link())
            Log.i(TAG, "MajVjProgram succeeded to link shaders.");

        // Create, compile, and link shaders from a file stored in assets. See also assets/shaders/.
        MajVjProgram mvp2 = mv.createProgram();
        if (mvp2.loadVertexShader(readAssetAsString("shaders/vec4CoordThrough.vs")) &&
            mvp2.loadFragmentShader(getString(R.string.fillRedFragmentShader)) &&
            mvp2.link())
            Log.i(TAG, "MajVjProgram succeeded to load and link shaders.");

        // Simply do everything in a single step.
        MajVjProgram mvp3 = mv.createProgram();
        if (mvp3.link(
                getString(R.string.vec4CoordThroughVertexShader),
                getString(R.string.fillRedFragmentShader)))
            Log.i(TAG, "MajVjProgram succeeded to handle shaders in a single step.");

        // Then, release internal GLES2.0 resources when a program is not needed any more.
        // We should not rely on GC here.
        mvp1.shutdown();
        mvp2.shutdown();
        mvp3.shutdown();
    }

    public void onResized(int width, int height) {
    }

    public void onDraw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }
}
