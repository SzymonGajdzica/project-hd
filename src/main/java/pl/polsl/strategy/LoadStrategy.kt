package pl.polsl.strategy

import pl.polsl.main.Main
import java.util.*

abstract class LoadStrategy {

    protected val maxBufferSize = Main.maxBufferSize
    protected val pageSize = Main.pageSize
    private val loadDataList = LinkedList<LoadData>()

    abstract fun getNumberOfPages(remainingElements: Int): Int

    abstract fun getInitialNumberOfPages(): Int

    open fun analyzeData(loadData: LoadData) {
        loadDataList.add(loadData)
    }

    protected open val logMessage: String? = null

    private val lScore: Double
        get() {
            val timeFactor = 100.0
            val averageWaitTime = loadDataList.map { it.waitTime }.average()
            val averageBufferSize = loadDataList.map { it.bufferSize }.average()
            return ((maxBufferSize - averageBufferSize) / maxBufferSize) / ((averageWaitTime * timeFactor) + 1.0)
        }

    val loadStats: LoadStats
        get() = LoadStats(lScore, loadDataList, logMessage)

    enum class Type {
        RENEW, SPARE, TRIGGER, ADAPTIVE, TEST
    }

    companion object {

        fun getLoadStrategy(type: Type): LoadStrategy {
            return when (type) {
                Type.RENEW -> RenewLoadStrategy()
                Type.SPARE -> SpareLoadStrategy()
                Type.TRIGGER -> TriggerLoadStrategy()
                Type.ADAPTIVE -> AdaptiveLoadStrategy()
                Type.TEST -> TestLoadStrategy()
            }
        }

    }

}
