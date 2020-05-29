package org.example.lewjun.util

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.example.lewjun.model.ApiResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.net.ssl.*
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit

class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class)
    private static final Map<String, List<Cookie>> cookieStore = new HashMap<>()


    private static class TrustAllCerts implements X509TrustManager {
        @Override
        void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        }

        @Override
        void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        }

        @Override
        X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0]
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        boolean verify(final String hostname, final SSLSession session) {
            true
        }
    }

    interface OnHttpCallback<T> {

        void onStart()

        void onSuccess(final T data)

        void onError(final String err)

        void onFinish()

    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null
        try {
            final SSLContext sc = SSLContext.getInstance("TLS")
            sc.init(null,
                    [new TrustAllCerts()] as TrustManager[],
                    new SecureRandom())

            ssfFactory = sc.getSocketFactory()
        } catch (final Exception e) {
            logger.error("发生异常", e)
        }

        return ssfFactory
    }

    private static <T> void handlerResponseBody(final ApiUrl apiUrl,
                                                final OnHttpCallback<T> callback,
                                                final Response response) throws IOException {
        final ResponseBody body = response.body()
        if (!body) {
            callback.onError("返回数据异常")
        } else {
            if (apiUrl.enumHttpMethod == ApiUrl.EnumHttpMethod.GET_FILE) {
                final ApiResult<T> apiResult = new ApiResult<>()
                apiResult.data = (T) body.bytes()
                callback.onSuccess(apiResult.data)
            } else {
                final String string = body.string()

                final ApiResult<T> apiResult = GsonUtil.jsonStringToObj(string, apiUrl.typeOfT)
                if (apiResult.code) {
                    callback.onSuccess(apiResult.data)
                } else {
                    callback.onError(apiResult.msg)
                }
            }
        }

    }

    private static OkHttpClient.Builder getOkHttpClientBuilder() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder()
        builder.sslSocketFactory(createSSLSocketFactory())
        builder.hostnameVerifier(new TrustAllHostnameVerifier())

        final long timeout = 5L
        final TimeUnit timeUnit = TimeUnit.SECONDS
        builder.connectTimeout(timeout, timeUnit)
        builder.readTimeout(timeout, timeUnit)
        builder.writeTimeout(timeout, timeUnit)
        return builder
    }

    private static OkHttpClient getOkHttpClient(final OkHttpClient.Builder okHttpClientBuilder) {
        final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            void log(String s) {
                logger.info(s)
            }
        })

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return okHttpClientBuilder
                .addNetworkInterceptor(httpLoggingInterceptor)
                .cookieJar(new CookieJar() {
                    @Override
                    void saveFromResponse(final HttpUrl httpUrl, final List<Cookie> list) {
                        cookieStore.put(httpUrl.host(), list)
                    }

                    @Override
                    List<Cookie> loadForRequest(final HttpUrl httpUrl) {
                        final List<Cookie> cookies = cookieStore.get(httpUrl.host())
                        return cookies ?: new ArrayList<Cookie>()
                    }
                })
                .build()
    }


    /**
     * @param apiUrl url信息
     * @param queryParams 查询参数
     * @param formBodyParams 表单提交内容
     * @param fileParams 上传文件内容
     * @param toJsonObj 要转换为json的对象
     * @param callback 回调
     * @param < T >
     */
    static <T> void execute(
            final ApiUrl apiUrl,
            final Map<String, ?> queryParams,
            final Map<String, ?> formBodyParams,
            final Map<String, File> fileParams,
            final Object toJsonObj,
            final OnHttpCallback<T> callback) {
        new Thread({
            try {
                callback.onStart()
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
                )
            } catch (final SocketTimeoutException e) {
                logger.error("出现异常", e)
                callback.onError("网络链接超时")
            } catch (final IOException e) {
                logger.error("出现异常", e)
                callback.onError("IO异常")
            } catch (final Exception e) {
                logger.error("出现异常", e)
                callback.onError("其他异常")
            } finally {
                callback.onFinish()
            }
        }).start()
    }

    private static HttpUrl.Builder getHttpUrlBuilder(final ApiUrl apiUrl, final Map<String, ?> queryParams) {
        final HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(apiUrl.url).newBuilder()
        if (queryParams) {
            queryParams.each { key, value ->
                httpUrlBuilder.addQueryParameter(key, String.valueOf(value))
            }
        }
        return httpUrlBuilder
    }

    private static Request getRequest(final ApiUrl apiUrl,
                                      final Map<String, ?> formBodyParams,
                                      final Map<String, File> fileParams,
                                      final Object toJsonObj,
                                      final HttpUrl.Builder httpUrlBuilder) {
        final Request.Builder requestBuilder = new Request.Builder()
        requestBuilder.url(httpUrlBuilder.build())

        final ApiUrl.EnumHttpMethod enumHttpMethod = apiUrl.enumHttpMethod
        if (enumHttpMethod == ApiUrl.EnumHttpMethod.GET) {
            requestBuilder.get()
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.GET_FILE) {
            requestBuilder.get()
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST) {
            if (formBodyParams) {
                final FormBody.Builder formBodyBuilder = new FormBody.Builder()
                formBodyParams.each { key, value ->
                    formBodyBuilder.add(key, String.valueOf(value))
                }

                requestBuilder.post(formBodyBuilder.build())
            }
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST_FILE) {
            if (fileParams) {
                final MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                multipartBodyBuilder.setType(MultipartBody.FORM)
                fileParams.each { key, file ->
                    multipartBodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                }

                requestBuilder.post(multipartBodyBuilder.build())
            }
        } else if (enumHttpMethod == ApiUrl.EnumHttpMethod.POST_JSON) {
            if (toJsonObj) {
                requestBuilder.post(
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), GsonUtil.objToJsonString(toJsonObj))
                )
            }
        } else {
        }

        return requestBuilder.build()
    }
}
