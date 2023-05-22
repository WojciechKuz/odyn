package com.example.odyn.cam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.core.content.ContextCompat;

import java.nio.ByteBuffer;

public class CamInfo {
	private float FOV = 0;
	private float width = 0;
	private float height = 0;
	private Bitmap BMP = null;

	public CamInfo(float FOV, float width, float height, Bitmap BMP) {
		this.FOV = FOV;
		this.width = width;
		this.height = height;
		this.BMP = BMP;
	}
	public CamInfo(float FOV, float width, float height) {
		this.FOV = FOV;
		this.width = width;
		this.height = height;
	}

	public float getFOV() {
		return FOV;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public float[] getDimensions() {
		return new float[]{width, height};
	}

	public Bitmap getBMP() {
		return BMP;
	}
}
