package org.example.lewjun.util;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ApiUrl {
    public static final ApiUrl getObj
            = new ApiUrl("http://www.mocky.io/v2/5ec9c8cd3000006600a6ce7a",
            EnumHttpMethod.GET,
            new TypeToken<ApiResult<Obj>>() {
            }.getType());

    public static final ApiUrl getObjList
            = new ApiUrl("http://www.mocky.io/v2/5ec9c9643000005b00a6ce7c",
            EnumHttpMethod.GET,
            new TypeToken<ApiResult<List<Obj>>>() {
            }.getType());

    public static final ApiUrl getFile =
            new ApiUrl("https://cdn.abcotvs.com/dip/images/5261705_042019-cc-ss-sumatran-tiger-img.jpg?w=1600",
                    EnumHttpMethod.GET_FILE,
                    new TypeToken<ApiResult<byte[]>>() {
                    }.getType()
            );

    public String url;
    public Type typeOfT;
    public EnumHttpMethod enumHttpMethod;

    public ApiUrl(final String url, final EnumHttpMethod enumHttpMethod, final Type typeOfT) {
        this.url = url;
        this.enumHttpMethod = enumHttpMethod;
        this.typeOfT = typeOfT;
    }

    public enum EnumHttpMethod {
        GET, POST, GET_FILE, POST_FILE, POST_JSON;
    }

    public static class ApiResult<T> {
        public int code;
        public String msg;
        public T data;
    }

    public static class Obj extends BaseObj {
        public String hello;
    }

    public static class BaseObj {

    }
}

