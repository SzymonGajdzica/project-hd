package pl.polsl.strategy

class RenewLoadStrategy: LoadStrategy() {

    override fun getNumberOfPages(remainingElements: Int): Int {
        return if (remainingElements <= maxBufferSize - pageSize) 1 else 0
    }

    override fun getInitialNumberOfPages(): Int {
        return maxBufferSize / pageSize
    }

}