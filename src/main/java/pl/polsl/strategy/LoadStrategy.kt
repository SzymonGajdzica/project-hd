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

    open fun analyzeInitialData(loadData: LoadData) {

    }

    fun getLScore(): Double {
        val maxWaitTime = loadDataList.map { it.waitTime }.max()?.toDouble() ?: 0.0
        val averageWaitTime = loadDataList.map { it.waitTime }.average()
        val numberOfDelays = loadDataList.map { it.waitTime }.filter { it != 0L }.count().toDouble()
        val maxBufferSize = loadDataList.map { it.initialBufferSize }.max()?.toDouble() ?: 0.0
        val averageBufferSize = loadDataList.map { it.initialBufferSize }.average()
        val numberOfConnections = loadDataList.map { it.loadedPages }.filter { it != 0 }.count().toDouble()
        //TODO mix variables
        return maxWaitTime
    }

    fun getLoadData(): List<LoadData> {
        return loadDataList
    }

    enum class Type {
        RENEW, SPARE, TRIGGER, ADAPTIVE
    }

    companion object {

        fun getLoadStrategy(type: Type): LoadStrategy {
            return when(type) {
                Type.RENEW -> RenewLoadStrategy()
                Type.SPARE -> SpareLoadStrategy()
                Type.TRIGGER -> TriggerLoadStrategy()
                Type.ADAPTIVE -> AdaptiveLoadStrategy()
            }
        }

    }

}
