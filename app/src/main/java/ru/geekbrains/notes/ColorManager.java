package ru.geekbrains.notes;

import android.content.res.Resources;
import android.content.res.TypedArray;

public class ColorManager {
    Resources resources;

    public ColorManager(Resources resources) {
        this.resources = resources;
    }


    public int getColorIdFromResourcesArray(int noteColorId) {
        int[] colors = getImageArray();
        return colors[noteColorId];
    }

    private int[] getImageArray() {
        TypedArray colors = resources.obtainTypedArray(R.array.note_colors);
        int length = colors.length();
        int[] answer = new int[length];
        for (int i = 0; i < length; i++) {
            answer[i] = colors.getResourceId(i, 0);
        }
        colors.recycle();
        return answer;
    }
}
