package kggogrichiani.ihw2.looping

import kotlinx.serialization.SerializationException
import java.io.File
import java.util.Scanner
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.FileNotFoundException
import java.lang.NumberFormatException
import kotlin.io.path.Path
import kotlin.io.path.createFile

object ScanningUtil {
    fun getLine(scanner: Scanner, string: String = ""): String {
        print(string)
        return scanner.nextLine()
    }

    fun safeStrToInt(string: String) = try {
        string.toInt()
    } catch (ex: NumberFormatException) {
        null
    }

    fun getInt(scanner: Scanner, string: String = "") : Int? {
        val str = getLine(scanner, string)
        return safeStrToInt(str)
    }

    fun getYN(scanner: Scanner, string: String = "") : Boolean {
        var str = getLine(scanner, string)
        while (str != "y" && str != "n") str = getLine(scanner,"(y/n) ")
        return str == "y"
    }

    inline fun <reified T> saveToFile(contents: T, file: File) {
        file.writeText(Json.encodeToString(contents))
    }

    inline fun <reified T> customCreateFile(
        scanner: Scanner,
        typeDescription: String,
        defaultContents: T
    ) : Pair<T, File> {

        val result: T = defaultContents
        var customFile: File

        while (true) {
            val customPath = getLine(
                scanner,
                "Enter path to create file with $typeDescription: "
            )
            customFile = File(customPath)
            if (!customFile.exists()) {
                Path(customPath).createFile()
                customFile = File(customPath)
            }
            if (!customFile.exists()) {
                println("Cannot create file, try again")
                continue
            }
            try {
                customFile.writeText(Json.encodeToString(result))
            } catch (ex: FileNotFoundException) {
                println("A problem occurred. Make sure you are entering a path to a FILE, not to a FOLDER")
                continue
            }
            break
        }
        return Pair(result, customFile)
    }


    inline fun <reified E> customDeserialize(
        scanner: Scanner,
        typeDescription: String,
        defaultContents: E
    ) : Pair<E, File> {

        var result : E
        var customFile: File

        while (true) {
            val customPath = getLine(
                scanner,
                "Enter path to file with $typeDescription (or enter '#' to create a new one): "
            )
            if (customPath == "#") return customCreateFile(scanner, typeDescription, defaultContents)
            customFile = File(customPath)
            if (!customFile.exists()) {
                println("File does not exist. Try again")
                continue
            }
            try {
                result = Json.decodeFromString<E>(customFile.readText())
                break
            } catch (ex: SerializationException) {
                println("Could not deserialize. Enter '#' to overwrite file")
                continue
            } catch (ex: FileNotFoundException) {
                println("A problem occurred. Make sure you are entering a path to a FILE, not to a FOLDER")
                continue
            }

        }
        return Pair(result, customFile)
    }
}