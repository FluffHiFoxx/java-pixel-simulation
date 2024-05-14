package org.pxlsim.model.materials;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.pxlsim.Display;
import org.pxlsim.model.MaterialType;
import org.pxlsim.model.Position;

import java.util.UUID;

@Getter
@Setter
public class Material {
    private final UUID id;
    private final Position limit;
    private final MaterialType type;
    private final Position position;
    private Color Color;

    public Material(Display display, int positionX, int positionY, MaterialType type, Color color) {
        this.id = UUID.randomUUID();
        this.limit = new Position(display.getWidth() - 1, display.getHeight() - 1);
        this.type = type;
        this.position = new Position(Math.min(positionX, this.limit.getX()), Math.min(positionY, this.limit.getY()));
        this.Color = color;
    }

}
