package com.example.lenovo.myfirstapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.myfirstapp.R;
import com.example.lenovo.myfirstapp.adapter.ImageAdapter;
import com.example.lenovo.myfirstapp.fragment.ImageChooseFragment;
import com.example.lenovo.myfirstapp.model.Acg;
import com.example.lenovo.myfirstapp.model.BookLab;
import com.example.lenovo.myfirstapp.model.FolderBean;
import com.example.lenovo.myfirstapp.model.PvcLab;
import com.example.lenovo.myfirstapp.view.ListImageDirPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by lenovo on 2016/9/5.
 */
public class ImageActivity extends Activity {

    private GridView mGridView;
    private List<String> mImgs;

    private RelativeLayout mBottomLy;
    private TextView mDirName;
    private TextView mDirCount;

    private File mCurrentDir;
    private int mMaxCount;
    private String mImagePath;

    private List<FolderBean> mfolderBeans = new ArrayList<FolderBean>();
    private ProgressDialog mProgressDialog;
    private static final int DATA_LOADED = 0x110;

    private ListImageDirPopupWindow mDirPopupWindow;
    private ImageAdapter mImgAdapter;

    private Acg mAcg;

    private int chooseTag;

    public static final String EXTRA_IMAGE_ACTIVITY = "extra.image.activity";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == DATA_LOADED) {
                mProgressDialog.dismiss();
                //绑定数据到View中
                data2View();
                initDirPopupWindow();

            }

        }
    };

    private void initDirPopupWindow() {
        mDirPopupWindow = new ListImageDirPopupWindow(this, mfolderBeans);
        mDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });

        mDirPopupWindow.setOnDirSelectedListener(new ListImageDirPopupWindow.onDirSelectListener() {
            @Override
            public void onSelected(FolderBean folderBean) {
                mCurrentDir = new File(folderBean.getDir());
                mImgs =Arrays.asList( mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File file, String s) {
                        if(s.endsWith(".jpg") || s.endsWith(".png") || s.endsWith(".jpeg"))
                            return true;
                        return false;
                    }
                }));
                mImgAdapter = new ImageAdapter(ImageActivity.this, mImgs, mCurrentDir.getAbsolutePath() );
                mGridView.setAdapter(mImgAdapter);

                mDirCount.setText(mImgs.size() + "");
                mDirName.setText(folderBean.getName());

                mDirPopupWindow.dismiss();
            }
        });
    }

    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);

    }

    private void data2View() {
        if(mCurrentDir == null) {
            Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        }
        mImgs = Arrays.asList(mCurrentDir.list());
        mImgAdapter = new ImageAdapter(this, mImgs, mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(mImgAdapter);

        mDirName.setText(mCurrentDir.getName());
        mDirCount.setText(mMaxCount + " ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_IMAGE_ACTIVITY);

        mAcg= PvcLab.get(this).getPvc(uuid);
        if(mAcg == null) {
            mAcg = BookLab.get(this).getBook(uuid);
        }

        setContentView(R.layout.activity_image);
        initView();
        initDatas();
        initEvent();

    }

    private void initEvent() {
        mBottomLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDirPopupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
                mDirPopupWindow.showAsDropDown(mBottomLy, 0 ,0);
                lightOff();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mImagePath = mCurrentDir.getAbsolutePath() + "/" + mImgAdapter.getItem(i);

                if (mAcg.getmHaveImage() == false) {
                    Intent i1 = new Intent(ImageActivity.this, ImageChooseActivity.class);
                    mAcg.setmImageCHoosePath(mImagePath);
                    i1.putExtra(ImageChooseFragment.EXTRA_IMAGEIME_ID, mAcg.getmId());
                    startActivity(i1);
                    finish();
                } else {
                    Intent i2 = new Intent();
                    i2.putExtra(EXTRA_IMAGE_ACTIVITY, mImagePath);
                    setResult(Activity.RESULT_OK, i2);
                    finish();
                }

            }
        });
    }

    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    private void initDatas() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog = ProgressDialog.show(this, null, "正在加载。。。");

        new Thread() {
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = ImageActivity.this.getContentResolver();
                Cursor cursor = cr.query(mImgUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> mDirPaths = new HashSet<String>();

                while (cursor.moveToNext()) {
                    //图片路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if(parentFile == null) {
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();

                    FolderBean folderBean = null;

                    if(mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }

                    if(parentFile.list() == null)
                        continue;

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File file, String s) {
                            if(s.endsWith(".jpg") || s.endsWith(".png") || s.endsWith(".jpeg")) {
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    mfolderBeans.add(folderBean);

                    if(picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
                cursor.close();
                //通知handler扫描图片玩成
                mHandler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();
    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.id_girdView);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        mDirName = (TextView) findViewById(R.id.id_dir_name);
        mDirCount = (TextView) findViewById(R.id.id_count);
    }



}
