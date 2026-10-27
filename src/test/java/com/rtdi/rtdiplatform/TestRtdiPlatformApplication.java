package com.rtdi.rtdiplatform;

import org.springframework.boot.SpringApplication;

public class TestRtdiPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.from(RtdiPlatformApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
