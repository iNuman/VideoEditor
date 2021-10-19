package com.numan.videoeditor.ui.dialog_fragments

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.numan.videoeditor.R
import com.numan.videoeditor.utils.Constants.KEY_DIALOG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ActionsToBePerformedDialogFragment : DialogFragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var yesListener: ((selectedItemPos: Int) -> Unit)? = null

    fun setYesListener(listener: (selectedItemPos: Int) -> Unit) {
        yesListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val options = arrayOf("Positive", "Negative")
        var selectedItem = sharedPreferences.getInt(KEY_DIALOG, 0)

        return MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
            .setTitle(getString(R.string.add_dialog_title_dialog))
            .setIcon(R.drawable.ic_remove)
            .setSingleChoiceItems(options, selectedItem) { dialogInterface, item: Int ->
                selectedItem = item
                yesListener?.let { yes ->
                    when (selectedItem) {
                        0 -> {
                            yes(selectedItem)
                        }
                        1 -> {
                            yes(selectedItem)

                        }
                    }
                    dialogInterface.cancel()
                }
            }

            .create()

    }

}