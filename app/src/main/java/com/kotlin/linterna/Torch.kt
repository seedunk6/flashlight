package com.kotlin.linterna

import android.hardware.Camera

class Torch {
    var camera: Camera
    var parameters: Camera.Parameters
    var isOn: Boolean

    // Constructor
    init {
        camera = Camera.open()
        parameters = camera.parameters
        camera.startPreview()
        isOn = false
    }

    // Turn on flashlight
    fun on(){
        if(!isOn){
            isOn = true
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            camera.parameters = parameters
        }
    }

    // Turn off flashlight
    fun off(){
        if(isOn){
            isOn = false
            parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
            camera.parameters = parameters
        }
    }

    // Disconnect the resources from the camera
    fun release(){
        camera.stopPreview()
        camera.release()
    }

    // Check if the flash is on
    fun isOnTorch(): Boolean {
        return (parameters.flashMode == Camera.Parameters.FLASH_MODE_TORCH)
    }
}