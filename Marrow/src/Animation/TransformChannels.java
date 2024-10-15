package Animation;

public enum TransformChannels{
    x(0),
    y(1),
    scaleX(2),
    scaleY(3),
    rotation(4),
    shear(5),
    opacity(6);
    private final int value;
    private TransformChannels(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}