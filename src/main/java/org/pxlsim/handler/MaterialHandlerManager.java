package org.pxlsim.handler;

import org.pxlsim.model.materials.Material;
import org.pxlsim.model.materials.dynamic.DynamicMaterial;
import org.pxlsim.model.materials.dynamic.GasMaterial;
import org.pxlsim.model.materials.dynamic.SandMaterial;
import org.pxlsim.model.materials.dynamic.WaterMaterial;

public class MaterialHandlerManager {

    private final SandHandler sandHandler;
    private final WaterHandler waterHandler;
    private final GasHandler gasHandler;

    public MaterialHandlerManager() {
        this.sandHandler = new SandHandler();
        this.waterHandler = new WaterHandler();
        this.gasHandler = new GasHandler();
    }

    public void handle(Material[][] board, DynamicMaterial material) {
        if (material instanceof SandMaterial) {
            sandHandler.handle(board, (SandMaterial) material);
        } else if (material instanceof WaterMaterial) {
            waterHandler.handle(board, (WaterMaterial) material);
        } else if (material instanceof GasMaterial) {
            gasHandler.handle(board, (GasMaterial) material);
        }
    }

}
