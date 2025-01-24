package com.chilly.places_svc

import com.chilly.places_svc.containers.ContainersEnvironment
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PlacesSvcApplicationTests : ContainersEnvironment() {

    @Test
    fun contextLoads() {}

}