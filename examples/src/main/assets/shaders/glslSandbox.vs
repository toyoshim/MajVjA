attribute vec2 aCoord;
varying vec2 surfacePosition;

void main() {
    gl_Position = vec4(aCoord.x, aCoord.y, 0.0, 1.0);
    surfacePosition = aCoord;
}
