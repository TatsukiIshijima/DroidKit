package com.tatsuki.droidkitapp.ui.compose.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tatsuki.droidkitapp.R

@Composable
fun SoundSection(
  modifier: Modifier,
  onClickTypeNo: (Int) -> Unit,
) {
  SectionFrame(
    modifier = modifier,
    titleResource = R.string.sound_section_title,
    body = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = stringResource(id = R.string.sound_type_label))
        Spacer(modifier = Modifier.size(16.dp))
        Button(
          onClick = { onClickTypeNo(0) },
          content = {
            Text(text = stringResource(id = R.string.set_button_label))
          },
        )
      }
    }
  )
}

@Preview
@Composable
private fun PreviewSoundSection() {
  SoundSection(
    modifier = Modifier.fillMaxWidth(),
    onClickTypeNo = {},
  )
}
