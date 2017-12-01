package com.loorain.live.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loorain.live.common.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * @author luzeyan
 * @time 2017/11/3 下午9:32
 * @description
 */


public class TakePhotoDialog extends Dialog implements View.OnClickListener {

    private static final int ASPECTX = 8;
    private static final int ASPECTY = 5;

    private final String CHOOSE_PHOTO = "相册";
    private final String TAKE_PHOTO = "相机";

    private final int TAKE_PHOTO_REQUEST = 0x1110;
    private final int CHOOSE_PHOTO_REQUEST = 0x1111;
    private final int CROP_PHOTO_REQUEST = 0x1112;

    private int mPicWidth;
    private int mPicHeight;


    private Activity mContext;

    private OnBitmapReturn mBitmapReturn;

    private boolean mCanTakePhoto;
    private Fragment mFragment;

    private File mImageFile;

    public TakePhotoDialog(Builder builder) {
        super(builder.context);
        mContext = (Activity) builder.context;
        mBitmapReturn = builder.bitmapReturn;
        mPicWidth = builder.picWidth;
        mPicHeight = builder.picHeight;
        mFragment = builder.fragment;
        mCanTakePhoto = mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(true);

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button chooseBtn = createBtn(CHOOSE_PHOTO);
        chooseBtn.setOnClickListener(this);

        Button takePhoteBtn = createBtn(TAKE_PHOTO);
        takePhoteBtn.setOnClickListener(this);

        linearLayout.addView(chooseBtn);

        linearLayout.addView(takePhoteBtn);


        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (widthPixels * 0.6), ViewGroup.LayoutParams.WRAP_CONTENT);

        params.topMargin = (int) (metrics.density* 20 + 0.5f );
        params.bottomMargin = (int) (metrics.density* 20 + 0.5f);
        setContentView(linearLayout, params);
    }


    /**
     * 在 activity 或 fragment 中的对应方法中调用此方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO_REQUEST:
                    cropPic(formatUri(mImageFile), mPicWidth, mPicHeight);
                    break;

                case CHOOSE_PHOTO_REQUEST:
                    Uri uri = data.getData();
                    if (uri == null) {
                        mBitmapReturn.captureFailed("选取照片失败");
                    }
                    cropPic(uri, mPicWidth, mPicHeight);
                    break;
                case CROP_PHOTO_REQUEST:
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap == null) {
                        mBitmapReturn.captureFailed("剪裁照片失败");
                    }

                    if (mBitmapReturn != null) {
                        mBitmapReturn.returnBitmap(bitmap, saveBitmapFile(bitmap));
                    }
                    dismiss();
                    break;
                default:
            }
        }
    }


    private Button createBtn(String text) {

        Button btn = new Button(mContext);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(10, 20, 10, 20);
        btn.setText(text);
        btn.setTag(text);

        LinearLayout.LayoutParams chooseParams = new LinearLayout.LayoutParams(-1, -2);
        chooseParams.weight = 1;
        btn.setLayoutParams(chooseParams);

        return btn;
    }

    @Override
    public void onClick(View v) {
        switch ((String) v.getTag()) {
            case CHOOSE_PHOTO:
                choosePhoto();
                break;
            case TAKE_PHOTO:
                takePhoto();
                break;
            default:
        }

        dismiss();
    }


    /**
     * 从相册选取照片
     */
    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            if (mFragment == null) {
                mContext.startActivityForResult(intent, CHOOSE_PHOTO_REQUEST);
            } else {
                mFragment.startActivityForResult(intent, CHOOSE_PHOTO_REQUEST);
            }
        }
    }


    /**
     * 调用相机
     */
    private void takePhoto() {
        if (!mCanTakePhoto) {
            Toast.makeText(mContext, "当前手机不支持拍照", Toast.LENGTH_SHORT).show();
            return;
        }

        mImageFile = createImageFile();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, formatUri(mImageFile));

        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            if (mFragment == null) {
                mContext.startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            } else {
                mFragment.startActivityForResult(intent, TAKE_PHOTO_REQUEST);
            }
        }
    }


    /**
     * 裁剪图片
     *
     * @param uri
     * @param outputX
     * @param outputY
     */
    private void cropPic(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        List<ResolveInfo> infoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if(infoList.size() == 0) {
            ToastUtils.show("没有合适的相机应用程序");
            return;
        }
        Iterator<ResolveInfo> iterator = infoList.iterator();
        while (iterator.hasNext()){
            ResolveInfo resolveInfo = iterator.next();

        }


        //指定文件路径以及所访问的文件类型
        intent.setDataAndType(uri, "image/*");
        //进行裁剪
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
         /*图片格式*/
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        /*小图直接通过intent返回*/
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (mFragment == null) {
            mContext.startActivityForResult(intent, CROP_PHOTO_REQUEST);
        } else {
            mFragment.startActivityForResult(intent, CROP_PHOTO_REQUEST);
        }
    }


    /**
     * 生成一个图片文件，默认保存在根目录下的包名文件夹下
     *
     * @return
     */
    private File createImageFile() {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory(), mContext.getPackageName());
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            file = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }

        return new File(file,createFileName());
    }


    private String createFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'yyyyMMdd_HHmmss", Locale.CHINA);
        return dateFormat.format(new Date()) + ".jpg";
    }

    /**
     * 将裁剪的 bm 写入本地
     *
     * @param bitmap
     * @return
     */
    private File saveBitmapFile(Bitmap bitmap) {
        FileOutputStream fos = null;
        File file = createImageFile();
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
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
        Uri uri = formatUri(file);
        mediaScanIntent.setData(uri);
        mContext.sendBroadcast(mediaScanIntent);
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


    /**
     * 兼容 Android7.0 以上
     *
     * @param file
     * @return
     */
    private Uri formatUri(File file) {
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }


    public static class Builder {

        private Context context;
        private Fragment fragment;

        private OnBitmapReturn bitmapReturn;

        private int picWidth = 750;
        private int picHeight = 550;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置图片剪裁尺寸，默认 750，550
         *
         * @param picWidth  图片宽
         * @param picHeight 图片高
         */
        public Builder setPicSize(int picWidth, int picHeight) {
            this.picWidth = picWidth;
            this.picHeight = picHeight;
            return this;
        }


        public Builder setListener(OnBitmapReturn listener) {
            bitmapReturn = listener;
            return this;
        }

        /**
         * 在 fragment 中调用此方法
         *
         * @param fragment
         */
        public Builder setFragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public TakePhotoDialog build() {
            return new TakePhotoDialog(this);
        }
    }

    public interface OnBitmapReturn {


        /**
         * 返回裁剪照片结果
         *
         * @param bitmap
         * @param file
         */
        void returnBitmap(Bitmap bitmap, File file);

        /**
         * 裁剪相片失败
         *
         * @param msg
         */
        void captureFailed(String msg);

    }

}
