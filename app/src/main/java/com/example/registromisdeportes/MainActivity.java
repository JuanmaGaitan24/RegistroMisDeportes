package com.example.registromisdeportes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int VENGO_GALERIA = 101;
    private static final int PIDO_PERMISO_ESCRITURA = 111;
    private static final int VENGO_CAMARA = 76;
    Button btnSacarFoto, btnCogerFoto, btnAcceder;
    TextView Contrasenna;
    ImageView FotoPerfil;
    File fichero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSacarFoto = findViewById(R.id.buttonSacarFoto);
        btnCogerFoto = findViewById(R.id.buttonCogerFoto);
        btnAcceder = findViewById(R.id.buttonAcceder);
        Contrasenna = findViewById(R.id.editTextTextPassword);
        FotoPerfil = findViewById(R.id.imageViewFotoPerfil);

        btnCogerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cargarImagen();

            }
        });

        btnSacarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {PedirPermisosFoto();}
        });

    }

    private void PedirPermisosFoto() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PIDO_PERMISO_ESCRITURA);
            }
        } else {
            hacerFoto();
        }

    }

    private void hacerFoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            fichero = crearFicheroFoto();
        } catch (IOException e) {
            e.printStackTrace();
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.example.registromisdeportes.fileprovider", fichero));

        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, VENGO_CAMARA);
        } else {
            Toast.makeText(this, "Necesitas una camara", Toast.LENGTH_SHORT).show();
        }

    }

    private File crearFicheroFoto() throws IOException {

        String FechaYHora = new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(new Date());
        String NombreFichero = "Foto_" + FechaYHora;
        File CarpetaFotos = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        CarpetaFotos.mkdir();
        File Imagen = File.createTempFile(NombreFichero, ".jpg", CarpetaFotos);
        return Imagen;

    }

    private void cargarImagen() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent, "Seleccione Aplicacion"), VENGO_GALERIA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri ruta = data.getData();
            FotoPerfil.setImageURI(ruta);
        } else {
            Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
        }

    }
}