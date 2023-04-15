package com.tatsuki.droidkitapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tatsuki.droidkitapp.ui.compose.view.ColorSection
import com.tatsuki.droidkitapp.ui.compose.view.ConnectionSection
import com.tatsuki.droidkitapp.ui.compose.view.MovementSection
import com.tatsuki.droidkitapp.ui.compose.view.RotationSection
import com.tatsuki.droidkitapp.ui.compose.view.SoundListDialog
import com.tatsuki.droidkitapp.ui.compose.view.SoundSection
import com.tatsuki.droidkitapp.ui.theme.DroidKitAppTheme
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

class MainActivity : ComponentActivity() {

  private val mainViewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val requestPermissionLauncher = registerForActivityResult(RequestPermission()) { }
    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

    setContent {

      val scrollState = rememberScrollState()
      val colorPickerController = rememberColorPickerController()
      val dialogState = rememberMaterialDialogState()

      SoundListDialog(
        dialogState = dialogState,
        initialSection = mainViewModel.selectedSoundStateFlow.collectAsState().value,
        onChoiceChange = { position ->
          mainViewModel.onChangeSound(position)
        },
        onClickPositiveButton = {
          mainViewModel.playSound()
        },
      )

      DroidKitAppTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colors.background
        ) {
          Body(
            modifier = Modifier
              .fillMaxWidth()
              .padding(48.dp)
              .verticalScroll(scrollState),
            connectSection = {
              ConnectionSection(
                modifier = Modifier.fillMaxWidth(),
                onClickConnect = {
                  mainViewModel.connect()
                },
                onClickDisconnect = {
                  mainViewModel.disconnect()
                },
              )
            },
            movementSection = {
              MovementSection(
                modifier = Modifier.fillMaxWidth(),
                value = mainViewModel.speedStateFlow.collectAsState().value,
                onValueChange = {
                  mainViewModel.onChangeSpeed(it)
                },
                onClickGo = {
                  mainViewModel.go()
                },
                onClickStop = {
                  mainViewModel.stop()
                },
                onClickBack = {
                  mainViewModel.back()
                },
              )
            },
            rotationSection = {
              RotationSection(
                modifier = Modifier.fillMaxWidth(),
                value = mainViewModel.degreeStateFlow.collectAsState().value,
                onValueChange = {
                  mainViewModel.onChangeDegree(it)
                },
                onClickTurn = {
                  mainViewModel.turn()
                },
                onClickReset = {
                  mainViewModel.reset()
                },
              )
            },
            colorSection = {
              ColorSection(
                modifier = Modifier.fillMaxWidth(),
                colorPickerController = colorPickerController,
                onClickSet = { selectedColor ->
                  mainViewModel.changeLEDColor(selectedColor)
                },
                onClickReset = { selectedColor ->
                  mainViewModel.changeLEDColor(selectedColor)
                },
              )
            },
            soundSection = {
              SoundSection(
                modifier = Modifier.fillMaxWidth(),
                onClickSet = {
                  dialogState.show()
                }
              )
            },
          )
        }
      }
    }
  }

  override fun onStop() {
    super.onStop()

    mainViewModel.disconnect()
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  DroidKitAppTheme {
    Body(
      modifier = Modifier.fillMaxWidth(),
      connectSection = {
        ConnectionSection(
          modifier = Modifier.fillMaxWidth(),
          onClickConnect = {},
          onClickDisconnect = {},
        )
      },
      movementSection = {
        MovementSection(
          modifier = Modifier.fillMaxWidth(),
          value = 0f,
          onValueChange = {},
          onClickGo = {},
          onClickStop = {},
          onClickBack = {},
        )
      },
      rotationSection = {
        RotationSection(
          modifier = Modifier.fillMaxWidth(),
          value = 0f,
          onValueChange = {},
          onClickTurn = {},
          onClickReset = {},
        )
      },
      colorSection = {
        ColorSection(
          modifier = Modifier.fillMaxWidth(),
          colorPickerController = rememberColorPickerController(),
          onClickSet = {},
          onClickReset = {},
        )
      },
      soundSection = {
        SoundSection(
          modifier = Modifier.fillMaxWidth(),
          onClickSet = {}
        )
      },
    )
  }
}

@Composable
private fun Body(
  modifier: Modifier,
  connectSection: @Composable () -> Unit = {},
  movementSection: @Composable () -> Unit = {},
  rotationSection: @Composable () -> Unit = {},
  colorSection: @Composable () -> Unit = {},
  soundSection: @Composable () -> Unit = {},
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    connectSection()
    Spacer(modifier = Modifier.size(24.dp))
    movementSection()
    Spacer(modifier = Modifier.size(24.dp))
    rotationSection()
    Spacer(modifier = Modifier.size(24.dp))
    colorSection()
    Spacer(modifier = Modifier.size(24.dp))
    soundSection()
    Spacer(modifier = Modifier.size(24.dp))
  }
}
