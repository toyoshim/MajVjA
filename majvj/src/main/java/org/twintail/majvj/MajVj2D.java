package org.twintail.majvj;

public interface MajVj2D {
    public float random(float high);
    public void background(float gray);
    public void background(float gray, float alpha);
    public void background(float v1, float v2, float v3);
    public void background(float v1, float v2, float v3, float alpha);
    public void strokeWeight(float weight);
    public void stroke(float gray);
    public void stroke(float gray, float alpha);
    public void stroke(float v1, float v2, float v3);
    public void stroke(float v1, float v2, float v3, float alpha);
    public void fill(float gray);
    public void fill(float gray, float alpha);
    public void fill(float v1, float v2, float v3);
    public void fill(float v1, float v2, float v3, float alpha);
    public void line(float x1, float y1, float x2, float y2);
}
