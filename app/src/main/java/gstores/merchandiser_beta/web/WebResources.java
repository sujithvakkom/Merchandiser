package gstores.merchandiser_beta.web;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WebResources {
    public static Bitmap getOnlineImageResources(String urlLink, Bitmap defaultImage) throws MalformedURLException {
        URL url = new URL(urlLink);
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return defaultImage;
        }
        return bmp;
    }
}
