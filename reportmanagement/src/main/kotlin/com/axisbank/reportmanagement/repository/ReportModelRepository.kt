package com.axisbank.reportmanagement.repository

import com.axisbank.reportmanagement.model.ReportModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReportModelRepository : JpaRepository<ReportModel?, Int?> {
    fun findByUsername(username: String?): Optional<ReportModel?>?
}