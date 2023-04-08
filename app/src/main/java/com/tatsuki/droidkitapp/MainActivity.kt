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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.tatsuki.droidkit.model.DroidCommand
import com.tatsuki.droidkitapp.ui.compose.view.ColorSection
import com.tatsuki.droidkitapp.ui.compose.view.ConnectionSection
import com.tatsuki.droidkitapp.ui.compose.view.MovementSection
import com.tatsuki.droidkitapp.ui.compose.view.RotationSection
import com.tatsuki.droidkitapp.ui.compose.view.SoundSection
import com.tatsuki.droidkitapp.ui.theme.DroidKitAppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

  private val mainViewModel by viewModels<MainViewModel>()

  // TODO:ViewModelへ移行
  private val mutableIsGrantedFineLocationPermissionStateFlow = MutableStateFlow(false)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // TODO:ViewModelへ移行
    val requestPermissionLauncher =
      registerForActivityResult(RequestPermission()) { isGranted ->
        mutableIsGrantedFineLocationPermissionStateFlow.value = isGranted
      }
    requestPermissionLauncher.launch(
      Manifest.permission.ACCESS_FINE_LOCATION
    )

    setContent {

      val scrollState = rememberScrollState()
      val colorPickerController = rememberColorPickerController()

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
                value = 0f,
                onValueChange = {},
                onClickGo = {},
                onClickBack = {},
              )
            },
            rotationSection = {
              RotationSection(
                modifier = Modifier.fillMaxWidth(),
                value = 0f,
                onValueChange = {},
                onClickTurn = {},
                onClickRest = {},
              )
            },
            colorSection = {
              ColorSection(
                modifier = Modifier.fillMaxWidth(),
                colorPickerController = colorPickerController,
                onColorChanged = {},
                onClickSet = {},
                onClickReset = {},
              )
            },
            soundSection = {
              SoundSection(
                modifier = Modifier.fillMaxWidth(),
                onClickSet = {
                  mainViewModel.playSound(DroidCommand.PlaySound.S1)
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
          onClickBack = {},
        )
      },
      rotationSection = {
        RotationSection(
          modifier = Modifier.fillMaxWidth(),
          value = 0f,
          onValueChange = {},
          onClickTurn = {},
          onClickRest = {},
        )
      },
      colorSection = {
        ColorSection(
          modifier = Modifier.fillMaxWidth(),
          colorPickerController = rememberColorPickerController(),
          onColorChanged = {},
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
