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
            append("LScore(")
            append("Max=${logDataList.map { it.lScore }.max()?.format()}, ")
            append("Min=${logDataList.map { it.lScore }.min()?.format()}, ")
            append("Average=${logDataList.map { it.lScore }.average().format()})\n")
            append("WaitTime(")
            append("Max=${logDataList.flatMap { it.loadDataList }.map { it.waitTime }.max()}, ")
            append("Average=${logDataList.flatMap { it.loadDataList }.map { it.waitTime }.average().format()}, ")
            append("Sum=${logDataList.flatMap { it.loadDataList }.map { it.waitTime }.sum()})\n")
            append("BufferSize(")
            append("Max=${logDataList.flatMap { it.loadDataList }.map { it.initialBufferSize }.max()}, ")
            append("Min=${logDataList.flatMap { it.loadDataList }.map { it.initialBufferSize }.min()}, ")
            append("Average=${logDataList.flatMap { it.loadDataList }.map { it.initialBufferSize }.average().format()})\n")
            append("LoadedPages(")
            append("Max=${logDataList.flatMap { it.loadDataList }.map { it.loadedPages }.max()}, ")
            append("Sum=${logDataList.flatMap { it.loadDataList }.map { it.loadedPages }.sum()})\n\n")
            append("--------DetailedData--------\n\n")
            logDataList.sortedBy { it.lScore }.forEach { logData ->
                append("Info(")
                append("userId=${logData.userId}, ")
                append("staticWaitTime=${logData.staticWaitTime}), ")
                append("requestedElements=${logData.loadDataList.size})\n")
                append("LScore(${logData.lScore})\n")
                append("WaitTime(")
                append("Max=${logData.loadDataList.map { it.waitTime }.max()}, ")
                append("Average=${logData.loadDataList.map { it.waitTime }.average().format()}, ")
                append("Sum=${logData.loadDataList.map { it.waitTime }.sum()})\n")
                append("BufferSize(")
                append("Max=${logData.loadDataList.map { it.initialBufferSize }.max()}, ")
                append("Min=${logData.loadDataList.map { it.initialBufferSize }.min()}, ")
                append("Average=${logData.loadDataList.map { it.initialBufferSize }.average().format()})\n")
                append("LoadedPages(")
                append("Max=${logData.loadDataList.map { it.loadedPages }.max()}, ")
                append("Sum=${logData.loadDataList.map { it.loadedPages }.sum()})\n\n")
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