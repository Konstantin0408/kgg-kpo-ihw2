package kggogrichiani.ihw2.looping

abstract class LoopingCommand(
    val looper: Looper
) {
    abstract fun name(): String
    abstract fun description(): String
    abstract fun execute()
}


