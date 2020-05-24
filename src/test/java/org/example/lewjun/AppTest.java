package org.example.lewjun;

import org.example.lewjun.util.ApiUrl;
import org.example.lewjun.util.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testGet() throws InterruptedException {
        final Map<String, Object> params = new HashMap<>();
        params.put("mocky-delay", "3s");
        HttpUtil.execute(ApiUrl.getObj,
                params,
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<ApiUrl.Obj>() {
                    @Override
                    public void onSuccess(final ApiUrl.Obj data) {
                        System.out.println(data.hello);
                    }

                    @Override
                    public void onError(final String err) {
                        System.err.println("err1 " + err);
                    }
                });

        System.out.println("--------------------");

        HttpUtil.execute(ApiUrl.getObjList,
                params,
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<List<ApiUrl.Obj>>() {
                    @Override
                    public void onSuccess(final List<ApiUrl.Obj> data) {
                        data.forEach(it -> {
                            System.out.println(it.hello);
                        });
                    }

                    @Override
                    public void onError(final String err) {
                        System.err.println("err " + err);
                    }
                });


        HttpUtil.execute(ApiUrl.getFile,
                params,
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<byte[]>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(final byte[] data) {
                        System.out.println(data.length);
                    }

                    @Override
                    public void onError(final String err) {
                        System.out.println(err);
                    }

                    @Override
                    public void onFinish() {

                    }
                });

        Thread.sleep(10000);
    }
}
