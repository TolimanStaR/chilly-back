package com.chilly.files.svc

import com.chilly.files.svc.containers.ContainersEnvironment
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FilesSvcApplicationTests : ContainersEnvironment() {

    @Test
    fun contextLoads() {}

}