package com.axisbank.reportmanagement.controller

import com.axisbank.reportmanagement.model.ReportModel
import com.axisbank.reportmanagement.repository.ReportModelRepository
import com.axisbank.reportmanagement.servcie.amazons3.StorageService
import com.axisbank.reportmanagement.servcie.reports.ReportGenerationService
import net.sf.jasperreports.engine.JRException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RestController
@RequestMapping("/files3")
@CrossOrigin("http://localhost:3000")
class StorageController {
    @Autowired
    private val reportService: ReportGenerationService? = null
    var downloadDecryptedFileName = ""

    @Autowired
    private val service: StorageService? = null

    @Autowired
    private val repository: ReportModelRepository? = null
    @PostMapping("/uploads3")
    fun uploadFile(@RequestParam(value = "file") file: MultipartFile?): ResponseEntity<String> {
        return ResponseEntity(service!!.uploadFile(file!!), HttpStatus.OK)
    }

    @PostMapping("/encrypt/upload")
    fun uploadEncryptFile(@RequestParam(value = "file") file: MultipartFile?): ResponseEntity<String> {
        return ResponseEntity(service!!.upoadEncryptedFile(file!!), HttpStatus.OK)
    }

    @GetMapping("/download/{fileName}")
    fun downloadFile(@PathVariable fileName: String): ResponseEntity<ByteArrayResource> {
        val data = service!!.downloadFile(fileName)
        val resource = ByteArrayResource(data!!)
        return ResponseEntity
            .ok()
            .contentLength(data.size.toLong())
            .header("Content-type", "application/octet-stream")
            .header("Content-disposition", "attachment; filename=\"$fileName\"")
            .body(resource)
    }

    @GetMapping("/decrypt/download/{fileName}")
    fun downloadDecryptFile(@PathVariable fileName: String): ResponseEntity<ByteArrayResource> {
        downloadDecryptedFileName += " $fileName"
        val data = service!!.downloadDecryptFile(fileName)
        val resource = ByteArrayResource(data!!)
        return ResponseEntity
            .ok()
            .contentLength(data.size.toLong())
            .header("Content-type", "application/octet-stream")
            .header("Content-disposition", "attachment; filename=\"$fileName\"")
            .body(resource)
    }

    @GetMapping("/decryptReport/download/{fileName}")
    fun downloadDecryptReport(@PathVariable fileName: String): ResponseEntity<ByteArrayResource> {
        downloadDecryptedFileName += " $fileName"
        val data = service!!.downloadDecryptReport(fileName)
        val resource = ByteArrayResource(data!!)
        return ResponseEntity
            .ok()
            .contentLength(data.size.toLong())
            .header("Content-type", "application/octet-stream")
            .header("Content-disposition", "attachment; filename=\"$fileName\"")
            .body(resource)
    }

    @PostMapping("/generateReport")
    fun uploadReport(@RequestBody report: ReportModel): ResponseEntity<String>? {
        val r = ReportModel()
        r.setUsername(report.getUsername())
        r.setEmail(report.getEmail())
        r.setDownloadedItems(downloadDecryptedFileName)
        var  dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    	var  now = LocalDateTime.now();
        val k = "" + dtf.format(now)
        r.setTimestamp("" + k)
        repository!!.save(r)
        try {
            val pdf: File = reportService!!.exportReport(r.getUsername())
            val s = service!!.uploadEncryptedReport(pdf)
            return ResponseEntity(s, HttpStatus.OK)
        } catch (e: JRException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    @DeleteMapping("/delete/{fileName}")
    fun deleteFile(@PathVariable fileName: String?): ResponseEntity<String> {
        return ResponseEntity(service!!.deleteFile(fileName!!), HttpStatus.OK)
    }

    @DeleteMapping("/delete/report/{fileName}")
    fun deleteReport(@PathVariable fileName: String?): ResponseEntity<String> {
        return ResponseEntity(service!!.deleteReport(fileName!!), HttpStatus.OK)
    }

    @get:GetMapping("/list")
    val allfile: List<String>
        get() = service!!.listAllFiles()

    @get:GetMapping("/listReport")
    val allReport: List<String>
        get() = service!!.listAllReports()
}