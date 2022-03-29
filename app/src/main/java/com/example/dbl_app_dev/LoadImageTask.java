package com.example.dbl_app_dev;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class LoadImageTask extends AsyncTask {
    private ImageView imageView;

    public LoadImageTask (ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Bitmap image = null;
        try {
            URL url = new URL("https://media.istockphoto.com/photos/modern-apartment-buildings-on-a-sunny-day-with-a-blue-sky-picture-id1177797403");
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        this.imageView.setImageBitmap(image);
        return  null;
    }
}
