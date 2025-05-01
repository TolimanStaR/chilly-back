package com.chilly.files.svc.service

import io.minio.MinioClient
import org.chilly.common.exception.CallFailedException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class FilesService(
    private val minioClient: MinioClient,
    @Value("\${minio.bucket}") private val bucketName: String
) {

    fun uploadFile(file: MultipartFile): ResponseEntity<String> {
        val id = UUID.randomUUID()
        val filename = "$id${file.originalFilename.extension()}"

        upload(filename, file).getOrRethrowLogging("Unable to save file '$filename' to storage")
        return ResponseEntity.ok(filename)

    }

    fun downloadFile(filename: String): ResponseEntity<InputStreamResource> {
        val inputStream = inputStreamFor(filename).getOrRethrowLogging("Unable to get file '$filename' from storage")
        val contentType = contentTypeFor(filename).getOrRethrowLogging("Unable to get file '$filename' content type")
        return ResponseEntity.ok()
            .contentType(contentType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            .body(InputStreamResource(inputStream))
    }

    private fun String?.extension(): String {
        this ?: return ""
        val extStartIndex = lastIndexOf(".")
        return when {
            extStartIndex > 0 -> substring(startIndex = extStartIndex)
            else -> ""
        }
    }

    private fun <T> Result<T>.getOrRethrowLogging(message: String) = onFailure { it.printStackTrace() }
            .getOrElse { throw CallFailedException(message) }

    private fun contentTypeFor(filename: String) = runCatching {
        val rawType = minioClient.contentTypeFor(bucketName, filename)
        MediaType.parseMediaType(rawType)
    }

    private fun inputStreamFor(filename: String) = runCatching {
        minioClient.inputStreamFor(bucketName, filename)
    }

    private fun upload(filename: String, file: MultipartFile) = runCatching {
        minioClient.upload(bucketName, filename, file)
    }
}
