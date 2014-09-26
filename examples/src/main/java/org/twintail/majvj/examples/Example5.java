package org.twintail.majvj.examples;

import org.twintail.majvj.MajVj;
import org.twintail.majvj.MajVj2D;
import org.twintail.majvj.MajVjActivity;
import org.twintail.majvj.MajVjClient;

public class Example5 extends MajVjActivity implements MajVjClient {

    private final String TAG = "Example5";
    private MajVj2D m2D;
    private int mWidth;
    private int mHeight;

    protected MajVjClient createClient() {
        return this;
    }

    public void onCreated(MajVj mv, int width, int height) {
        m2D = mv.get2DInterface();
        m2D.background(0f);
    }

    public void onResized(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void onDraw() {
        for (int i = 0; i < 16; ++i) {
            m2D.stroke(m2D.random(255), m2D.random(255), m2D.random(255));
            m2D.line(m2D.random(mWidth), m2D.random(mHeight),
                     m2D.random(mWidth), m2D.random(mHeight));
        }
    }
}