package pl.polsl.data.source

data class DataSourceInfo(
        val produceTimeRange: LongRange,
        val numberOfProducers: Int,
        val connectionTimeRange: LongRange,
        val singleDataLoadRange: LongRange
)
