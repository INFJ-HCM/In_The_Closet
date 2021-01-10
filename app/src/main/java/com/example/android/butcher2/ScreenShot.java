package com.example.android.butcher2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShot {

    public void screenShot(AutoFitTextureView textureView, DrawView drawView, Context context) {

        String fileName = "";

        Bitmap bitmap = textureView.getBitmap(textureView.getWidth(), textureView.getHeight()); // 카메라 화면 캡쳐
        drawView.setCaptureview(bitmap); // 캡쳐한 카메라 화면을 캔버스로 보내

        SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss"); // 현재 시각
        Date date = new Date();

        drawView.buildDrawingCache(); // 옷이 그려지는 뷰 캐싱
        Bitmap captureview = drawView.getDrawingCache(); // 그걸 비트맵으로 만들어

        fileName = "#In_The_Closet_" + day.format(date) + ".JPEG"; // 저장할 파일 명

        /** After Q */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            // 파일을 write중이라면 다른곳에서 데이터요구를 무시하겠다는 의미입니다.
            values.put(MediaStore.Images.Media.IS_PENDING, 1);
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/In_The_Closet"); // 새로 만들 폴더 명
            System.out.println(MediaStore.Images.Media.RELATIVE_PATH);
            ContentResolver contentResolver = context.getContentResolver();
            Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI; //
            Uri item = contentResolver.insert(collection, values);

            try {
                ParcelFileDescriptor pdf = contentResolver.openFileDescriptor(item, "w", null);
                if (pdf == null) {

                } else {
                    InputStream inputStream = getImageInputStram(captureview);
                    byte[] strToByte = getBytes(inputStream);
                    FileOutputStream fos = new FileOutputStream(pdf.getFileDescriptor());
                    fos.write(strToByte);
                    fos.close();
                    inputStream.close();
                    pdf.close();
                    contentResolver.update(item, values, null, null);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            values.clear();
            // 파일을 모두 write하고 다른곳에서 사용할 수 있도록 0으로 업데이트를 해줍니다.
            values.put(MediaStore.Images.Media.IS_PENDING, 0);
            contentResolver.update(item, values, null, null);
        }

        /** Before Q */
        else {
            String path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() +
                    "/In_The_Closet";

            Log.e("path", path);

            File file = new File(path); // 파일 생성
            if (!file.exists() || file.isDirectory()) {
                file.mkdirs();
            }

            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(path + file); // 파일명 지정
                captureview.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + fileName))); // 폴더 위치
                Log.e("File", "file://" + path + "/Look" + day.format(date) + ".JPEG");

                fos.flush();
                fos.close();
                drawView.destroyDrawingCache();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream getImageInputStram(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapData);

        return bs;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
