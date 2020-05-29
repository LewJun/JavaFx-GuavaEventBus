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

    static final ApiUrl getFile = new ApiUrl("https://cdn.abcotvs.com/dip/images/5261705_042019-cc-ss-sumatran-tiger-img.jpg?w=1600",
            EnumHttpMethod.GET_FILE,
            new TypeToken<ApiResult<byte[]>>() {
            }.getType()
    )

    enum EnumHttpMethod {
        GET, POST, GET_FILE, POST_FILE, POST_JSON
    }
}
