package Animation;

public enum TransformChannels{
    x(0),
    y(1),
    scaleX(2),
    scaleY(3),
    rotation(4),
    shearX(5),
    shearY(6),
    opacity(7);
    private final int value;
    private TransformChannels(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}