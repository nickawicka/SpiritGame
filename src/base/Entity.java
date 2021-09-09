package base;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Entity {

    Point2D position;
    float rotation;
    float scale = 1;
    double width;
    double height;

    Image entity_image;

    public Entity(Image entity_image) {
        this.entity_image = entity_image;
        this.width = entity_image.getWidth();
        this.height = entity_image.getHeight();
    }

    /* ************************************************************************************************************
     *                                                  POSITIONAL                                                *
     ************************************************************************************************************ */

    public Point2D getDrawPosition() {
        return position;
    }

    public void setDrawPosition(float x, float y) {
        this.position = new Point2D(x, y);
    }

    private void rotate(float rotation) {
        this.rotation += rotation;
    }

    private void move(Point2D vector) {
        this.position = this.position.add(vector);
    }

    public float getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public Point2D getCenter() {
        Point2D pos = getDrawPosition();
        return new Point2D(pos.getX() + width / 2, pos.getY() + height / 2);
    }

    /* ************************************************************************************************************
     *                                                    IMAGE                                                   *
     ************************************************************************************************************ */

    public Image getImage() {
        return entity_image;
    }

    public double getWidth() {
        return this.width * getScale();
    }

    public double getHeight() {
        return this.height * getScale();
    }

    /* ************************************************************************************************************
     *                                                   MOVEMENT                                                 *
     ************************************************************************************************************ */

    private float MAX_SPEED = 5f;
    private Point2D current_thrust_vector = new Point2D(0, 0);

    private float MAX_TORQUE = 5f;
    private float current_torque_force = 0;

    public void addTorque(float torqueForce) {
        float newTorque = current_torque_force + torqueForce;
        if (torqueForce > 0) {
        	current_torque_force = Math.min(newTorque, MAX_TORQUE);
        } else {
        	current_torque_force = Math.max(newTorque, -MAX_TORQUE);
        }
    }

    public void addThrust(double scalar) {
        addThrust(scalar, getRotation());
    }

    private void addThrust(double scalar, double angle) {
        Point2D thrust_vector = calculateNewThrustVector(scalar, Math.toRadians(-angle));
        current_thrust_vector = current_thrust_vector.add(thrust_vector);
        current_thrust_vector = clampToMaxSpeed(current_thrust_vector);
    }

    private Point2D calculateNewThrustVector(double scalar, double angle) {
        return new Point2D(
                (float) (Math.sin(angle) * scalar),
                (float) (Math.cos(angle) * scalar));
    }

    private Point2D clampToMaxSpeed(Point2D thrust_vector) {
        if (thrust_vector.magnitude() > MAX_SPEED) {
            return current_thrust_vector = thrust_vector.normalize().multiply(MAX_SPEED);
        } else {
            return current_thrust_vector = thrust_vector;
        }
    }

    private void applyDrag() {
        float movement_drag = current_thrust_vector.magnitude() < 0.5 ? 0.01f : 0.07f;
        float rotation_drag = current_torque_force < 0.2f ? 0.05f : 0.1f;

        current_thrust_vector = new Point2D(
                reduceTowardsZero((float) current_thrust_vector.getX(), movement_drag),
                reduceTowardsZero((float) current_thrust_vector.getY(), movement_drag));

        current_torque_force = reduceTowardsZero(current_torque_force, rotation_drag);
    }

    private float reduceTowardsZero(float value, float modifier) {
        float newValue = 0;
        if (value > modifier) {
            newValue = value - modifier;
        } else if (value < -modifier) {
            newValue = value + modifier;
        }
        return newValue;
    }

    public void update() {
        applyDrag();
        move(current_thrust_vector);
        rotate(current_torque_force);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}