package kggogrichiani.ihw2.looping

interface LooperState {
    fun description() : String
    fun getAllCommands(looper: Looper) : List<LoopingCommand>
}
