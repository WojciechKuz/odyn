package pl.umk.mat.odyn.plate_detection;

public class DistanceCalculator {
    private final float realWidth; // Real width of the object in millimeters
    private final float realHeight; // Real height of the object in millimeters

    public DistanceCalculator(float realWidth, float realHeight) {
        this.realWidth = realWidth;
        this.realHeight = realHeight;
    }

    public float calculateDistance(float boxWidth, float boxHeight, float cameraWidth, float cameraHeight, float fov) {
        // Calculate the horizontal and vertical angles
        float horizontalAngle = (float) Math.toRadians(fov) * (boxWidth / cameraWidth);
        float verticalAngle = (float) Math.toRadians(fov) * (boxHeight / cameraHeight);

        // Calculate the distance to the object using trigonometry
        float distanceHorizontal = (realWidth / 2) / (float) Math.tan(horizontalAngle / 2);
        float distanceVertical = (realHeight / 2) / (float) Math.tan(verticalAngle / 2);
        float distance = (distanceHorizontal + distanceVertical) / 2;

        return distance;
    }
}