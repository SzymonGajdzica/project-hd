package pl.polsl.strategy

class FullLoadStrategy: LoadStrategy() {

    private val pagesToLoad = maxBufferSize / pageSize / 2

    override fun getNumberOfPages(remainingElements: Int): Int {
        return if (remainingElements <= maxBufferSize - (pageSize * pagesToLoad)) pagesToLoad else 0
    }

    override fun getInitialNumberOfPages(): Int {
        return maxBufferSize / pageSize
    }

}