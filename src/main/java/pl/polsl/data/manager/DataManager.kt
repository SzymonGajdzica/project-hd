package pl.polsl.data.manager

import pl.polsl.data.model.Data
import pl.polsl.data.source.DataSource
import pl.polsl.main.Logger
import pl.polsl.main.Main
import pl.polsl.main.waitTill
import pl.polsl.strategy.LoadData
import pl.polsl.strategy.LoadStrategy
import java.util.*
import java.util.concurrent.LinkedBlockingDeque

class DataManager(
        private val dataSource: DataSource,
        private val loadStrategy: LoadStrategy,
        private var currentDate: Date
) {

    private val maxBufferSize = Main.maxBufferSize
    private val dataSet = LinkedBlockingDeque<Data>()
    private var isLoadingData = false

    fun initialize() {
        loadMoreData(loadStrategy.getInitialNumberOfPages())
        waitTill { hasNext }
    }

    fun finish(userId: Int, staticWaitTime: Long) {
        Logger.instance.log(Logger.LogData(userId, staticWaitTime, loadStrategy.loadStats))
    }

    val hasNext: Boolean
        get() = dataSet.isNotEmpty()

    val next: Data
        get() {
            val numberOfPages = if (!isLoadingData) loadStrategy.getNumberOfPages(dataSet.size) else 0
            val wasLoadingData = isLoadingData
            val initialBufferSize = dataSet.size
            if (numberOfPages > 0)
                loadMoreData(numberOfPages)
            val waitTime = waitTill { hasNext }
            loadStrategy.analyzeData(LoadData(
                    date = Date(),
                    waitTime = waitTime,
                    loadedPages = numberOfPages,
                    initialBufferSize = initialBufferSize,
                    bufferSize = dataSet.size - 1,
                    initialIsLoadingData = wasLoadingData,
                    isLoadingData = isLoadingData
            ))
            return dataSet.poll()
        }

    private fun loadMoreData(numberOfPagesToLoad: Int) {
        isLoadingData = true
        Thread {
            val data = dataSource.fetchData(currentDate, numberOfPagesToLoad)
            if (data != null) {
                if (dataSet.size + data.size <= maxBufferSize) {
                    currentDate = data.last().date
                    dataSet.addAll(data)
                } else
                    println("Buffer overload, not adding data to dataSet! Try to configure loadStrategy to prevent it")
            } else
                println("Data source do not have enough data! This scenario not implemented")
            isLoadingData = false
        }.start()
    }

}