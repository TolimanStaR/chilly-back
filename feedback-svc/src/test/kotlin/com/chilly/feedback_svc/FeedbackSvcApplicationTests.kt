package com.chilly.feedback_svc

import com.chilly.feedback_svc.containers.ContainersEnvironment
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FeedbackSvcApplicationTests : ContainersEnvironment() {

    @Test
    fun contextLoads() {}

}