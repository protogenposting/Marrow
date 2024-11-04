package Animation;

public enum TransformChannels{
    x(0),
    y(1),
    opacity(7),
    scaleX(3),
    scaleY(4),
    shearX(5),
    shearY(6),
    rotation(2);
    private final int value;
    private TransformChannels(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}