package com.example.springdemo;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/easyCheck")
@Slf4j
public class Hello {

    @GetMapping("/domain")
    private Integer requestDomain(@RequestParam String callbackUrl) {
        try {
            HttpGet httpGet = new HttpGet();
            httpGet.setURI(new URI(callbackUrl));

            CloseableHttpClient client = HttpClients.createDefault();
            HttpResponse httpResponse = client.execute(httpGet);
            // 调用成功
            if (Objects.equals(httpResponse.getStatusLine().getStatusCode(), 200)) {
                return 0;
            } else {
                // 抛异常，重新消费
                log.info("域名->{}，巡查不可用->{}，请检查", callbackUrl, JSON.toJSONString(httpResponse));
                return 1;
            }
        } catch (Exception e) {
            log.error("域名->{}，巡查异常，请检查", callbackUrl, e);
            return 2;
        }
    }
}
