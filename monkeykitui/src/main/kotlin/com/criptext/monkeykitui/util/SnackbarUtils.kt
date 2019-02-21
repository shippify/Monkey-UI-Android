package com.criptext.monkeykitui.util

import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.criptext.monkeykitui.R

/**
 * Created by gesuwall on 9/15/16.
 */

class SnackbarUtils {
    companion object {
        fun showUndoMessage(recycler: androidx.recyclerview.widget.RecyclerView, msg: String, undoAction: (View) -> Unit,
                            callback: Snackbar.Callback){
            val snack = Snackbar.make(recycler, msg, Snackbar.LENGTH_LONG)
                snack.setCallback(callback)
                snack.setAction(recycler.context.getString(R.string.mk_undo), undoAction)
                snack.show()
        }
    }
}