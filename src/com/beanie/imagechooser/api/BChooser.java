
package com.beanie.imagechooser.api;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;

public abstract class BChooser {
    protected Activity activity;

    protected int type;

    protected String foldername;

    protected boolean shouldCreateThumbnails;

    protected String filePathOriginal;

    public BChooser(Activity activity, int type, String foldername, boolean shouldCreateThumbnails) {
        this.activity = activity;
        this.type = type;
        this.foldername = foldername;
        this.shouldCreateThumbnails = shouldCreateThumbnails;
    }

    public abstract void choose() throws IllegalArgumentException;

    public abstract void submit(int requestCode, Intent data);

    protected void checkDirectory() {
        File directory = null;
        directory = new File(FileUtils.getDirectory(foldername));
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    protected void startActivity(Intent intent) {
        activity.startActivityForResult(intent, type);
    }

    protected String getAbsoluteImagePathFromUri(Uri imageUri) {
        String[] proj = {
                MediaColumns.DATA, MediaColumns.DISPLAY_NAME
        };

        if (imageUri.toString().startsWith("content://com.android.gallery3d.provider")) {
            imageUri = Uri.parse(imageUri.toString().replace("com.android.gallery3d",
                    "com.google.android.gallery3d"));
        }
        Cursor cursor = activity.getContentResolver().query(imageUri, proj, null, null, null);

        cursor.moveToFirst();

        String filePath = "";
        if (imageUri.toString().startsWith("content://com.google.android.gallery3d")) {
            filePath = imageUri.toString();
        } else {
            filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
        }
        cursor.close();

        return filePath;
    }
}
