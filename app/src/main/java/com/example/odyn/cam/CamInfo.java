/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

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

/**
 * Jest to klasa zawierająca informacje z kamery.
 */
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

	/**
	 * Jest to metoda odpowiedzialna za otrzymywanie informacji o polu widzenia.
	 */
	public float getFOV() {
		return FOV;
	}

	/**
	 * Jest to metoda odpowiedzialna za otrzymywanie informacji o szerokości.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Jest to metoda odpowiedzialna za otrzymywanie informacji o wysokości.
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Jest to metoda odpowiedzialna za otrzymywanie informacji o wymiarach.
	 */
	public float[] getDimensions() {
		return new float[]{width, height};
	}

	/**
	 * Jest to metoda odpowiedzialna za otrzymywanie informacji o bitmapie.
	 */
	public Bitmap getBMP() {
		return BMP;
	}
}
