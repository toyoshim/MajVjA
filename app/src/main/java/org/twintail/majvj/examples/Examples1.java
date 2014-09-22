package org.twintail.majvj.examples;

import android.opengl.GLES20;

import org.twintail.majvj.MajVj;
import org.twintail.majvj.MajVjActivity;
import org.twintail.majvj.MajVjClient;

public class Examples1 extends MajVjActivity implements MajVjClient {
    protected MajVjClient createClient() {
        return this;
    }

    public void onCreated(MajVj api, int width, int height) {
        GLES20.glClearColor(1f, 0f, 1f, 1f);
    }

    public void onResized(int width, int height) {
    }

    public void onDraw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }
}
