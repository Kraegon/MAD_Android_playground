package nl.reupload.guube.contactcardapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by guube on 10/19/2015.
 */
public class PersonDetail extends AppCompatActivity {


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();

        TextView contactName = (TextView) findViewById(R.id.contactNameTextView);
        TextView contactEmail = (TextView) findViewById(R.id.emailTextView);
        TextView contactNationality = (TextView) findViewById(R.id.nationalityTextView);
        TextView contactPhoneNo = (TextView) findViewById(R.id.phoneNoTextView);
        final ImageView contactPicture = (ImageView) findViewById(R.id.imageContact);

        Bundle extras = getIntent().getExtras();

        String contactPictureURL = extras.getString("IMAGE");
        contactName.setText(extras.getString("FIRSTNAME") + " " + extras.getString("SURNAME"));
        contactEmail.setText(extras.getString("EMAIL"));
        contactNationality.setText(extras.getString("NATIONALITY"));
        contactPhoneNo.setText(extras.getString("PHONENO"));

        // Load image, decode it to Bitmap and return Bitmap to callback
        imageLoader.loadImage(contactPictureURL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                contactPicture.setImageBitmap(loadedImage);
            }
        });
    }

}
