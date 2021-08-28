package ru.geekbrains.notes

import android.content.res.Resources

class ColorManager(var resources: Resources) {
    fun getColorIdFromResourcesArray(noteColorId: Int): Int {
        val colors = imageArray
        return colors[noteColorId]
    }

    private val imageArray: IntArray
        get() {
            val colors = resources.obtainTypedArray(R.array.note_colors)
            val length = colors.length()
            val answer = IntArray(length)
            for (i in 0 until length) {
                answer[i] = colors.getResourceId(i, 0)
            }
            colors.recycle()
            return answer
        }
}