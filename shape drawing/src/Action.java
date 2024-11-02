public class Action {

    private final char actionType;
    private final int shapeIndex;
    private int originalCenterIndex, newCenterIndex, listIndex, rotation;

    public Action(int shapeIndex, int originalCenterIndex, int newCenterIndex, int listIndex) {

        this.shapeIndex = shapeIndex;
        this.actionType = 'T';

        this.originalCenterIndex = originalCenterIndex;
        this.newCenterIndex = newCenterIndex;
        this.listIndex = listIndex;

    }

    public Action(int shapeIndex, int rotation) {

        this.shapeIndex = shapeIndex;
        this.actionType = 'R';

        this.rotation = rotation;

    }

    public int getShapeIndex() {
        return shapeIndex;
    }

    public char getActionType() {
        return actionType;
    }

    public int getOriginalCenterIndex() {
        return originalCenterIndex;
    }

    public int getNewCenterIndex() {
        return newCenterIndex;
    }

    public int getListIndex() {
        return listIndex;
    }

    public int getRotation() {
        return rotation;
    }

}
