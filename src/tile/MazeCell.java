package tile;

public class MazeCell {
    private boolean N;
    private boolean S;
    private boolean E;
    private boolean W;
    public Coord position;

    public MazeCell() {
        clearCell();
    }

    @Override
    public String toString() {
        return this.position.toString();
    }

    private void clearCell() {
        this.N = false;
        this.S = false;
        this.E = false;
        this.W = false;
    }

    public void setPosition(Coord coord) {
        this.position = coord;
    }

    public void setDirection(String direction, boolean isTrue) {
        switch (direction) {
            case "N":
                this.N = isTrue;
                break;
            case "S":
                this.S = isTrue;
                break;
            case "E":
                this.E = isTrue;
                break;
            case "W":
                this.W = isTrue;
                break;
            default:
                break;
        }
    }

    public void setOpposite(String direction, boolean isTrue) {
        switch (direction) {
            case "N":
                this.S = isTrue;
                break;
            case "S":
                this.N = isTrue;
                break;
            case "E":
                this.W = isTrue;
                break;
            case "W":
                this.E = isTrue;
                break;
            default:
                break;
        }
    }

    public boolean isDirection(String direction) {
        boolean isTrue = false;

        switch (direction) {
            case "N":
                isTrue = this.N;
                break;
            case "S":
                isTrue = this.S;
                break;
            case "E":
                isTrue = this.E;
                break;
            case "W":
                isTrue = this.W;
                break;
            default:
                break;
        }

        return isTrue;
    }

    public void carvePassageTo(MazeCell neighbour) {
        Coord neighbourCoord = neighbour.position;
        if (neighbourCoord.r == this.position.r + 1) {
            setDirection("S", true);
        } else if (neighbourCoord.r == this.position.r - 1) {
            setDirection("N", true);
        } else if (neighbourCoord.c == this.position.c + 1) {
            setDirection("E", true);
        } else if (neighbourCoord.c == this.position.c - 1) {
            setDirection("W", true);
        }
    }
}
