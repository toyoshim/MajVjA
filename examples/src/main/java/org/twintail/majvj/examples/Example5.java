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
        m2D.background(230, 230, 230);
        m2D.stroke(130, 0, 0);
        m2D.strokeWeight(4);
        m2D.line(mWidth / 2 - 70, mHeight / 2 - 70, mWidth / 2 + 70, mHeight / 2 + 70);
        m2D.line(mWidth / 2 + 70, mHeight / 2 - 70, mWidth / 2 - 70, mHeight / 2 + 70);
        // fill(255, 150);
        // ellipse(mWidth / 2, mHeight / 2, 50, 50);
    }

    public void onDraw1() {
        for (int i = 0; i < 16; ++i) {
            m2D.stroke(m2D.random(255), m2D.random(255), m2D.random(255));
            m2D.line(m2D.random(mWidth), m2D.random(mHeight),
                     m2D.random(mWidth), m2D.random(mHeight));
        }
    }
}