package pl.polsl.data.source

import pl.polsl.data.model.Data
import pl.polsl.data.model.Page
import pl.polsl.main.Main
import pl.polsl.main.mRemove
import java.util.*

class DataSource(private val dataSourceInfo: DataSourceInfo) {

    private val pageSize = Main.pageSize
    private val pages = arrayListOf<Page>()
    private val pendingDataList = LinkedList<Data>()
    private var lastFetchTime = System.currentTimeMillis()

    init {
        Thread {
            while (System.currentTimeMillis() - lastFetchTime < 1000) {
                repeat(dataSourceInfo.numberOfProducers) {
                    pendingDataList.add(Data(Date(), Random().nextDouble(), it))
                }
                synchronized(this) {
                    while (pendingDataList.size >= pageSize) {
                        pages.add(Page(pendingDataList.mRemove(pageSize)))
                    }
                }
                Thread.sleep(dataSourceInfo.produceTimeRange.random())
            }
        }.start()
    }

    fun fetchData(startTime: Date, numberOfPages: Int): List<Data>? {
        Thread.sleep(dataSourceInfo.connectionTimeRange.random() + (numberOfPages * dataSourceInfo.singleDataLoadRange.random()))
        lastFetchTime = System.currentTimeMillis()
        if (numberOfPages <= 0)
            throw IllegalArgumentException("numberOfPages must be greater than 0")
        synchronized(this) {
            val startIndex = pages.indexOfFirst { it.date > startTime }
            val endIndex = startIndex + numberOfPages
            if (startIndex == -1 || endIndex >= pages.size)
                return null
            return pages.subList(startIndex, endIndex).flatMap { it.dataList }.map { it.copy() }
        }
    }

}