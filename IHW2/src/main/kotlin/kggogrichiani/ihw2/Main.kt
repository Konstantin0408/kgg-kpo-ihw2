package kggogrichiani.ihw2

import java.util.*
import kggogrichiani.ihw2.looping.Looper
import kggogrichiani.ihw2.looping.ScanningUtil
import kggogrichiani.ihw2.saving.RestaurantData
import kotlin.system.exitProcess

fun main() {
    val scanner = Scanner(System.`in`)
    val (save, saveFile) = ScanningUtil.customDeserialize<RestaurantData>(
        scanner, "data", RestaurantData.empty
    )
    val looper = Looper(scanner, save, saveFile)
    looper.run()
    exitProcess(0)
}