package Animation;

public class Transform2D {
    public double x = 0;
    public double y = 0;
    public double scaleX = 1;
    public double scaleY = 1;
    public double rotation = 0;
    public double centerX = 0;
    public double centerY = 0;
    public double shear = 0;
    public double opacity = 100;

    public Transform2D()
    {

    }

    public Transform2D(double x, double y, double scaleX, double scaleY, double rotation, double rotationCenterX, double rotationCenterY, double shear, double opacity) {
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.centerX = rotationCenterX;
        this.centerY = rotationCenterY;
        this.shear = shear;
        this.opacity = opacity;
    }
}
