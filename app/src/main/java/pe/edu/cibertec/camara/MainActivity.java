package pe.edu.cibertec.camara;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CAMERA = 1;
    static final int REQUEST_TAKE_PICTURE = 2;

    //Ruta absoluta de la imagen ;
    String currentPathImage ;

    //    cmta2112

    Button btCamara;
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCamara = findViewById(R.id.btCamara);
        ivPhoto = findViewById(R.id.ivPhoto);

        btCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();

            }
        });
    }

    private void takePicture() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //validar que la cámara este disponible
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            //Verificar que se disponga del permiso
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {

                File photoFile = null;
                try {
                    photoFile = createImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivityForResult(cameraIntent, REQUEST_TAKE_PICTURE);
            }
        }
    }

    private File createImage() throws IOException {

        //Asignarle un nombre
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //crear el archivo
        File storgeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storgeDir);

        currentPathImage = image.getAbsolutePath();

        return image;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
//                Manifest.permission.CAMERA ,Manifest.permission.READ_CONTACTS}, REQUEST_CAMERA);
                Manifest.permission.CAMERA}, REQUEST_CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_TAKE_PICTURE && resultCode == RESULT_OK) {
//            Glide.with(this).load(currentPathImage).into(ivPhoto);

            Bitmap  bitmap = (Bitmap) data.getExtras().get("data");
            ivPhoto.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Se dió permiso", Toast.LENGTH_SHORT).show();
                takePicture();

            }
        }
    }
}




