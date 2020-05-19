package pl.polsl.data.manager

import pl.polsl.data.model.Data
import pl.polsl.data.source.DataSource
import pl.polsl.main.waitTill
import pl.polsl.strategy.LoadStrategy
import java.util.*
import java.util.concurrent.LinkedBlockingDeque

class DataManager(
        private val dataSource: DataSource,
        private val loadStrategy: LoadStrategy,
        private var currentDate: Date
) {

    private val dataSet = LinkedBlockingDeque<Data>()
    private var isLoadingData = false

    fun initialize(){
        loadMoreData(loadStrategy.getInitialNumberOfPages())
        waitTill { hasNext }
    }

    fun finish() {
        println("Report = ${loadStrategy.generateReport()}")
    }

    val hasNext: Boolean
        get() = dataSet.isNotEmpty()

    val next: Data
        get() {
            loadMoreData(loadStrategy.getNumberOfPages(dataSet.size))
            val waitTime = waitTill { hasNext }
            loadStrategy.analyzeData(waitTime, dataSet.size)
            return dataSet.poll()
        }

    private fun loadMoreData(numberOfPagesToLoad: Int) {
        if (isLoadingData || numberOfPagesToLoad <= 0)
            return
        isLoadingData = true
        Thread {
            val data = dataSource.fetchData(currentDate, numberOfPagesToLoad)
            if (data == null) {
                isLoadingData = false
                loadMoreData(numberOfPagesToLoad)
            } else {
                dataSet.addAll(data)
                if (dataSet.isNotEmpty())
                    currentDate = dataSet.last.date
                isLoadingData = false
            }
        }.start()
    }

}