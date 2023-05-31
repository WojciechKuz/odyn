package pl.umk.mat.odyn.plate_detection;

public class BoundingBox {
    float xmin, ymin, xmax, ymax;

    public BoundingBox(float xmin, float ymin, float xmax, float ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }
}