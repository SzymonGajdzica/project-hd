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

    open fun notEnoughDataAvailable(numberOfRequestedPages: Int) {

    }

    fun getLScore(): Double {
        return (10000L - ((loadDataList.map { it.waitTime }.max() ?: 0L) * 100) - ((loadDataList.map { it.initialBufferSize }.max()?.toLong() ?: 0L))).toDouble()
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
