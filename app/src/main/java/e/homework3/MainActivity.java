package e.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private File file;
    private Bitmap bitmap;
    private ImageView imageView;
    private TextView textView;
    private Button button;
    private Button imitation;
    private BroadcastReceiver imageReceiver;
    private BroadcastReceiver broadcastReceiver;
    public static final String IMAGE = "savedImage.jpg";
    public static final String BROADCAST = "BROADCAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.error_text);
        file = new File(getFilesDir().getAbsolutePath(), IMAGE);
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        button = (Button) findViewById(R.id.button);
        imitation = (Button) findViewById(R.id.imitation);

        button.setOnClickListener(this);
        imitation.setOnClickListener(this);

        if (file.exists()) {
            setImage();
        } else setText();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent in = new Intent(context, ImageService.class);
                context.startService(in);
            }
        };
        imageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                file = new File(getFilesDir(), IMAGE);
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                if (file.exists()) setImage();
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(imageReceiver, new IntentFilter(BROADCAST));
    }

    private void setImage() {
        imageView.setImageBitmap(bitmap);
        textView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        imitation.setVisibility(View.INVISIBLE);
    }

    private void setText() {
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        imitation.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(imageReceiver);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            try {
                boolean bool = file.getCanonicalFile().delete();
                Log.e("Status:", bool + "");
                setText();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            broadcastReceiver.onReceive(this, new Intent(Intent.ACTION_POWER_CONNECTED));
        }

    }
}
