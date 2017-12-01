package com.loorain.live.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loorain.live.LiveApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 拍照的popWindow
 * Created by lqh on 2016/9/13.
 */
public class TakePhotoPop extends Dialog {
    private static final int ASPECTX = 8;
    private static final int ASPECTY = 5;
    public static final int TAKE_PHOTO = 0x2220;
    public static final int CHECK_PHOTO = 0x2221;
    public static final int CROP_PHOTO = 0x2222;

    private Activity mActivity;
    private Fragment mFragment;
    private TextView mTakePhotoView, mCheckPhotoView;

    private boolean mCanTakePhoto;
    /*拍摄图片保存的路径*/
    private File mImageFile;
    /*--裁剪图片的目标大小--*/
    private int mPicWidth, mPicHeight;

    private OnBitmapReturn mBitmapReturn;

    public void setOnBitmapReturn(OnBitmapReturn bitmapReturn) {
        this.mBitmapReturn = bitmapReturn;
    }

    public TakePhotoPop(Context context, Fragment fragment, int picWith, int picHeight) {
        super(context);
        this.mActivity = (Activity) context;
        this.mFragment = fragment;
        this.mPicWidth = picWith;
        this.mPicHeight = picHeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(createContentView(), params);
        initListener();
    }

    private View createContentView() {
        LinearLayout contentView = new LinearLayout(getContext());
        contentView.setOrientation(LinearLayout.VERTICAL);
        mTakePhotoView = createButton("拍照");
        mCheckPhotoView = createButton("相册");

        contentView.addView(mTakePhotoView);
        contentView.addView(mCheckPhotoView);

        return contentView;
    }


    private TextView createButton(String text) {
        TextView view = new TextView(getContext());
        view.setGravity(Gravity.CENTER);
        view.setPadding(10, 20, 10, 20);
        view.setText(text);
        view.setTextSize(18);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        return view;
    }

    private void initListener() {
        mCanTakePhoto = mActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        /*--------拍照-----*/
        mTakePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        /*----从相册选取----*/
        mCheckPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                    if (mFragment == null) {
                        mActivity.startActivityForResult(intent, CHECK_PHOTO);
                    } else {
                        mFragment.startActivityForResult(intent, CHECK_PHOTO);
                    }
                }

            }
        });
    }

    private void takePhoto() {
        if (!mCanTakePhoto) {
            Toast.makeText(mActivity, "当前手机不支持拍照", Toast.LENGTH_SHORT).show();
            return;
        }
        mImageFile = createImageFile();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, formatUri(mImageFile));

        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            if (mFragment == null) {
                mActivity.startActivityForResult(intent, TAKE_PHOTO);
            } else {
                mFragment.startActivityForResult(intent, TAKE_PHOTO);

            }

        }
    }

    /**
     * 在对应的activity中的onActivityResult调用该方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mBitmapReturn == null) {return;}
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    cropPic(formatUri(mImageFile), mPicWidth, mPicHeight);
                    break;
                case CHECK_PHOTO:
                    Uri uri = data.getData();
                    if (uri == null) {
                        mBitmapReturn.captureFailed("选取照片失败");
                    }
                    cropPic(uri, mPicWidth, mPicHeight);
                    break;
                case CROP_PHOTO:
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap == null) {
                        mBitmapReturn.captureFailed("裁剪照片失败");
                    } else {
                        mBitmapReturn.returnBitmap(bitmap, saveBitmapToFile(bitmap));
                    }
                    dismiss();
                    break;
                default:
            }
        }
    }

    private void cropPic(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        /*指定文件路径以及所访问的文件类型*/
        intent.setDataAndType(uri, "image/*");
        /*进行裁剪*/
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", ASPECTX);
        intent.putExtra("aspectY", ASPECTY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        /*图片格式*/
        intent.putExtra("outputFormat", "JPEG");
        /*小图直接通过intent返回*/
        intent.putExtra("return-data", true);
        if (mFragment == null){
            mActivity.startActivityForResult(intent, CROP_PHOTO);
        } else {
            mFragment.startActivityForResult(intent, CROP_PHOTO);
        }
    }


    /**
     * 生成一个图片文件，默认保存在根目录下的应用包名文件夹下
     *
     * @return
     */
    public File createImageFile() {
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), mActivity.getPackageName());
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            file = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        return new File(file, createFileName());
    }

    private static String createFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'IMG'_MM_dd_HHmmss");
        String str = sdf.format(date) + ".jpg";
        return str;
    }

    /**
     * 将裁剪后的bm写入到本地
     *
     * @param bm
     */
    private File saveBitmapToFile(Bitmap bm) {
        FileOutputStream fos = null;
        File file = createImageFile();
        try {
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 调用系统的扫描器将图片添加到媒体扫描器的数据库中，
     * 使得这些照片可以被系统的相册应用或者其他app访问
     */
    private void galleryAddPic(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        mediaScanIntent.setData(uri);
        mActivity.sendBroadcast(mediaScanIntent);
    }

    /**
     * 压缩图片的方法
     */
    private void zoomPic(ImageView view, String filePath) {
        float targetW = view.getWidth();
        float targetH = view.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        int photoW = options.outWidth;
        int photoH = options.outHeight;


        float scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        options.inJustDecodeBounds = false;
        options.inSampleSize = (int) scaleFactor;
        options.inPurgeable = true;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        view.setImageBitmap(bm);

    }


    //为了兼容7.0文件权限，需要生成不同的uri
    private Uri formatUri(File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(LiveApp.sInstance, LiveApp.sInstance.getPackageName(), file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    //为了兼容7.0文件权限，需要生成不同的uri
    private Uri formatUri(Uri fileUri) {
        File file = null;
        try {
            file = new File(new URI(fileUri.toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return formatUri(file);
    }


    /**
     * 返回裁剪后的图片
     */
    public interface OnBitmapReturn {
        void returnBitmap(Bitmap bm, File file);

        void captureFailed(String msg);
    }

}
