package main.blaschke.model.shapes;

public class TIShapeCache {

    private final TIShape tiShape;
    private final String operation;

    public TIShapeCache(TIShape tiShape, String operation) {
        this.tiShape = tiShape;
        this.operation = operation;
    }

    public TIShape getTIShape() {
        return tiShape;
    }

    public String getOperation() {
        return operation;
    }

}
