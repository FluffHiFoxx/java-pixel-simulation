package org.pxlsim.model.materials.dynamic;

import javafx.scene.paint.Color;
import org.pxlsim.Display;
import org.pxlsim.model.MaterialType;

import java.math.BigDecimal;

public class GasMaterial extends DynamicMaterial {

    private static final Color DEFAULT_COLOR = Color.hsb(Color.DARKKHAKI.getHue(),
            Color.DARKKHAKI.getSaturation(),
            BigDecimal.valueOf(Color.DARKKHAKI.getBrightness())
                    .subtract(BigDecimal.valueOf(0.5))
                    .doubleValue(),
            Color.DARKKHAKI.getOpacity());

    public GasMaterial(Display display, int positionX, int positionY) {
        super(display, positionX, positionY, MaterialType.GAS, DEFAULT_COLOR);
    }

}
