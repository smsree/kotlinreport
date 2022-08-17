package com.axisbank.reportmanagement.servcie.reports

import com.axisbank.reportmanagement.repository.ReportModelRepository
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


@Service
class ReportGenerationService {
    @Autowired
    private val repository: ReportModelRepository? = null

    private var resourceLoader: ResourceLoader? = null

    @Inject
    fun ReportGenerationService(r: ResourceLoader?) {
        resourceLoader = r
    }
    
    @Throws(JRException::class, IOException::class)
    fun exportReport(username: String?): File {
        val audit1 = repository!!.findAll()
        val r: Resource = resourceLoader!!.getResource("classpath:auditreport.jrxml")
        val auditR: InputStream = r.getInputStream()
        val jasperReport = JasperCompileManager.compileReport(auditR)
        val dataSource = JRBeanCollectionDataSource(audit1)
        val parameters: MutableMap<String, Any> = HashMap()
        parameters["createdBy"] = "audit user"
        val jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource)
        val fileName = System.currentTimeMillis().toString() + "_" + "audit."
        val report = File.createTempFile(fileName, ".pdf")
        JasperExportManager.exportReportToPdfStream(jasperPrint, FileOutputStream(report))
        return report
    }
}