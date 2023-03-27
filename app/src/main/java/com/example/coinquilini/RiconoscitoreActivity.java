package com.example.coinquilini;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class RiconoscitoreActivity extends AppCompatActivity {

    private MaterialButton inputImageBtn;
    private MaterialButton recognizeTextBtn;
    private MaterialButton salvaBtn;
    private ShapeableImageView imageIv;
    private EditText recognizedTextEt;
    private static  final String TAG = "MAIN_TAG";
    private Uri imageUri = null;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private  String[] cameraPermissions;
    private String[] storagePermissions;
    private ProgressDialog progressDialog;
    private TextRecognizer textRecognizer;
    private String nome_casa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riconoscitore);

        inputImageBtn = findViewById(R.id.inputImageBtn);
        recognizeTextBtn = findViewById(R.id.riconosciBtn);
        salvaBtn = findViewById(R.id.salvaBtn);
        imageIv = findViewById(R.id.image_view);
        recognizedTextEt = findViewById(R.id.recognizedTestEt);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) nome_casa = bundle.getString("nome casa");

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Attendi");
        progressDialog.setCanceledOnTouchOutside(false);

        salvaBtn.setOnClickListener(v -> {

            String[] array = recognizedTextEt.getText().toString().split(" ");

            String tipo = array[0].trim();
            String importo = array[1].trim();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {

                String[] campi = new String[3];
                campi[0] = "tipo";
                campi[1] = "importo";
                campi[2] = "id_casa";

                String[] dati = new String[3];
                dati[0] = tipo;
                dati[1] = importo;
                dati[2] = nome_casa;

                HttpsTrustManager.allowAllSSL();
                PutData putData = new PutData("https://192.168.113.254/appCoinquilini/newBolletta.php", "POST", campi, dati);

                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult().trim();
                        switch (result) {
                            case "Registrazione bolletta completata":
                                Toast.makeText(getApplicationContext(), "Registrazione bolletta completata", Toast.LENGTH_SHORT).show();
                                break;
                            case "Registrazione fallita":
                                Toast.makeText(getApplicationContext(), "Registrazione fallita", Toast.LENGTH_SHORT).show();
                                break;
                            case "Error: Database connection":
                                Toast.makeText(getApplicationContext(), "Error: Database connection", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            });
        });

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        inputImageBtn.setOnClickListener(v -> showInputImageDialog());

        recognizeTextBtn.setOnClickListener(v -> {

            if(imageUri == null) Toast.makeText(getApplicationContext(),"Scegli l'immagine prima",Toast.LENGTH_SHORT).show();
            else recognizeTextFromImage();
        });

    }

    private void recognizeTextFromImage()
    {

        Log.d(TAG, "recognizeTextFromImage: ");

        progressDialog.setMessage("Preparando l'immagine...");
        progressDialog.show();

        try
        {

            InputImage inputImage = InputImage.fromFilePath(this,imageUri);

            progressDialog.setMessage("Riconoscendo il testo...");

            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(text -> {

                progressDialog.dismiss();
                String recognizedText = text.getText();
                Log.d(TAG, "onSucces: recognizedText: "+recognizedText);
                recognizedTextEt.setText(recognizedText);

            })
                    .addOnFailureListener(e -> {

                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: ",e);
                        Toast.makeText(getApplicationContext(),"Preparazione immagine fallita per "+e.getMessage(),Toast.LENGTH_SHORT).show();

                    });

        } catch (Exception e) {

            progressDialog.dismiss();
            Log.d(TAG,"recognizeTextFromImage: ",e);
            Toast.makeText(getApplicationContext(),"Preparazione immagine fallita per "+e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }

    private void showInputImageDialog()
    {

        PopupMenu popupMenu = new PopupMenu(this,inputImageBtn);

        popupMenu.getMenu().add(Menu.NONE,1,1,"CAMERA");
        popupMenu.getMenu().add(Menu.NONE,2,2,"GALLERIA");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(menuItem -> {

            int id = menuItem.getItemId();

            if(id==1)
            {

                Log.d(TAG,"onMenuItemClick: Camera Clicked...");

                if(checkCameraPermission()) pickImageCamera();
                else requestCameraPermission();
            }

            else if (id == 2)
            {

                Log.d(TAG,"onMenuItemClick: Gallery Clicked...");

                if(checkStoragePermission()) pickImageGallery();
                else requestStoragePermission();
            }

            return true;

        });

    }

    private void pickImageGallery()
    {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);

    }

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK)
                    {

                        Intent data = result.getData();
                        if (data != null)
                        {
                            imageUri = data.getData();
                            imageIv.setImageURI(imageUri);
                        }

                    }

                    else Toast.makeText(getApplicationContext(),"Cancellato...",Toast.LENGTH_SHORT).show();

                }
            }
    );

    private void pickImageCamera()
    {

        Log.d(TAG,"pickImageCamera: ");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Sample Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);

    }

    private final ActivityResultLauncher<Intent>  cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK) imageIv.setImageURI(imageUri);
                    else Toast.makeText(getApplicationContext(),"Cancellato",Toast.LENGTH_SHORT).show();
                }
            }
    );

    private boolean checkStoragePermission()
    {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission()
    {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {

            case CAMERA_REQUEST_CODE:
            {

                if(grantResults.length>0)
                {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted) pickImageCamera();
                    else Toast.makeText(this,"Permessi della Camera e Storage richiesti", Toast.LENGTH_SHORT).show();

                }

                else Toast.makeText(this,"Cancellato", Toast.LENGTH_SHORT).show();

            }

            break;

            case STORAGE_REQUEST_CODE:
            {

                if(grantResults.length>0)
                {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted) pickImageGallery();
                    else Toast.makeText(this,"Permessi di Storage richiesti", Toast.LENGTH_SHORT).show();

                }

            }
            break;
        }
    }
}