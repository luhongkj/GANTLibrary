package com.luhong.locwithlibrary.ui;

import android.graphics.Canvas;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.gson.JsonSyntaxException;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.NestedScrollAgentWebView;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.utils.DownloadUtil;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zyq.easypermission.EasyPermissionResult;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.HttpException;

/**
 * https://app.luhongkj.com:9443/doc/Common-problem-lib.pdf    常见问题
 * https://app.luhongkj.com:9443/doc/product-description-lib.pdf 产品说明书
 * https://app.luhongkj.com:9443/doc/bicycle-agreement-lib.pdf 保障协议
 * 隐私政策：http://app.luhongkj.com:8820/doc/privacy.pdf
 * Created by ITMG on 2020-01-19.
 */
public class BasePdfActivity extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener, OnDrawListener {
    @BindView(R2.id.pdfView_basePdf)
    PDFView pdfView;
    @BindView(R2.id.progressBar_basePdf)
    ProgressBar progressBar;
    @BindView(R2.id.tv_errorLoad_basePdf)
    TextView tv_errorPage;
    @NonNull
    private String mWebUrl, fileSuffix;
    private String mWebTitle = "", rightText = "";

    @Override
    protected int initLayoutId() {
        return R.layout.activity_base_pdf;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mWebUrl = bundle.getString(BaseConstants.WEB_URL_KEY);
            Logger.error("加载URL= " + mWebUrl);
            mWebTitle = bundle.getString(BaseConstants.WEB_TITLE_KEY);
            rightText = bundle.getString(BaseConstants.WEB_RIGHT_TEXT_KEY);
        }
        initTitleView(true, mWebTitle);
    }

    @RequiresApi(api = 30)
    @Override
    protected void initData() {
        requestStoragePermissions(new EasyPermissionResult() {
            @Override
            public void onPermissionsAccess(int requestCode) {
                super.onPermissionsAccess(requestCode);
                try {
                    if (TextUtils.isEmpty(mWebUrl)) return;
                    fileSuffix = mWebUrl.substring(mWebUrl.lastIndexOf("/") + 1);
//                    File file = new File(FileUtils.getFileDir(), fileSuffix);
//                    if (file != null && file.exists()) {
//                        loadPDFFromAsset(file);
//                        return;
//                    }
                    DownloadUtil.getInstance().download(mActivity, mWebUrl, FileUtils.getFileDir(), fileSuffix, new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(String path) {
                            if (progressBar != null) {
                                progressBar.setProgress(0);
                                progressBar.setVisibility(View.GONE);
                            }
                            File file = new File(path);
                            loadPDFFromAsset(file);
                        }

                        @Override
                        public void onDownloading(int progress) {
                            Logger.error("下载进度total=" + progress + ",progress=" + progress);
                            if (progressBar != null)
                                progressBar.setProgress((int) (progress * 100));
                        }

                        @Override
                        public void onDownloadFailed() {
                            Logger.error("下载出错=");
                            if (progressBar != null)
                                progressBar.setVisibility(View.GONE);
                            loadPDFFromAsset(null);
                        }

                    });
                 /*   OkHttpUtils.post().url(mWebUrl).build().execute(new FileCallBack(FileUtils.getFileDir(), fileSuffix) {
                        @Override
                        public void inProgress(float progress, long total, int id) {
                            Logger.error("下载进度total=" + total + ",progress=" + progress);
                            progressBar.setProgress((int) (progress * 100));
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e, int id) {
                            String errMeg = getError(e);//validateError
                            Logger.error("下载出错=" + errMeg);
                            progressBar.setVisibility(View.GONE);
                            loadPDFFromAsset(null);
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            progressBar.setProgress(0);
                            progressBar.setVisibility(View.GONE);
                            loadPDFFromAsset(response);
                        }

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                            tv_errorPage.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            pdfView.setVisibility(View.VISIBLE);
                        }
                    });*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                super.onPermissionsDismiss(requestCode, permissions);
                showToast("没有权限");
            }
        });
    }

    @Override
    protected void onEventListener() {
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
    }

    @Override
    public void loadComplete(int nbPages) {
        Logger.error("加载完成");
    }

    private void loadPDFFromAsset(File file) {
        if (pdfView == null) return;
        PDFView.Configurator configurator;
        if (file != null) {
            configurator = pdfView.fromFile(file);
        } else {
            configurator = pdfView.fromAsset(fileSuffix);
        }
        if (configurator == null) return;
        configurator.enableSwipe(true)//是否允许翻页，默认是允许翻页
                .swipeHorizontal(false)//pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableDoubletap(false)
                .defaultPage(0)
                .onLoad(this)//设置加载监听
                .onPageChange(this)//翻页监听
                .enableAnnotationRendering(false)// 渲染风格（就像注释，颜色或表单）
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true)// 改善低分辨率屏幕上的渲染
                .spacing(0)// 页面间的间距。定义间距颜色，设置背景视图
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Logger.error("翻页page=" + page + ",pageCount=" + pageCount);
    }

    private String getError(Exception e) {
        String errorMsg = e.getMessage();
        Logger.error("http请求异常信息= " + e.toString());
        if (e instanceof ConnectException || e instanceof UnknownHostException) {
            errorMsg = getString(R.string.net_error);
        } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException) {
            errorMsg = getString(R.string.time_out);
        } else if (e instanceof JsonSyntaxException) {//数据解析异常
            errorMsg = getString(R.string.parsing_error);
        } else if (e instanceof HttpException) {//服务器异常
            errorMsg = getString(R.string.server_error);
        } else if (e instanceof ConcurrentModificationException) {
            errorMsg = getString(R.string.server_error);
        } else {
            errorMsg = getString(R.string.server_error);
        }
        return errorMsg;
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
    }
}
