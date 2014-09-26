package org.twintail.majvj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

final public class Dialog {
    public static void fatal(final Activity activity, String title, String message, String ok) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        });
        builder.create();
        builder.show();
    }
}
