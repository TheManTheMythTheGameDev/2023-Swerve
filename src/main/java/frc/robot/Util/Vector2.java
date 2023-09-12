package frc.robot.Util;

public class Vector2 {
    public double x;
    public double y;

    public static Vector2 fromAngleAndMag(double angle, double magnitude) {
        return new Vector2(angle).multiply(magnitude);
    }

    public Vector2(double angle) {
        var radians = AngleMath.conformAngle(angle) / 360 * (2 * Math.PI);
        x = Math.cos(radians);
        y = Math.sin(radians);
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 multiply(Vector2 other) {
        return new Vector2(x * other.x, y * other.y);
    }

    public Vector2 multiply(double other) {
        return multiply(new Vector2(other, other));
    }

    /**
     * Gets the angle in standard position
     * 
     * @return the angle in degrees
     */
    public double getAngleDeg() {
        double angle = Math.atan2(y, x);
        angle = Math.toDegrees(angle);
        angle = (angle + 360) % 360;
        return angle;
    }

    public double getTurnAngleDeg() {
        return AngleMath.conformAngle(-1 * (getAngleDeg() - 90));
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(
                x + other.x,
                y + other.y);
    }

    public double dotProduct(Vector2 other) {
        return x * other.x + y * other.y;
    }

    public Vector2 rotate(double degrees) {
        return Vector2.fromAngleAndMag(getAngleDeg() + degrees, getMagnitude());
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
