package pl.polsl.strategy

class SpareLoadStrategy : LoadStrategy() {

    override fun getNumberOfPages(remainingElements: Int): Int {
        return if (remainingElements <= pageSize) 1 else 0
    }

    override fun getInitialNumberOfPages(): Int {
        return 2
    }

}