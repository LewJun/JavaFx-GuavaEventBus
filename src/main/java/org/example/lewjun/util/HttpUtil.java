package org.example.lewjun.util;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.io.IOUtils;
import org.apache.tika.mime.MimeTypes;
import org.example.lewjun.model.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.TimeUnit;

class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final Map<String, List<Cookie>> cookieStore = new HashMap<>();


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

    interface OnHttpCallback<T> {

        void onStart();

        void onSuccess(final T data);

        void onError(final String err);

        void onDownload(int progress, long received, long total);

        void onFinish();

    }

    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,
                    new TrustManager[]{new TrustAllCerts()},
                    new SecureRandom());

            return sc.getSocketFactory();
        } catch (final Exception e) {
            logger.error("发生异常", e);
        }

        return null;
    }

    private static <T> void handlerResponseBody(final ApiUrl apiUrl,
                                                final OnHttpCallback<T> callback,
                                                final Response response) throws Exception {
        ResponseBody body = response.body();
        if (null == body || !response.isSuccessful()) {
            callback.onError("返回数据异常");
        } else {
            if (apiUrl.enumHttpMethod == ApiUrl.EnumHttpMethod.GET_FILE) {
                byte[] buf = new byte[1024];
                int len = 0;
                long sum = 0L;
                InputStream is = body.byteStream();
                MediaType contentType = body.contentType();

                File file = File.createTempFile(
                        UUID.randomUUID().toString(),
                        MimeTypes.getDefaultMimeTypes()
                                .forName(contentType.type() + "/" + contentType.subtype())
                                .getExtension()
                );
                FileOutputStream fos = new FileOutputStream(file);
                long contentLength = body.contentLength();
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    callback.onDownload((int) (sum * 1.0f / contentLength * 100), sum, contentLength);
                }

                callback.onSuccess(
                        (T) new GetFileInfo(
                                contentType,
                                contentLength,
                                file)
                );

                IOUtils.closeQuietly(fos);
                IOUtils.closeQuietly(is);
            } else {
                ApiResult<T> apiResult = GsonUtil.jsonStringToObj(body.string(), apiUrl.typeOfT);
                if (apiResult.code == 1) {
                    callback.onSuccess(apiResult.data);
                } else {
                    callback.onError(apiResult.msg);
                }
            }
        }

    }

    static class GetFileInfo {
        MediaType contentType;
        long contentLength;
        File file;

        public GetFileInfo(MediaType contentType, long contentLength, File file) {
            this.contentType = contentType;
            this.contentLength = contentLength;
            this.file = file;
        }
    }

    private static OkHttpClient.Builder getOkHttpClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.hostnameVerifier(new TrustAllHostnameVerifier());

        long timeout = 5L;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        builder.connectTimeout(timeout, timeUnit);
        builder.readTimeout(timeout * 10, timeUnit);
        builder.writeTimeout(timeout * 10, timeUnit);
        return builder;
    }

    private static OkHttpClient getOkHttpClient(final OkHttpClient.Builder okHttpClientBuilder) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(logger::info);

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
                        return cookies == null ? new ArrayList<Cookie>() : cookies;
                    }
                })
                .build();
    }


    /**
     * @param apiUrl         url信息
     * @param queryParams    查询参数
     * @param formBodyParams 表单提交内容
     * @param fileParams     上传文件内容
     * @param toJsonObj      要转换为json的对象
     * @param callback       回调
     * @param <T             >
     */
    static <T> void execute(
            final ApiUrl apiUrl,
            final Map<String, ?> queryParams,
            final Map<String, ?> formBodyParams,
            final Map<String, File> fileParams,
            final Object toJsonObj,
            final OnHttpCallback<T> callback) {
        new Thread(() -> {
            try {
                callback.onStart();
                handlerResponseBody(
                        apiUrl,
                        callback,
                        getOkHttpClient(getOkHttpClientBuilder()).newCall(
                                getRequest(apiUrl,
                                        formBodyParams,
                                        fileParams,
                                        toJsonObj,
                                        getHttpUrlBuilder(apiUrl, queryParams)
                                )
                        ).execute()
                );
            } catch (final SocketTimeoutException e) {
                logger.error("出现异常", e);
                callback.onError("网络链接超时");
            } catch (final IOException e) {
                logger.error("出现异常", e);
                callback.onError("IO异常");
            } catch (final Exception e) {
                logger.error("出现异常", e);
                callback.onError("其他异常");
            } finally {
                callback.onFinish();
            }
        }
        ).start();
    }

    private static HttpUrl.Builder getHttpUrlBuilder(final ApiUrl apiUrl, final Map<String, ?> queryParams) {
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(apiUrl.url).newBuilder();
        if (null != queryParams) {
            for (Map.Entry<String, ?> stringEntry : queryParams.entrySet()) {
                httpUrlBuilder.addQueryParameter(stringEntry.getKey(), String.valueOf(stringEntry.getValue()));
            }
        }
        return httpUrlBuilder;
    }

    private static Request getRequest(final ApiUrl apiUrl,
                                      final Map<String, ?> formBodyParams,
                                      final Map<String, File> fileParams,
                                      final Object toJsonObj,
                                      final HttpUrl.Builder httpUrlBuilder) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(httpUrlBuilder.build());

        ApiUrl.EnumHttpMethod enumHttpMethod = apiUrl.enumHttpMethod;
        if (enumHttpMethod == ApiUrl.EnumHttpMethod.GET) {
            requestBuilder.get();
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.GET_FILE) {
            requestBuilder.get();
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST) {
            if (null != formBodyParams) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, ?> me : formBodyParams.entrySet()) {
                    formBodyBuilder.add(me.getKey(), String.valueOf(me.getValue()));
                }

                requestBuilder.post(formBodyBuilder.build());
            }
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST_FILE) {
            if (null != fileParams) {
                MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
                multipartBodyBuilder.setType(MultipartBody.FORM);
                for (Map.Entry<String, File> me : fileParams.entrySet()) {
                    File f = me.getValue();
                    multipartBodyBuilder.addFormDataPart(me.getKey(), f.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), f));
                }

                requestBuilder.post(multipartBodyBuilder.build());
            }
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST_JSON) {
            if (null != toJsonObj) {
                requestBuilder.post(
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), GsonUtil.objToJsonString(toJsonObj))
                );
            }
        } else {
        }

        return requestBuilder.build();
    }
}
