package com.example.odyn.plate_detection;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.odyn.ml.Detect;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Detection {

    int imageSize = 320;

    public void plateDetection(Bitmap image, Context context){

        try {

            Detect model = Detect.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 320, 320, 3}, DataType.FLOAT32);

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());



            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(),0 ,0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for(int i =0; i < imageSize; i ++){
                for(int j =0; j < imageSize; j ++){
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f/255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f/255));
                    byteBuffer.putFloat((val  & 0xFF) * (1.f/255));
                }


            }

            inputFeature0.loadBuffer(TensorImage.fromBitmap(image).getBuffer());

            // Runs model inference and gets result.
            Detect.Outputs outputs = model.process(inputFeature0);


            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            List<Integer> results = new ArrayList<Integer>(outputFeature0.getShape().length);



            for(int i : outputFeature0.getShape()){
                results.add(i);
            }

            for(int i =0; i < results.size(); i++){
                System.out.println(results.get(i));
            }

            System.out.println();


            float[] confidence = outputFeature0.getFloatArray();
            if(confidence[0] > 0.8) {
                System.out.println("plate");
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }



    }
}
