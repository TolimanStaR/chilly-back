package com.chilly.files.svc.service

import io.minio.*
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream


internal fun MinioClient.upload(bucketName: String, objectName: String, file: MultipartFile) = putObject(
    /* args = */ PutObjectArgs.builder()
        .bucket(bucketName)
        .`object`(objectName)
        .stream(file.inputStream, file.size, -1)
        .contentType(file.contentType)
        .build()
)

internal fun MinioClient.inputStreamFor(bucketName: String, filename: String): InputStream = getObject(
    /* args = */ GetObjectArgs.builder()
        .bucket(bucketName)
        .`object`(filename)
        .build()
)

internal fun MinioClient.checkBucketExists(bucketName: String) = bucketExists(
    /* args = */ BucketExistsArgs.builder()
        .bucket(bucketName)
        .build()
)

internal fun MinioClient.createBucket(bucketName: String) = makeBucket(
    /* args = */ MakeBucketArgs.builder()
        .bucket(bucketName)
        .build()
)

internal fun MinioClient.contentTypeFor(bucketName: String, filename: String) = statObject(
    /* args = */ StatObjectArgs.builder()
        .bucket(bucketName)
        .`object`(filename)
        .build()

).contentType()