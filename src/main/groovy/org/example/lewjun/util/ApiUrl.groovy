package org.example.lewjun.util

import com.google.gson.reflect.TypeToken
import org.example.lewjun.model.Ac01
import org.example.lewjun.model.ApiResult

import java.lang.reflect.Type

class ApiUrl {
    public String url
    public Type typeOfT
    public EnumHttpMethod enumHttpMethod

    ApiUrl(final String url, final EnumHttpMethod enumHttpMethod, final Type typeOfT) {
        this.url = url
        this.enumHttpMethod = enumHttpMethod
        this.typeOfT = typeOfT
    }

    static final ApiUrl fail = new ApiUrl("http://www.mocky.io/v2/5ed0c8e33500008d00ff9f2c",
            EnumHttpMethod.GET,
            new TypeToken<ApiResult<Ac01>>() {
            }.getType())

    static final ApiUrl getAc01 = new ApiUrl("http://www.mocky.io/v2/5ed0beb23500004e00ff9e89",
            EnumHttpMethod.GET,
            new TypeToken<ApiResult<Ac01>>() {
            }.getType())

    static final ApiUrl getAc01List = new ApiUrl("http://www.mocky.io/v2/5ed0bf0a3500004e00ff9e8e",
            EnumHttpMethod.GET,
            new TypeToken<ApiResult<List<Ac01>>>() {
            }.getType())

    // http://sqdownd.onlinedown.net/down/com.cnvcs.gomoku-1.45-145.apk
    // https://s.cn.bing.net/th?id=OJ.zPJyccrUWF0Gsw&pid=MsnJVFeeds&w=16&h=16
    static final ApiUrl getFile = new ApiUrl("http://sqdownd.onlinedown.net/down/com.cnvcs.gomoku-1.45-145.apk",
            EnumHttpMethod.GET_FILE,
            new TypeToken<HttpUtil.GetFileInfo>() {
            }.getType()
    )

    static final ApiUrl getFileNoContentLength = new ApiUrl("https://libs.cdnjs.net/vue/0.6.0/vue.min.js",
            EnumHttpMethod.GET_FILE,
            new TypeToken<HttpUtil.GetFileInfo>() {
            }.getType()
    )

    enum EnumHttpMethod {
        GET, POST, GET_FILE, POST_FILE, POST_JSON
    }
}
