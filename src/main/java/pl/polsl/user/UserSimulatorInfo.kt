package pl.polsl.user

data class UserSimulatorInfo(
        val maxNumberOfUsers: Int,
        val userSpawnTimeRange: LongRange,
        val fetchedElementsByUserRange: IntRange,
        val userStaticAskDelayRange: LongRange,
        val userDynamicAskDelayRange: LongRange
)