package pl.umk.mat.odyn.plate_detection;

import static java.lang.Float.NaN;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import pl.umk.mat.odyn.cam.CamInfo;
import pl.umk.mat.odyn.ml.Detect;

/**
 * Klasa odpowiadająca za analizę obrazu i wyznaczanie odległości
 */
public class Detection {

    int imageSize = 320;

    /**
     * Ta metoda na podstawie informacji z kamery oblicza odległość od pojazdu z przodu
     * @param caminfo informacje z kamery
     * @param context kontekst aplikacji
     * @return odległość od pojazdu z przodu
     */
    public float plateDetection(CamInfo caminfo, Context context) {
        try {
            Bitmap image = caminfo.getBMP();
            if(image == null) {
                Log.e("Detection", ">>> Uwaga, bitmapa jest nullem");
                //return NaN;
            }
            Detect model = Detect.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 320, 320, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            //adjusting input image
            if(image.getHeight()>image.getWidth())
            {
                int dimension = Math.min(image.getHeight(), image.getWidth());
                image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
                image = Bitmap.createScaledBitmap(image, imageSize,imageSize, true);
                Matrix matrix = new Matrix();
                matrix.postRotate(270);
                image = Bitmap.createBitmap(image, 0, 0, image.getWidth(),image.getHeight() , matrix, true);
            }
            int dimension = Math.min(image.getHeight(), image.getWidth());
            image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
            image = Bitmap.createScaledBitmap(image, imageSize,imageSize, true);


            int[] intValues = new int[image.getWidth() * image.getHeight()];
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
            //System.out.println(caminfo.getHeight() + "wysokosc");
            //System.out.println(caminfo.getWidth() + "szerokosc");
            System.out.println(maxConfidence);
            if(maxConfidence > 0.5) {
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
                float distance = distanceCalculator.calculateDistance(boxWidth, boxHeight, caminfo.getWidth(), caminfo.getHeight(), caminfo.getFOV());
                model.close();
                return distance/1000;
            }else{
                return 0;
            }

            // Releases model resources if no longer used.

        } catch (IOException e) {
            Log.e("Detection", ">>> Error w Detection\n" + e); // pisze do logów
            // TODO Handle the exception
        } catch (NullPointerException e) {
            Log.e("Detection", ">>> Error w Detection, coś może być nullem!\n" + e);
        }

        return 0;

    }
}
