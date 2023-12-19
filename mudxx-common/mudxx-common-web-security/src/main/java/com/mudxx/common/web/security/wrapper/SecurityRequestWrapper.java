package com.mudxx.common.web.security.wrapper;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HttpServletRequest包装类
 * <p>
 * 防篡改和防重放通过SpringBoot Filter来实现，
 * filter过滤器需要读取request数据流，request数据流只能读取一次，
 * 需要实现HttpServletRequestWrapper对数据流包装，目的是将request流保存下来
 *
 * @author laiw
 * @date 2023/12/1 16:56
 */
public class SecurityRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 用于将流保存下来
     */
    private final byte[] requestBody;

    public SecurityRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        requestBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return bais.read();
            }
        };

    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
