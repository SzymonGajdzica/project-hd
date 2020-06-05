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
            val timeWeight = 4.0
            val sizeWeight = 1.0
            val timeFactor = loadDataList.map { it.waitTime }.let { waitTimes ->
                1.0 / ((waitTimes.sum().toDouble() / 100.0) + 1.0)
            }
            val sizeFactor = loadDataList.map { it.bufferSize }.let { bufferSizes ->
                val maxBufferSize = maxBufferSize.toDouble()
                val averageBufferSize = bufferSizes.average()
                (maxBufferSize - averageBufferSize) / maxBufferSize
            }
            return ((sizeFactor * sizeWeight) + (timeFactor * timeWeight)) / (timeWeight + sizeWeight)
        }

    val loadStats: LoadStats
        get() = LoadStats(lScore, loadDataList, logMessage)

    enum class Type {
        RENEW, SPARE, TRIGGER, ADAPTIVE_RENEW, ADAPTIVE_SPARE, FULL_RENEW
    }

    companion object {

        fun getLoadStrategy(type: Type): LoadStrategy {
            return when (type) {
                Type.RENEW -> RenewLoadStrategy()
                Type.SPARE -> SpareLoadStrategy()
                Type.TRIGGER -> TriggerLoadStrategy()
                Type.ADAPTIVE_RENEW -> AdaptiveRenewLoadStrategy()
                Type.ADAPTIVE_SPARE -> AdaptiveSpareLoadStrategy()
                Type.FULL_RENEW -> FullRenewLoadStrategy()
            }
        }

    }

}
