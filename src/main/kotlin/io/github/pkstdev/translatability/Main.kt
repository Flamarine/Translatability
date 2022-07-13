package io.github.pkstdev.translatability

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Parameters: <inputFile> <outputFile>")
    }
    val inputFile = File(args[0])
    val outputFile = File(args[1])
    if (inputFile.exists() && inputFile.toString().endsWith(".json")) {
        val translations: Map<String, String>? = readJsonFile(inputFile.toString())
        val consoleInput = Scanner(System.`in`)
        if (translations != null) {
            val translatedEntries = HashMap<String, String>()
            for (translation in translations) {
                val translationKey = translation.key
                val translationValue = translation.value
                println("Translation Key: $translationKey")
                println("Translation Value from Original: $translationValue")
                println("Your Translation Entry:")
                if (consoleInput.hasNextLine()) {
                    val translatedValue = consoleInput.nextLine()
                    translatedEntries[translationKey] = translatedValue
                }
            }
            consoleInput.close()
            if(!outputFile.parentFile.exists()){
                outputFile.parentFile.mkdirs()
            }
            if(outputFile.exists()){
                outputFile.delete()
            }
            outputFile.createNewFile()
            val gson = GsonBuilder().setPrettyPrinting().create()
            val json = gson.toJson(translatedEntries)
            val writer: Writer = OutputStreamWriter(FileOutputStream(outputFile), StandardCharsets.UTF_8)
            writer.write(json)
            writer.flush()
            writer.close()
            println("Done translating work with " + translatedEntries.size + " entries.")
        } else {
            println("Json file does not contain any values!")
        }
    } else {
        println("Input file does not exist or is not a valid Json file!")
    }
}

fun readJsonFile(fileName: String): Map<String, String>? {
    val gson = Gson()
    val json: String
    return try {
        val file = File(fileName)
        val reader: Reader = InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)
        var ch: Int
        val buffer = StringBuffer()
        while (reader.read().also { ch = it } != -1) {
            buffer.append(ch.toChar())
        }
        reader.close()
        json = buffer.toString()
        gson.fromJson<Map<String, String>>(json, MutableMap::class.java)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}