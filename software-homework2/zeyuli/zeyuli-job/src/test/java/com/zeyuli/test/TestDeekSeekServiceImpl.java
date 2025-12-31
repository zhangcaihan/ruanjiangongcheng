package com.zeyuli.test;


import com.zeyuli.service.impl.DeekSeekServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Map;

/**
 *
 *
 * @author 李泽聿
 * @since 2025-10-22 08:37
 */
@SpringBootTest
public class TestDeekSeekServiceImpl {
    @Autowired
    private DeekSeekServiceImpl deekSeekService;

    @Test
    public void test() {
//        Map<String, Object> chat = deekSeekService.chat("北京", "上海", LocalDate.now(), LocalDate.now());
    }
}
