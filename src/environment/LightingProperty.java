package environment;

public class LightingProperty {
    public int defaultRadius = 125;
    public int radius;
    public int counter;

    public LightingProperty() {
        this.radius = defaultRadius;
        this.counter = 0;
    }

    public void update() {
        if (counter > 0) {
            counter--;
        }

        if (radius != defaultRadius && counter == 0) {
            radius = defaultRadius;
        }
    }

    public void reset() {
        radius = defaultRadius;
        counter = 0;
    }
}
