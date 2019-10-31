package net.fkm.pullrefreshtest.utils;

import net.fkm.pullrefreshtest.MyApplication;
import net.fkm.pullrefreshtest.R;

import okhttp3.Callback;
import okhttp3.Request;


public class OkHttpUtils {

    private static final String REQUEST_TAG = "okhttp";

    public static Request buildRuquest(String url) {
        if (CheckNetwork.isNetworkConnected(MyApplication.getInstance())) {
            Request request = new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .build();
            return request;
        } else {
            ToastUtil.showToastLong(MyApplication.getInstance().getString(R.string.network_unavailable));
        }
        return null;
    }

    public static void excute(String url, Callback callback) {
        Request request = buildRuquest(url);
        excute(request, callback);
    }

    public static void excute(Request request, Callback callback) {
        MyApplication.getHttpClient().newCall(request).enqueue(callback);
    }

    public static void excute(String url, String headerName, String headerValue, Callback callback) {
        if (CheckNetwork.isNetworkConnected(MyApplication.getInstance())) {
            Request request = new Request.Builder()
                    .tag(REQUEST_TAG)
                    .url(url)
                    .addHeader(headerName, headerValue)
                    .get()
                    .build();
            excute(request, callback);
        }
    }

}
