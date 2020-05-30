package pl.polsl.main

import pl.polsl.data.source.DataSourceInfo
import pl.polsl.strategy.LoadData
import pl.polsl.user.UserSimulatorInfo
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
            val loadDataList: List<LoadData>
    )

    @Synchronized
    fun log(logData: LogData) {
        logDataList.add(logData)
        if(logDataList.size >= userSimulatorInfo.maxNumberOfUsers)
            sumUp()
    }

    private fun sumUp(){
        print("--------General data--------\n")
        print("Info(date=${Date()}, ")
        print("strategyType=$loadStrategyName, ")
        print("pageSize=$pageSize, ")
        print("maxBufferSize=$maxBufferSize)\n")
        print("$dataSourceInfo\n")
        print("$userSimulatorInfo\n")
        print("Wait time(")
        print("Max=${logDataList.flatMap { it.loadDataList }.map { it.waitTime }.max()}, ")
        print("Average=${logDataList.flatMap { it.loadDataList }.map { it.waitTime }.average().format()}, ")
        print("Sum=${logDataList.flatMap { it.loadDataList }.map { it.waitTime }.sum()})\n")
        print("Buffer size(")
        print("Max=${logDataList.flatMap { it.loadDataList }.map { it.initialBufferSize }.max()}, ")
        print("Min=${logDataList.flatMap { it.loadDataList }.map { it.initialBufferSize }.min()}, ")
        print("Average=${logDataList.flatMap { it.loadDataList }.map { it.initialBufferSize }.average().format()})\n")
        print("Loaded pages(")
        print("Max=${logDataList.flatMap { it.loadDataList }.map { it.loadedPages }.max()}, ")
        print("Sum=${logDataList.flatMap { it.loadDataList }.map { it.loadedPages }.sum()})\n")
        print("--------DetailedData--------\n")
        print("TODO\n")
    }

}