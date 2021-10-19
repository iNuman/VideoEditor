package com.numan.videoeditor.db.dataclass

import android.graphics.Typeface

/**
 * Created by soham on 9/8/2017.
 */
class TEXT_MODEL(var name: String, typeface: Typeface?, colors: Int) {
    var typeface: Typeface? = null
    var colors: Int

    init {
        this.typeface = typeface
        this.colors = colors
    }
}