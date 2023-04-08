package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tatsuki.droidkit.model.DroidCommand
import com.tatsuki.droidkitapp.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItemsSingleChoice
import com.vanpra.composematerialdialogs.title

@Composable
fun SoundListDialog(
  dialogState: MaterialDialogState,
  onClickPositiveButton: () -> Unit,
) {
  val soundTypes = DroidCommand.PlaySound::class.sealedSubclasses
    .mapNotNull { it.objectInstance }
    .map { it.type.toString() }

  MaterialDialog(
    dialogState = dialogState,
    buttons = {
      positiveButton(
        text = stringResource(id = R.string.positive_button_label),
        onClick = onClickPositiveButton
      )
      negativeButton(
        text = stringResource(id = R.string.negative_button_label),
      )
    }
  ) {
    title(res = R.string.sound_section_title)
    listItemsSingleChoice(
      initialSelection = 0,
      list = soundTypes,
    )
  }
}
