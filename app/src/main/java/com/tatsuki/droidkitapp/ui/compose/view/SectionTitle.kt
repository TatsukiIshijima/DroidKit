package com.tatsuki.droidkitapp.ui.compose.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tatsuki.droidkitapp.R

@Composable
fun SectionTitle(
  modifier: Modifier,
  @StringRes textResource: Int
) {
  Text(
    text = stringResource(id = textResource),
    modifier = modifier,
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold,
    textAlign = TextAlign.Center,
  )
}

@Preview
@Composable
private fun PreviewSectionTitle() {
  SectionTitle(
    modifier = Modifier.fillMaxWidth(),
    textResource = R.string.connection_section_title,
  )
}
