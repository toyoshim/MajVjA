package org.twintail.majvj;

public interface MajVjClient {
    public void onCreated(MajVj api, int width, int height);
    public void onResized(int width, int height);
    public void onDraw();
}
