package pl.polsl.data.manager

import pl.polsl.data.model.Data
import pl.polsl.data.source.DataSource
import pl.polsl.main.Main
import pl.polsl.main.waitTill
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
        val numberOfPages = loadStrategy.getInitialNumberOfPages()
        loadMoreData(numberOfPages)
        val waitTime = waitTill { hasNext }
        loadStrategy.analyzeInitialData(waitTime, numberOfPages)
    }

    fun finish() {
        print(loadStrategy.generateReport())
    }

    val hasNext: Boolean
        get() = dataSet.isNotEmpty()

    val next: Data
        get() {
            val numberOfPagesToLoad = loadStrategy.getNumberOfPages(dataSet.size)
            loadMoreData(numberOfPagesToLoad)
            val waitTime = waitTill { hasNext }
            loadStrategy.analyzeData(waitTime, dataSet.size, numberOfPagesToLoad)
            //println(dataSet.size)
            return dataSet.poll()
        }

    private fun loadMoreData(numberOfPagesToLoad: Int) {
        if (isLoadingData || numberOfPagesToLoad <= 0)
            return
        //println("starting load of $numberOfPagesToLoad pages")
        isLoadingData = true
        Thread {
            val data = dataSource.fetchData(currentDate, numberOfPagesToLoad)
            if (data != null) {
                //println("loaded ${data.size / Main.pageSize} pages")
                if (dataSet.size + data.size <= maxBufferSize) {
                    dataSet.addAll(data)
                    if (dataSet.isNotEmpty())
                        currentDate = dataSet.last.date
                } else
                    println("Buffer overload, not adding data to dataSet")

            } else {
                println("No data found for this date")
            }
            isLoadingData = false
        }.start()
    }

}