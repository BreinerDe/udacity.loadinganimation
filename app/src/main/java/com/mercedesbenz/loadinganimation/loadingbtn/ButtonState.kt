package com.mercedesbenz.loadinganimation.loadingbtn


sealed class ButtonState {
    data object ReadyToDownload : ButtonState()
    data object Loading : ButtonState()
    data object Completed : ButtonState()
}