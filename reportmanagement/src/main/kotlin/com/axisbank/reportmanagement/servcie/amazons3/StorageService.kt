package com.axisbank.reportmanagement.servcie.amazons3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.amazonaws.util.IOUtils
import com.axisbank.reportmanagement.encryption.EncryptDecrypt
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.util.stream.Collectors
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException


@Service
class StorageService {
    @Value("\${application.bucket.name}")
    private val bucketName: String? = null

    @Value("\${application.bucket.optional.name}")
    private val optionalBucketName: String? = null
    private val aes = EncryptDecrypt()

    @Autowired
    private val s3Client: AmazonS3? = null

    @Autowired
    private val s3ClientOptional: AmazonS3? = null
    fun uploadFile(file: MultipartFile): String {
        val fileObj = convertMultiPartFileToFile(file)
        val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
        s3Client!!.putObject(PutObjectRequest(bucketName, fileName, fileObj))
        fileObj.delete()
        return "File uploaded : $fileName"
    }

    fun upoadEncryptedFile(file: MultipartFile): String {
        val fileObj = convertMultiPartFileToFile(file)
        val fileName = System.currentTimeMillis().toString() + "_" + file.originalFilename
        try {
            val fis = FileInputStream(fileObj)
            val encrypt = aes.encryptFile(fis)
            val fos = FileOutputStream(fileObj)
            val data = encrypt!!.toByteArray()
            fos.write(data)
            s3Client!!.putObject(PutObjectRequest(bucketName, fileName, fileObj))
            fileObj.delete()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return "File uploaded : $fileName"
    }

    fun uploadEncryptedReport(file: File): String {
        val fileName = file.name
        try {
            val fis = FileInputStream(file)
            val encrypt = aes.encryptFile(fis)
            val fos = FileOutputStream(file)
            val data = encrypt!!.toByteArray()
            fos.write(data)
            s3ClientOptional!!.putObject(
                PutObjectRequest(
                    optionalBucketName,
                    fileName,
                    file
                )
            )
            file.delete()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return "File uploaded : $fileName"
    }

    fun downloadFile(fileName: String?): ByteArray? {
        val s3Object = s3Client!!.getObject(bucketName, fileName)
        val inputStream = s3Object.objectContent
        try {
            return IOUtils.toByteArray(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun downloadDecryptFile(fileName: String?): ByteArray? {
        val s3Object = s3Client!!.getObject(bucketName, fileName)
        val inputStream = s3Object.objectContent
        val bis = BufferedInputStream(inputStream)
        try {
            val content = IOUtils.toByteArray(bis)
            return aes.decryptByteArray(content)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return null
    }

    fun downloadDecryptReport(fileName: String?): ByteArray? {
        val s3Object = s3ClientOptional!!.getObject(optionalBucketName, fileName)
        val inputStream = s3Object.objectContent
        val bis = BufferedInputStream(inputStream)
        try {
            val content = IOUtils.toByteArray(bis)
            return aes.decryptByteArray(content)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return null
    }

    fun deleteFile(fileName: String): String {
        s3Client!!.deleteObject(bucketName, fileName)
        return "$fileName removed ..."
    }

    fun deleteReport(fileName: String): String {
        s3ClientOptional!!.deleteObject(optionalBucketName, fileName)
        return "$fileName removed ..."
    }

    private fun convertMultiPartFileToFile(file: MultipartFile): File {
        val convertedFile = File(file.originalFilename)
        try {
            FileOutputStream(convertedFile).use { fos -> fos.write(file.bytes) }
        } catch (e: IOException) {
            log.error("Error converting multipartFile to file", e)
        }
        return convertedFile
    }

    fun listAllFiles(): List<String> {
        val listObjectsV2Result = s3Client!!.listObjectsV2(bucketName)
        return listObjectsV2Result.objectSummaries.stream().map { obj: S3ObjectSummary -> obj.key }
            .collect(Collectors.toList())
    }

    fun listAllReports(): List<String> {
        val listObjectsV2Result = s3ClientOptional!!.listObjectsV2(optionalBucketName)
        return listObjectsV2Result.objectSummaries.stream().map { obj: S3ObjectSummary -> obj.key }
            .collect(Collectors.toList())
    }

    companion object {
        private val log = LoggerFactory.getLogger(StorageService::class.java)
    }
}