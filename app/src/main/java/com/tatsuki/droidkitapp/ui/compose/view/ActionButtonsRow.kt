package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.droidkitapp.R

@Composable
fun ActionButtonsRow(
  positiveButtonLabelId: Int = R.string.set_button_label,
  negativeButtonLabelId: Int = R.string.reset_button_label,
  onClickPositive: () -> Unit,
  onClickNegative: () -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center
  ) {
    Button(
      onClick = { onClickPositive() },
      content = {
        Text(text = stringResource(id = positiveButtonLabelId))
      },
    )
    Spacer(modifier = Modifier.size(16.dp))
    Button(
      onClick = { onClickNegative() },
      content = {
        Text(text = stringResource(id = negativeButtonLabelId))
      },
    )
  }
}

@Preview
@Composable
private fun PreviewActionButtonsRow() {
  ActionButtonsRow(
    onClickPositive = {},
    onClickNegative = {},
  )
}
