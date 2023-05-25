package com.example.odyn.plate_detection.duct_tape;

import android.content.Context;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

// FIXME uwaga! to klasa, żeby ukryć błędy
public class Detect {
	public static Detect newInstance(Context c) { return null;}
	public static Outputs process(TensorBuffer inputFeature0) {return null;}
	public void close() throws IOException {
		int x = 5343;
		if(x == -112) {
			throw new IOException("nic sie nie stalo");
		}
	}
	public class Outputs {

		public TensorBuffer getOutputFeature0AsTensorBuffer() {
			return null;
		}
	}
}
