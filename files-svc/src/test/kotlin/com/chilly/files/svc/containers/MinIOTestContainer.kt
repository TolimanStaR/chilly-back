package com.chilly.files.svc.containers

import org.testcontainers.containers.MinIOContainer
import java.lang.System.setProperty

class MinIOTestContainer private constructor() : MinIOContainer(IMAGE_VERSION) {

    override fun start() {
        super.start()

        setProperty("MINIO_ENDPOINT", s3URL)
        setProperty("MINIO_ACCESS_KEY", userName)
        setProperty("MINIO_SECRET_KEY", password)
    }

    override fun stop() = Unit

    companion object {
        private const val IMAGE_VERSION = "minio/minio:latest"
        private var container: MinIOTestContainer? = null

        operator fun invoke(): MinIOContainer {
            return container ?: MinIOTestContainer()
                .also { container = it }
        }
    }
}
