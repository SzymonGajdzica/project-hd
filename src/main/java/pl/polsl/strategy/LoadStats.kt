package pl.polsl.strategy

data class LoadStats(
        val lScore: Double,
        val loadDataList: List<LoadData>,
        val logMessage: String?

)