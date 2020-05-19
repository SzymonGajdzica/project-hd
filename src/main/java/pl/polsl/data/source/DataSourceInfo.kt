package pl.polsl.data.source

data class DataSourceInfo(
        val maxTimeOfInactivity: Long,
        val produceTimeRange: LongRange,
        val connectionTimeRange: LongRange,
        val singleDataLoadRange: LongRange
)