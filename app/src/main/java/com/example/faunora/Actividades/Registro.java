package com.example.faunora.Actividades;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faunora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Registro extends AppCompatActivity {
    ImageView imagenusuario;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedimagen;
    private EditText email,name,password,password2;
    private ProgressBar cargar;
    private Button cargarboton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        email = findViewById(R.id.textcorreo);
        name = findViewById(R.id.textnombre);
        password = findViewById(R.id.textcontraseña);
        password2 = findViewById(R.id.textcontraseña2);
        imagenusuario = findViewById(R.id.imagenusuario);
        cargar = findViewById(R.id.regProgressBar);
        cargarboton = findViewById(R.id.botonregistro);
        cargar.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        cargarboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarboton.setVisibility(View.INVISIBLE);
                cargar.setVisibility(View.VISIBLE);
                final String correo = email.getText().toString();
                final String nombre = name.getText().toString();
                final String contraseña = password.getText().toString();
                final String contaseña2 = password2.getText().toString();

                if(correo.isEmpty() || nombre.isEmpty() || contraseña.isEmpty() || !contraseña.equals(contaseña2)){

                    showMessage("Por favor, rellene todos los requisitos");
                    cargarboton.setVisibility(View.VISIBLE);
                    cargar.setVisibility(View.INVISIBLE);

                }else{
                    crearcuenta(correo, nombre, contraseña);

                }


            }
        });

        imagenusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();

                }

            }
        });
    }

    private void crearcuenta(String correo, final String nombre, String contraseña) {
        mAuth.createUserWithEmailAndPassword(correo,contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // user account created successfully
                            showMessage("Cuenta creada");
                            // after we created user account we need to update his profile picture and name
                            updateUserInfo( nombre,pickedimagen,mAuth.getCurrentUser());



                        }
                        else
                        {

                            // account creation failed
                            showMessage("Error en creación de cuenta" + task.getException().getMessage());
                            cargarboton.setVisibility(View.VISIBLE);
                            cargar.setVisibility(View.INVISIBLE);

                        }
                    }
                });

    }

    private void updateUserInfo(final String nombre, Uri pickedimagen, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedimagen.getLastPathSegment());
        imageFilePath.putFile(pickedimagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded succesfully
                // now we can get our image url


                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // uri contain user image url


                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nombre)
                                .setPhotoUri(uri)
                                .build();


                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            showMessage("Registro completo");
                                            updateUI();
                                        }

                                    }
                                });

                    }
                });





            }
        });


    }

    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(), Home.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();


    }

    private void openGallery() {
        //abrir galeria
        Intent galleyIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleyIntent.setType("image/*");
        startActivityForResult(galleyIntent, REQUESCODE);


    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Registro.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Registro.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Registro.this, "Por favor, Acepte el permiso", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(Registro.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);}
        }
        else{
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){
            pickedimagen = data.getData();
            imagenusuario.setImageURI(pickedimagen);

        }
    }

}
