package com.android.battery.saver.helper

import java.io.InputStreamReader

object ReadWriteFile {
    fun returnStringFromProcess(proc: Process): String {
        val stdInput = InputStreamReader(proc.inputStream)
        stdInput.buffered().use {
            return it.readText()
        }
    }
}