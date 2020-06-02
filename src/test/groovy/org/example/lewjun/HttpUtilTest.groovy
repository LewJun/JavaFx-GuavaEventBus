package org.example.lewjun

import org.example.lewjun.model.Ac01
import org.example.lewjun.util.ApiUrl
import org.example.lewjun.util.HttpUtil
import org.slf4j.LoggerFactory

class HttpUtilTest extends GroovyTestCase {
    private static final def logger = LoggerFactory.getLogger(HttpUtilTest.class)

    void testGetFail() {
        HttpUtil.execute(ApiUrl.fail,
                ["seq": UUID.randomUUID()],
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<Ac01>() {

                    @Override
                    void onStart() {
                        logger.info "onStart"
                    }

                    @Override
                    void onSuccess(Ac01 data) {
                        logger.info data.aac002
                    }

                    @Override
                    void onError(String err) {
                        logger.error err
                    }

                    @Override
                    void onDownload(int progress, long received, long total) {

                    }

                    @Override
                    void onFinish() {
                        logger.info "onFinish"
                    }
                })

        Thread.sleep(10000)
    }

    void testGetAc01() {
        HttpUtil.execute(ApiUrl.getAc01,
                ["seq": UUID.randomUUID()],
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<Ac01>() {

                    @Override
                    void onStart() {
                        logger.info "onStart"
                    }

                    @Override
                    void onSuccess(Ac01 data) {
                        logger.info data.aac002
                    }

                    @Override
                    void onError(String err) {
                        logger.error err
                    }

                    @Override
                    void onDownload(int progress, long received, long total) {
                        logger.info "$progress/$received/$total"
                    }

                    @Override
                    void onFinish() {
                        logger.info "onFinish"
                    }
                })

        Thread.sleep(10000)
    }

    void testGetFile() {
        HttpUtil.execute(ApiUrl.getFile,
                ["seq": UUID.randomUUID()],
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<HttpUtil.GetFileInfo>() {

                    @Override
                    void onStart() {
                        logger.info "onStart"
                    }

                    @Override
                    void onSuccess(HttpUtil.GetFileInfo data) {
                        logger.info "data.getPath: ${data.file.getPath()}"
                        logger.info "data.contentType: ${data.contentType}"
                        logger.info "data.contentLength: ${data.contentLength}"
                        logger.info "data.file.size: ${data.file.size()}"
                    }

                    @Override
                    void onError(String err) {
                        logger.error err
                    }

                    @Override
                    void onDownload(int progress, long received, long total) {
                        logger.info "$progress/$received/$total"
                    }

                    @Override
                    void onFinish() {
                        logger.info "onFinish"
                    }
                })

        Thread.sleep(30000)
    }

    void testGetFileNoContentLength() {
        HttpUtil.execute(ApiUrl.getFileNoContentLength,
                ["seq": UUID.randomUUID()],
                null,
                null,
                null,
                new HttpUtil.OnHttpCallback<HttpUtil.GetFileInfo>() {

                    @Override
                    void onStart() {
                        logger.info "onStart"
                    }

                    @Override
                    void onSuccess(HttpUtil.GetFileInfo data) {
                        logger.info "data.getPath: ${data.file.getPath()}"
                        logger.info "data.contentType: ${data.contentType}"
                        logger.info "data.contentLength: ${data.contentLength}"
                        logger.info "data.file.size: ${data.file.size()}"
                    }

                    @Override
                    void onError(String err) {
                        logger.error err
                    }

                    @Override
                    void onDownload(int progress, long received, long total) {

                    }

                    @Override
                    void onFinish() {
                        logger.info "onFinish"
                    }
                })

        Thread.sleep(30000)
    }
}
