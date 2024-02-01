package com.example.test_mfa_oauth_auth_client.controller;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.test_mfa_oauth_auth_client.utils.SecurityContextHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class RestApi {

    private final Log log = LogFactory.getLog(RestApi.class);

    @RequestMapping("/publishApi")
    public String test() {
        return "publish resource";
    }

    @RequestMapping("/needAuthApi")
    public String needHide() {
        log.info("get user = " + SecurityContextHandler.getUserInfo());
        return "need auth api";
    }

    @RequestMapping("/publisher/role")
    public String needPublisherRole() {
        log.info("get user = " + SecurityContextHandler.getUserInfo());
        return "needPublisherRole";
    }

    @RequestMapping("/writer/role")
    public String needWriterRole() {
        log.info("get user = " + SecurityContextHandler.getUserInfo());
        return "needWriterRole";
    }

    @RequestMapping("/reader/role")
    public String needReaderRole() {
        log.info("get user = " + SecurityContextHandler.getUserInfo());
        return "needReaderRole";
    }

    @RequestMapping("/writerPublisher/role")
    public String needWriterPublisherRole() {
        log.info("get user = " + SecurityContextHandler.getUserInfo());
        return "needWriterRole or needPublisherRole";
    }
    @RequestMapping("/testTop")
    public List<String> testTop() {
        return List.of("top1", "top2", "top3");
    }
    @RequestMapping("/testSub")
    public List<String> testSub() {
        int positiveInt = RandomUtil.getPositiveInt();
        System.err.println(positiveInt);
        boolean flag = positiveInt%2 == 0;
        System.err.println(flag);
        if (flag) {
            return List.of("sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3", "sub1", "sub2", "sub3");
        }
        return List.of("end1", "end2");
    }
}
