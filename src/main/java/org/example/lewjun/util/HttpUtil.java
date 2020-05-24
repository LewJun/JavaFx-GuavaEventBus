package org.example.lewjun.util;

import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final Map<String, List<Cookie>> cookieStore = new HashMap<>();

    /**
     * @param apiUrl         url信息
     * @param queryParams    查询参数
     * @param formBodyParams 表单提交内容
     * @param fileParams     上传文件内容
     * @param toJsonObj      要转换为json的对象
     * @param callback       回调
     * @param <T>
     */
    public static <T> void execute(
            final ApiUrl apiUrl,
            final Map<String, ?> queryParams,
            final Map<String, ?> formBodyParams,
            final Map<String, File> fileParams,
            final Object toJsonObj,
            final OnHttpCallback<T> callback) {

        callback.onStart();

        new Thread(() -> {
            try {
                handlerResponseBody(apiUrl, callback, getOkHttpClient(getOkHttpClientBuilder()).newCall(
                        getRequest(apiUrl, formBodyParams, fileParams, toJsonObj, getHttpUrlBuilder(apiUrl, queryParams))
                ).execute());
            } catch (final SocketTimeoutException e) {
                callback.onError("网络链接超时");
            } catch (final IOException e) {
                callback.onError("IO异常");
            } catch (final Exception e) {
                callback.onError("其他异常");
            } finally {
                callback.onFinish();
            }
        }).start();
    }

    private static HttpUrl.Builder getHttpUrlBuilder(final ApiUrl apiUrl, final Map<String, ?> queryParams) {
        final HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(apiUrl.url).newBuilder();
        if (queryParams != null && queryParams.size() > 0) {
            queryParams.forEach((key, value) -> httpUrlBuilder.addQueryParameter(key, String.valueOf(value)));
        }
        return httpUrlBuilder;
    }

    private static Request getRequest(final ApiUrl apiUrl, final Map<String, ?> formBodyParams, final Map<String, File> fileParams, final Object toJsonObj, final HttpUrl.Builder httpUrlBuilder) {
        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httpUrlBuilder.build());

        final ApiUrl.EnumHttpMethod enumHttpMethod = apiUrl.enumHttpMethod;
        if (enumHttpMethod == ApiUrl.EnumHttpMethod.GET) {
            requestBuilder.get();
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.GET_FILE) {
            requestBuilder.get();
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST) {
            if (formBodyParams != null && formBodyParams.size() > 0) {
                final FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyParams.forEach((key, value) -> formBodyBuilder.add(key, String.valueOf(value)));

                requestBuilder.post(formBodyBuilder.build());
            }
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST_FILE) {
            if (fileParams != null && fileParams.size() > 0) {
                final MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
                multipartBodyBuilder.setType(MultipartBody.FORM);
                fileParams.forEach((key, file) -> multipartBodyBuilder.addFormDataPart(
                        key,
                        file.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file)
                ));

                requestBuilder.post(multipartBodyBuilder.build());
            }
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST_JSON) {
            if (toJsonObj != null) {
                requestBuilder.post(
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(toJsonObj))
                );
            }
        } else {
        }

        return requestBuilder.build();
    }

    private static OkHttpClient getOkHttpClient(final OkHttpClient.Builder okHttpClientBuilder) {
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(logger::info);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return okHttpClientBuilder
                .addNetworkInterceptor(httpLoggingInterceptor)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(final HttpUrl httpUrl, final List<Cookie> list) {
                        cookieStore.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(final HttpUrl httpUrl) {
                        final List<Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<>();
                    }
                })
                .build();
    }

    private static OkHttpClient.Builder getOkHttpClientBuilder() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.hostnameVerifier(new TrustAllHostnameVerifier());

        final long timeout = 5L;
        final TimeUnit timeUnit = TimeUnit.SECONDS;
        builder.connectTimeout(timeout, timeUnit);
        builder.readTimeout(timeout, timeUnit);
        builder.writeTimeout(timeout, timeUnit);
        return builder;
    }

    private static <T> void handlerResponseBody(final ApiUrl apiUrl,
                                                final OnHttpCallback<T> callback,
                                                final Response response) throws IOException {
        final ResponseBody body = response.body();
        if (body == null) {
            callback.onError("返回数据异常");
        } else {
            if (apiUrl.enumHttpMethod == ApiUrl.EnumHttpMethod.GET_FILE) {
                final ApiUrl.ApiResult<T> apiResult = new ApiUrl.ApiResult<>();
                apiResult.data = (T) body.bytes();
                callback.onSuccess(apiResult.data);
            } else {
                final String string = body.string();

                final ApiUrl.ApiResult<T> apiResult = new Gson().fromJson(string, apiUrl.typeOfT);
                if (apiResult.code != 1) {
                    callback.onError(apiResult.msg);
                } else {
                    callback.onSuccess(apiResult.data);
                }
            }
        }

    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (final Exception e) {
            logger.error("发生异常", e);
        }

        return ssfFactory;
    }

    public interface OnHttpCallback<T> {

        default void onStart() {
        }

        default void onSuccess(final T data) {
        }

        default void onError(final String err) {
        }

        default void onFinish() {
        }

    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(final String hostname, final SSLSession session) {
            return true;
        }
    }
}