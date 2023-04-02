package com.tatsuki.droidkit

import android.bluetooth.BluetoothDevice
import com.tatsuki.droidkit.model.DroidCommand

interface DroidConnector {

  suspend fun connect(device: BluetoothDevice)

  suspend fun disconnect()

  suspend fun writeValue(command: DroidCommand)
}