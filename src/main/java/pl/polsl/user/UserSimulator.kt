package pl.polsl.user

import pl.polsl.data.manager.Connector
import java.util.*

class UserSimulator(
        private val connector: Connector,
        private val userSimulatorInfo: UserSimulatorInfo
) {

    private var numberOfUsers = 0

    private val userCreatorThread = Thread {
        while (numberOfUsers < userSimulatorInfo.maxNumberOfUsers) {
            Thread.sleep(userSimulatorInfo.userSpawnTimeRange.random())
            numberOfUsers++
            createNewUserThread(numberOfUsers)
        }
    }

    fun startSimulation() {
        userCreatorThread.start()
    }

    private fun createNewUserThread(id: Int) {
        Thread {
            val dataManager = connector.connect(Date().apply { time -= 5000L })
            dataManager.initialize()
            val maxNumberOfElements = userSimulatorInfo.fetchedElementsByUserRange.random()
            val staticWaitTime = userSimulatorInfo.userStaticAskDelayRange.random()
            var numberOfElements = 0
            while (numberOfElements < maxNumberOfElements) {
                Thread.sleep(staticWaitTime + userSimulatorInfo.userDynamicAskDelayRange.random())
                dataManager.next.apply {
                    //display data
                }
                numberOfElements++
            }
            dataManager.finish(id, staticWaitTime)
        }.start()
    }

}