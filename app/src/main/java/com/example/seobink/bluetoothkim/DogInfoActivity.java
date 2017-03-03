package com.example.seobink.bluetoothkim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class DogInfoActivity extends Activity implements View.OnClickListener{
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    private Uri mImageCaptureUri;
    private ImageView iv_DogPhoto;
    private EditText et_DogName;
    private EditText et_DogAge;
    private EditText et_DogSex;
    private int id_view;
    private String absolutePath;

    private DBManager dbmanager;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_info);

        dbmanager = new DBManager(this, "pet_profile.db", null, 1);

        iv_DogPhoto = (ImageView) this.findViewById(R.id.dog_image_modify);
        Button btn_agreeSubmit = (Button) this.findViewById(R.id.btn_submit_profile);

        btn_agreeSubmit.setOnClickListener(this);

        // EditText를 한줄만 입력 가능하도록 설정
        et_DogName = (EditText) this.findViewById(R.id.dog_name);
        et_DogAge = (EditText) this.findViewById(R.id.dog_age);
        et_DogSex = (EditText) this.findViewById(R.id.dog_sex);
        et_DogName.setSingleLine(true);
        et_DogSex.setSingleLine(true);
    }

    // 카메라에서 사진 촬영하기 => 촬영 후 이미지 가져옴
    public void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 임시 사용할 파일 경로 생성
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    // 앨범에서 가져오기
    public void doTakeAlbumAction() {
        // 앨범을 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK)
            return;

        switch(requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                Log.d("PetsAndroid", mImageCaptureUri.getPath().toString());
            }
            case PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후 리사이즈할 이미지 크기 결정
                // 이미지 크롭 어플리케이션을 호출
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                // 크롭할 이미지 사이즈 200 * 200으로 저장
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);

                break;
            }
            case CROP_FROM_IMAGE: {
                // 크롭 된 이미지 넘겨받음
                // 이미지뷰에 이미지를 보여주거나 부가 작업 이후 임시파일 삭제
                if (resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                // 크롭된 이미지의 저장 경로
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/PetsAndroid/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    // 크롭된 이미지를 비트맵 photo로 저장
                    Bitmap photo = extras.getParcelable("data");
                    iv_DogPhoto.setImageBitmap(photo);  // 레이아웃 이미지 칸에 crop된 이미지 보여줌

                    storeCropImage(photo, filePath);    // 크롭된 이미지를 외부저장소, 앨범에 저장

                    absolutePath = filePath;
                    break;
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        id_view = v.getId();
        if(v.getId() == R.id.btn_submit_profile) {
            // SQLite 통하여 프로필 업데이트
            // 처음 저장 인 경우
            db = dbmanager.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능
            ContentValues values = new ContentValues();
            // db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
            // 데이터의 삽입은 put을 이용한다.
            values.put("dog_name", et_DogName.getText().toString());
            values.put("dog_age", String.valueOf(et_DogAge.getText()));
            values.put("dog_sex", et_DogSex.getText().toString());
            db.insert("dog_profile", null, values);

            // 프로필 업데이트의 경우
            Intent mainIntent = new Intent(DogInfoActivity.this, MainActivity.class);
            DogInfoActivity.this.startActivity(mainIntent);
            DogInfoActivity.this.finish();

            Toast.makeText(this, "강아지 프로필이 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.btn_UploadDogPic) {
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakePhotoAction();
                }
            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진 촬영", cameraListener)
                    .setNeutralButton("앨범 선택", albumListener)
                    .setNegativeButton("취 소", cancelListener)
                    .show();
        }
    }

    // 비트맵을 저장하는 부분
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // PetsAndroid 폴더를 생성하여 이미지 저장
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PetsAndroid";
        File directory_PetsAndroid = new File(dirPath);

        if(!directory_PetsAndroid.exists()) // 새로 이미지 저장
            directory_PetsAndroid.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 크롭 한 사진이 앨범에 보이도록 갱신
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
