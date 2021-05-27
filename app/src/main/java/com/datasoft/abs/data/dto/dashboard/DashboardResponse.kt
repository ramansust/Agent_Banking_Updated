package com.datasoft.abs.data.dto.dashboard

data class DashboardResponse(
    val vmAgentInfos: VmAgentInfos,
    val vmProductWiseInfos: List<VmProductWiseInfo>
)