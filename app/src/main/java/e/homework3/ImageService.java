package e.homework3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Created by Михаил on 27.11.2016.
 */

public class ImageService extends Service implements Runnable {

    private File file;
    private static final String imageURL = "http://images-cdn.9gag.com/photo/a2YVb8e_700b.jpg";
    public static byte[] buffer;
    public static int temp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        file = new File(getFilesDir(), MainActivity.IMAGE);
        new Thread(this).start();
        return START_STICKY;
    }

    @Override
    public void run() {
        InputStream in = null;
        FileOutputStream out = null;
        try {
            URL URL = new URL(imageURL);
            in = new BufferedInputStream(URL.openStream());
            out = new FileOutputStream(file);
            buffer = new byte[1024];
            while ((temp = in.read(buffer)) != -1) {
                out.write(buffer, 0, temp);
            }
            out.close();
            sendBroadcast(new Intent(MainActivity.BROADCAST));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
