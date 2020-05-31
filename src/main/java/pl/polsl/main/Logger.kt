package pl.polsl.main

import pl.polsl.data.source.DataSourceInfo
import pl.polsl.strategy.LoadData
import pl.polsl.user.UserSimulatorInfo
import java.io.File
import java.io.FileWriter
import java.util.*

class Logger(
        private val userSimulatorInfo: UserSimulatorInfo,
        private val dataSourceInfo: DataSourceInfo,
        private val loadStrategyName: String
) {

    companion object {
        lateinit var instance: Logger
            private set

        fun initialize(userSimulatorInfo: UserSimulatorInfo, dataSourceInfo: DataSourceInfo, loadStrategyName: String) {
            instance = Logger(userSimulatorInfo, dataSourceInfo, loadStrategyName)
        }
    }

    private val pageSize = Main.pageSize
    private val maxBufferSize = Main.maxBufferSize
    private val logDataList = LinkedList<LogData>()

    data class LogData(
            val userId: Int,
            val staticWaitTime: Long,
            val lScore: Double,
            val loadDataList: List<LoadData>
    )

    @Synchronized
    fun log(logData: LogData) {
        logDataList.add(logData)
        if (logDataList.size >= userSimulatorInfo.maxNumberOfUsers)
            log(generateReport())
    }

    private fun generateReport(): String {
        return buildString {
            append("--------General data--------\n\n")
            append("Info(")
            append("date=${Date()}, ")
            append("strategyType=$loadStrategyName, ")
            append("pageSize=$pageSize, ")
            append("maxBufferSize=$maxBufferSize)\n")
            append("$dataSourceInfo\n")
            append("$userSimulatorInfo\n")
            logDataList.map { it.lScore }.also { lScores ->
                append("LScore(")
                append("Max=${lScores.max()}, ")
                append("Min=${lScores.min()}, ")
                append("Average=${lScores.average()})\n")
            }
            append(getLoadDataStats(logDataList.flatMap { it.loadDataList }))
            append("\n")
            append("--------DetailedData--------\n\n")
            logDataList.sortedBy { it.lScore }.forEach { logData ->
                append("Info(")
                append("userId=${logData.userId}, ")
                append("staticWaitTime=${logData.staticWaitTime}, ")
                append("requestedElements=${logData.loadDataList.size})\n")
                append("LScore(${logData.lScore})\n")
                append(getLoadDataStats(logData.loadDataList))
            }
        }
    }

    private fun getLoadDataStats(loadDataList: List<LoadData>): String {
        return buildString {
            loadDataList.map { it.waitTime }.also { waitTimes ->
                append("WaitTime(")
                append("Max=${waitTimes.max()}, ")
                append("Average=${waitTimes.average()}, ")
                append("NonZeroCount=${waitTimes.filter{ it != 0L }.count()}, ")
                append("Sum=${waitTimes.sum()})\n")
            }
            loadDataList.map { it.initialBufferSize }.also { bufferSizes ->
                append("BufferSize(")
                append("Max=${bufferSizes.max()}, ")
                append("Min=${bufferSizes.min()}, ")
                append("Average=${bufferSizes.average()})\n")
            }
            loadDataList.map { it.loadedPages }.also { loadedPagesList ->
                append("LoadedPages(")
                append("Max=${loadedPagesList.max()}, ")
                append("NonZeroCount=${loadedPagesList.filter{ it != 0 }.count()}, ")
                append("NonZeroAverage=${loadedPagesList.filter{ it != 0 }.average()}, ")
                append("Sum=${loadedPagesList.sum()})\n\n")
            }
        }
    }

    private fun log(data: String) {
        val file = File("output_data/log_${loadStrategyName}_${System.currentTimeMillis()}.txt").apply {
            parentFile.mkdirs()
            createNewFile()
        }
        FileWriter(file, true).use { it.write(data) }
        print(data)
    }


}