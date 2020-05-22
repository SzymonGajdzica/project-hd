package pl.polsl.strategy

class TriggerLoadStrategy : LoadStrategy() {

    override fun getNumberOfPages(remainingElements: Int): Int {
        return if (remainingElements <= 1) 1 else 0
    }

    override fun getInitialNumberOfPages(): Int {
        return 1
    }

}