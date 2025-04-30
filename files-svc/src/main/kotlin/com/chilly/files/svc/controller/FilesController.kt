package com.chilly.files.svc.controller

import com.chilly.files.svc.service.FilesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Files", description = "File storge related API")
@RestController
@RequestMapping("/api/files")
class FilesController(
    private val filesService: FilesService,
) {
    @PostMapping("upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "upload a file, returns saved filename, so file could be downloaded later")
    fun uploadFile(@RequestPart file: MultipartFile): ResponseEntity<String> {
        return filesService.uploadFile(file)
    }

    @GetMapping("download/{filename}")
    @Operation(summary = "download file with provided filename that has been returned from upload endpoint")
    fun downloadFile(@PathVariable filename: String): ResponseEntity<InputStreamResource> {
        return filesService.downloadFile(filename)
    }
}
