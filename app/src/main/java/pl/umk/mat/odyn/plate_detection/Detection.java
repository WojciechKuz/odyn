package pl.umk.mat.odyn.plate_detection;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import pl.umk.mat.odyn.cam.CamInfo;
import pl.umk.mat.odyn.ml.Detect;

public class Detection {

    int imageSize = 320;

    public float plateDetection(CamInfo caminfo, Context context) {
        try {
           Bitmap image = caminfo.getBMP();
            Detect model = Detect.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 320, 320, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }
            image.recycle();    //powinno rozwiazac problem z zamykaniem image po zbadaniu zdjecia
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Detect.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature1 = outputs.getOutputFeature1AsTensorBuffer(); // Assuming the bounding box coordinates are in the second output feature

            float[] confidences = outputFeature0.getFloatArray();
            float[] boundingBoxes = outputFeature1.getFloatArray();

            // Find the index of the bounding box with the highest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            // Retrieve the bounding box coordinates for the detected object with the best confidence
            int offset = maxPos * 4;
            float xmin = boundingBoxes[offset];
            float ymin = boundingBoxes[offset + 1];
            float xmax = boundingBoxes[offset + 2];
            float ymax = boundingBoxes[offset + 3];


            BoundingBox bestBoundingBox = new BoundingBox(xmin, ymin, xmax, ymax);

            float boxWidth = (xmax - xmin) * 320;
            float boxHeight = (ymax - ymin) * 320;
            DistanceCalculator distanceCalculator = new DistanceCalculator(520, 114);
            float distance = distanceCalculator.calculateDistance(boxWidth,boxHeight, caminfo.getWidth(), caminfo.getHeight(), caminfo.getFOV());
            model.close();
            return distance;

            // Releases model resources if no longer used.

        } catch (IOException e) {
            // TODO Handle the exception
        }

        return 0;

    }





}
