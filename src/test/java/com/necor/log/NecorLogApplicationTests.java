//package com.necor.necor_log;
//
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Marker;
//import org.slf4j.MarkerFactory;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//@Slf4j
//class NecorLogApplicationTests {
//
//    public static final Marker ORDER_PROCESSING = MarkerFactory.getMarker("ORDER_PROCESSING");
//
//    @Test
//    void contextLoads() {
//        log.info("1111");
//
//        log.info(ORDER_PROCESSING, "Processing order with ID: {}", "123456");
//
//        log.warn("aaaaa");
//
//        log.warn(ORDER_PROCESSING, "Processing order with ID: {}", "sss");
//    }
//
//}
