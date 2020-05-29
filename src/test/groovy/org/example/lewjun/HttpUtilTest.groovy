package org.example.lewjun

import org.example.lewjun.model.Ac01
import org.example.lewjun.util.ApiUrl
import org.example.lewjun.util.HttpUtil

class HttpUtilTest extends GroovyTestCase {
    void testGetFail() {
        HttpUtil.execute(ApiUrl.fail,
                ["seq": System.currentTimeMillis().toString()],
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<Ac01>() {

                    @Override
                    void onStart() {
                        println "onStart"
                    }

                    @Override
                    void onSuccess(Ac01 data) {
                        println data.aac002
                    }

                    @Override
                    void onError(String err) {
                        println err
                    }

                    @Override
                    void onFinish() {
                        println "onFinish"
                    }
                })

        Thread.sleep(10000)
    }

    void testGetAc01() {
        HttpUtil.execute(ApiUrl.getAc01,
                ["seq": System.currentTimeMillis().toString()],
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<Ac01>() {

                    @Override
                    void onStart() {
                        println "onStart"
                    }

                    @Override
                    void onSuccess(Ac01 data) {
                        println data.aac002
                    }

                    @Override
                    void onError(String err) {
                        println err
                    }

                    @Override
                    void onFinish() {
                        println "onFinish"
                    }
                })

        Thread.sleep(10000)
    }

    void testGetFile() {
        HttpUtil.execute(ApiUrl.getFile,
                ["seq": System.currentTimeMillis().toString()],
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<byte[]>() {

                    @Override
                    void onStart() {
                        println "onStart"
                    }

                    @Override
                    void onSuccess(byte[] data) {
                        println data.size()
                    }

                    @Override
                    void onError(String err) {
                        println err
                    }

                    @Override
                    void onFinish() {
                        println "onFinish"
                    }
                })

        Thread.sleep(30000)
    }
}
