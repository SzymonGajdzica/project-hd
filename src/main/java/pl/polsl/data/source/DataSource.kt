package pl.polsl.data.source

import pl.polsl.data.model.Data
import pl.polsl.data.model.Page
import pl.polsl.main.Main
import java.util.*

class DataSource(private val dataSourceInfo: DataSourceInfo) {

    private val pageSize = Main.pageSize
    private val pages = arrayListOf<Page>()
    private var pendingDataList = LinkedList<Data>()
    private var lastFetchTime = System.currentTimeMillis()

    init {
        Thread {
            while (System.currentTimeMillis() - lastFetchTime < dataSourceInfo.maxTimeOfInactivity) {
                pendingDataList.add(Data(Date(), Random().nextDouble(), 0))
                if (pendingDataList.size == pageSize) {
                    synchronized(this) {
                        pages.add(Page(pendingDataList))
                    }
                    pendingDataList = LinkedList<Data>()
                }
                Thread.sleep(dataSourceInfo.produceTimeRange.random())
            }
        }.start()
    }

    fun fetchData(startTime: Date, numberOfPages: Int): List<Data>? {
        if (numberOfPages <= 0)
            return emptyList()
        Thread.sleep(dataSourceInfo.connectionTimeRange.random() + (numberOfPages * dataSourceInfo.singleDataLoadRange.random()))
        lastFetchTime = System.currentTimeMillis()
        synchronized(this) {
            val startIndex = pages.indexOfFirst { it.date > startTime }
            if (startIndex == -1)
                return null
            val endIndex = startIndex + numberOfPages
            return pages.subList(startIndex, if (endIndex < pages.size) endIndex else pages.size - 1).flatMap { it.dataList }.map { it.copy() }
        }
    }

}