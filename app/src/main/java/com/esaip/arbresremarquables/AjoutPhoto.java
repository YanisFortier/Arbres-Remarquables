package com.esaip.arbresremarquables;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.esaip.arbresremarquables.Formulaires.AjoutAlignement;
import com.esaip.arbresremarquables.Formulaires.AjoutArbre;
import com.esaip.arbresremarquables.Formulaires.AjoutEspaceBoise;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class AjoutPhoto extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1, GALLERY = 2, IMAGE_MAX_SIZE = 18000000;
    private static String LOGIN = "invitesaip",PWD="Hg6ykLuvZBk";
    private String currentPath, fname= "", timeStamp = "";
    private ImageView ivPhoto;
    private Bitmap result,resultCompress;
    private Button btTakePhoto, btKeepPhoto,btChoosePhoto;
    private RadioButton rbType1,rbType2,rbType3;
    private LinearLayout infos;
    private Uri contentUri;
    private TextView tst;
    private File fileInfo,fileInfoBis;
    private Sardine sardine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_photo);
        ivPhoto = findViewById(R.id.ivPhotoImage);
        btTakePhoto = findViewById(R.id.btPhotoTake);
        btKeepPhoto = findViewById(R.id.btPhotoKeep);
        btChoosePhoto = findViewById(R.id.btGalleryTake);
        infos = findViewById(R.id.infos);
        rbType1 = findViewById(R.id.arbres);
        rbType2 = findViewById(R.id.alignement);
        rbType3 = findViewById(R.id.espaceBoise);
        tst = findViewById(R.id.tst1);

        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, R.string.no_camera_permission, Toast.LENGTH_LONG);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
            }
        } else {
            Toast.makeText(this, R.string.no_camera, Toast.LENGTH_LONG);
        }

        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePhoto(v);
            }
        });

        btKeepPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAjout(v);
            }
        });

        btChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallery();
            }
        });

        //Location
        double lat = 0;
        double lon = 0;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            lat = bundle.getDouble("latitude");
            lon = bundle.getDouble("longitude");
            Toast.makeText(this, "Latitude : " + lat, Toast.LENGTH_LONG).show();
        }

        sardine = new OkHttpSardine();
        sardine.setCredentials(LOGIN,PWD);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            Bitmap bitmap = BitmapFactory.decodeFile(currentPath);
            //Toast.makeText(AjoutPhoto.this,currentPath,Toast.LENGTH_LONG).show();
            tst.setText(currentPath);
            bitmap = RotateBitmap(bitmap,0);
            resultCompress = saveCompressImage(changeRatio(bitmap));
            ivPhoto.setImageBitmap(bitmap);
            galleryAddPic();
        }
        else if (requestCode == GALLERY && resultCode == Activity.RESULT_OK && data != null){
            contentUri = data.getData();
            result = saveImage(contentUri);
            resultCompress = saveCompressImage(changeRatio(result));
            /*
            String path = fileInfo.getAbsolutePath();
            String path2 = fileInfoBis.getAbsolutePath();
            Toast.makeText(this,path,Toast.LENGTH_LONG).show();
            Toast.makeText(this,path2,Toast.LENGTH_LONG).show();
            byte[] pho = new byte[0];
            try {
                pho = FileUtils.readFileToByteArray(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sardine.put("https://www.sauvegarde-anjou.org/nuage/remote.php/dav/files/invitesaip/", pho);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
            ivPhoto.setImageBitmap(resultCompress);
            infos.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void changePhoto(View view) {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, R.string.no_camera_permission, Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
            }
        } else {
            Toast.makeText(this, R.string.no_camera, Toast.LENGTH_LONG).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                currentPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.esaip.arbresremarquables",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //Creer le fichier contenant l'image
    private File createImageFile() throws IOException {
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fname = "JPEG_" + timeStamp + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        fileInfo = new File(storageDir, fname);
        //Sauvegarder l'image
        return fileInfo;
    }

    //Rotation de l'image
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    //Ajouter la photo prise depuis la caméra dans la galerie du téléphone
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        infos.setVisibility(View.VISIBLE);
    }

    //Accès à un formulaire en fonction du choix effectué entre : arbre, alignement et espace boisé
    public void goToAjout(View view){
        Intent i = getIntent();
        if(rbType1.isChecked()) {
            Intent ajout = new Intent(getApplicationContext(), AjoutArbre.class);
            ajout.putExtra("photo", currentPath);
            ajout.putExtra("geolocalisation",true);
            startActivity(ajout);
        }
        if (rbType2.isChecked()) {
            Intent ajout2 = new Intent(getApplicationContext(), AjoutAlignement.class);
            ajout2.putExtra("photo", currentPath);
            ajout2.putExtra("geolocalisation",true);
            startActivity(ajout2);
        }
        if(rbType3.isChecked()){
            Intent ajout3 = new Intent(getApplicationContext(), AjoutEspaceBoise.class);
            ajout3.putExtra("photo",currentPath);
            ajout3.putExtra("geolocalisation",true);
            startActivity(ajout3);
        }
    }

    //Récupérer un photo depuis la gallerie
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    //Sauvegarder l'image de la gallerie
    private Bitmap saveImage(Uri contentUri) {
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor c = getContentResolver().query(contentUri,filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String picturePath = c.getString(columnIndex);
        c.close();
        Bitmap finalBitmap = (BitmapFactory.decodeFile(picturePath));
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fname = "JPEG_"+ timeStamp +".jpg";

        fileInfo = new File(storageDir, fname);
        if (fileInfo.exists()) fileInfo.delete();
        try {
            FileOutputStream out = new FileOutputStream(fileInfo);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalBitmap;
    }

    private Bitmap saveCompressImage(Bitmap bitmap){
        fname = "JPEG_" + timeStamp + "_compress.jpg";
        fileInfoBis = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fname);
        if (fileInfoBis.exists()) fileInfoBis.delete();
        try {
            FileOutputStream out = new FileOutputStream(fileInfoBis);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap changeRatio(Bitmap bitmap){
        double height = bitmap.getHeight();
        double width = bitmap.getWidth();
        double ratio = width / height;

        int newWidth = (int) (ratio * (int)Math.sqrt(IMAGE_MAX_SIZE / ratio));
        int newHeight = (int)Math.sqrt(IMAGE_MAX_SIZE / ratio);
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap,newWidth,newHeight,false);
        return newBitmap;
    }
}