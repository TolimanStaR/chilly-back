package com.chilly.files.svc.config

import com.chilly.files.svc.service.checkBucketExists
import com.chilly.files.svc.service.createBucket
import io.minio.MinioClient
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MinioConfig {
    @Value("\${minio.endpoint}")
    private lateinit var url: String

    @Value("\${minio.accessKey}")
    private lateinit var accessKey: String

    @Value("\${minio.secretKey}")
    private lateinit var secretKey: String

    @Value("\${minio.bucket}")
    private lateinit var bucketName: String

    @Bean
    fun minioClient(): MinioClient {
        val client = MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build()

        val exists = client.checkBucketExists(bucketName)
        if (!exists) {
            client.createBucket(bucketName)
            println("bucket $bucketName has not existed. Created new bucket!")
        }

        return client
    }

    @PostConstruct
    private fun createBucketIfNotExists() {

    }
}