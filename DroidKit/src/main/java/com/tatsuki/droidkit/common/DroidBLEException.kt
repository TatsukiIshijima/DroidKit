package com.tatsuki.droidkit.common

sealed class DroidBLEException : Exception() {
  abstract val errorCode: Int

  data class NotSupport(
    override val errorCode: Int = NOT_SUPPORTED_BLE_ERROR,
    override val message: String = "This device is not supported BLE."
  ) : DroidBLEException()

  data class NotGrantedPermission(
    override val errorCode: Int = NOT_GRANTED_PERMISSION_ERROR,
    override val message: String? = "App does not have requirement permissions."
  ) : DroidBLEException()

  data class NotEnabled(
    override val errorCode: Int = NOT_ENABLED_BLE_ERROR,
    override val message: String? = "This device is not enabled BLE."
  ) : DroidBLEException()

  data class ScanFailed(
    override val errorCode: Int
  ) : DroidBLEException()

  data class NotConnectedDevice(
    override val errorCode: Int = NOT_CONNECTED_DEVICE_ERROR
  ) : DroidBLEException()

  data class GattOperationFailed(
    override val errorCode: Int
  ) : DroidBLEException()

  companion object {
    const val NOT_SUPPORTED_BLE_ERROR = -1
    const val NOT_GRANTED_PERMISSION_ERROR = -2
    const val NOT_ENABLED_BLE_ERROR = -3
    const val NOT_CONNECTED_DEVICE_ERROR = -4
  }
}