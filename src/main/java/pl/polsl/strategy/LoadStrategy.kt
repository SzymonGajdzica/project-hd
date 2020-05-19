package pl.polsl.strategy

import pl.polsl.main.Main
import java.util.*

abstract class LoadStrategy() {

    protected val pageSize = Main.pageSize
    protected val waitTimes = LinkedList<Long>()
    protected val bufferSizes = LinkedList<Int>()

    abstract fun getNumberOfPages(remainingElements: Int): Int

    abstract fun getInitialNumberOfPages(): Int

    open fun analyzeData(waitTime: Long, bufferSize: Int) {
        bufferSizes.add(bufferSize)
        waitTimes.add(waitTime)
    }

    open fun generateReport(): String {
        return "Average wait time = ${waitTimes.average()}, Max wait time ${waitTimes.max()}, " +
                "Average buffer size = ${bufferSizes.average()}, Max buffer size ${bufferSizes.max()}"
    }

}
