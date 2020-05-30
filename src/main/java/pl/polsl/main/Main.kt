package pl.polsl.main

import pl.polsl.data.manager.Connector
import pl.polsl.data.source.DataSource
import pl.polsl.data.source.DataSourceInfo
import pl.polsl.strategy.LoadStrategy
import pl.polsl.user.UserSimulator
import pl.polsl.user.UserSimulatorInfo

object Main {

    var maxBufferSize = 100
        private set
    var pageSize = 10
        private set

    @JvmStatic
    fun main(args: Array<String>) {
        val dataSourceInfo = DataSourceInfo(
                numberOfProducers = 4,
                produceTimeRange = 2L..4L,
                connectionTimeRange = 100L..120L,
                singleDataLoadRange = 15L..20L
        )
        val userSourceInfo = UserSimulatorInfo(
                fetchedElementsByUserRange = 10..1000,
                maxNumberOfUsers = 100,
                userSpawnTimeRange = 1L..100L,
                userStaticAskDelayRange = 5L..50L,
                userDynamicAskDelayRange = 0L..10L
        )
        val dataSource = DataSource(dataSourceInfo)
        val loadStrategyType = LoadStrategy.Type.ADAPTIVE
        val connector = Connector(dataSource, loadStrategyType)
        val userSimulator = UserSimulator(connector, userSourceInfo)
        Logger.initialize(userSourceInfo, dataSourceInfo, LoadStrategy.getLoadStrategy(loadStrategyType).javaClass.simpleName)
        userSimulator.startSimulation()
    }

}