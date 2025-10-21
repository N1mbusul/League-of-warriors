public class Cell {
    private int coordX, coordY;
    private boolean visited;
    private CellEntityType entityType;

    public Cell(int coordX, int coordY, boolean visited, CellEntityType entityType) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.visited = visited;
        this.entityType = entityType;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public CellEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(CellEntityType entityType) {
        this.entityType = entityType;
    }

}