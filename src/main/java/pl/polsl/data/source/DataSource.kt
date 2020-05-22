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
            while (System.currentTimeMillis() - lastFetchTime < dataSourceInfo.maxTimeOfInactivity) {
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
            return emptyList()
        synchronized(this) {
            val startIndex = pages.indexOfFirst { it.date > startTime }
            if (startIndex == -1)
                return null
            val endIndex = startIndex + numberOfPages
            return pages.subList(startIndex, if (endIndex < pages.size) endIndex else pages.size - 1).flatMap { it.dataList }.map { it.copy() }
        }
    }

}