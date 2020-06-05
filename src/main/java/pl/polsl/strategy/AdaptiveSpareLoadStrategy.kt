package pl.polsl.strategy

import kotlin.math.min

class AdaptiveSpareLoadStrategy : LoadStrategy() {

    private var pagesToLoad = 1

    override fun getNumberOfPages(remainingElements: Int): Int {
        return if (remainingElements < pageSize * pagesToLoad) pagesToLoad else 0
    }

    override fun getInitialNumberOfPages(): Int {
        return pagesToLoad * 2
    }

    override val logMessage: String?
        get() = "pagesToLoad=$pagesToLoad"

    override fun analyzeData(loadData: LoadData) {
        super.analyzeData(loadData)
        if(loadData.waitTime != 0L)
            pagesToLoad = min(pagesToLoad + 1, maxBufferSize / pageSize / 2)
    }

}