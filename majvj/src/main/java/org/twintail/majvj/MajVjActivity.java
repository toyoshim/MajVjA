package org.twintail.majvj;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public abstract class MajVjActivity extends Activity {

    private String TAG = "MajVjActivity";
    private GLSurfaceView mGLView;

    private boolean hasGLES20() {
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        if (info.reqGlEsVersion == 0) {
            Log.w(TAG, "Supporting GLES version is not provided. Assumes running on an emulator.");
            return true;
        }
        Log.i(TAG, "Supporting GLES version is " + info.getGlEsVersion());
        return info.reqGlEsVersion >= 0x00020000;
    }

    protected String readAssetAsString(String path) {
        AssetManager assets = getResources().getAssets();
        try {
            InputStream stream = assets.open(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null)
                builder.append(line);
            return builder.toString();
        } catch (IOException e) {
            Log.e(TAG, "Failed to open " + path + ": " + e.toString());
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!hasGLES20()) {
            Dialog.fatal(this, "Error", "GLES 2.0 is not supported on this device.", "OK");
            return;
        }
        mGLView = new GLSurfaceView(this);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setPreserveEGLContextOnPause(true);
        mGLView.setRenderer(new MajVjRenderer(createClient()));
        mGLView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGLView == null)
            return;
        mGLView.onResume();
        mGLView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLView == null)
            return;
        mGLView.onPause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGLView == null)
            return true;
        mGLView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        mGLView.onTouchEvent(event);
        return true;
    }

    protected abstract MajVjClient createClient();
}
