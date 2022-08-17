package com.axisbank.reportmanagement.amazonconfig

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SecurityConfig {
    @Value("\${cloud.aws.credentials.access-key}")
    private var accessKey: String? = null

    @Value("\${cloud.aws.credentials.secret-key}")
    private var accessSecret: String? = null

    @Value("\${cloud.aws.region.static}")
    private var region: String? = null

    @Value("\${cloud.aws.credentials.access-key}")
    private var accessKeyOptional: String? = null

    @Value("\${cloud.aws.credentials.secret-key}")
    private var accessSecretOptional: String? = null

    @Value("\${cloud.aws.region.static.optional}")
    private var regionOptional: String? = null
    @Bean
    fun s3Client(): AmazonS3 {
        val credentials: AWSCredentials = BasicAWSCredentials(accessKey, accessSecret)
        return AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(region).build()
    }

    @Bean
    fun s3ClientOptional(): AmazonS3 {
        val credentials: AWSCredentials = BasicAWSCredentials(accessKeyOptional, accessSecretOptional)
        return AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(regionOptional).build()
    }
}