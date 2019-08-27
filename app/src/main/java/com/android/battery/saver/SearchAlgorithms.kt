package com.android.battery.saver

object SearchAlgorithms {
    fun binarySearch(key: Int, clockLevels: ArrayList<Int>): Int {
        var low = 0
        var high = clockLevels.size - 1
        var middle = 0
        while (high >= low) {
            middle = (low + high) / 2
            if (clockLevels[middle] == key) {
                return middle
            }
            if (clockLevels[middle] < key) {
                low = middle + 1
            }
            if (clockLevels[middle] > key) {
                high = middle - 1
            }
        }
        return -1
    }
}