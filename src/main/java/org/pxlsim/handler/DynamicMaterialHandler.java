package org.pxlsim.handler;

import org.pxlsim.model.Position;
import org.pxlsim.model.materials.Material;

public interface DynamicMaterialHandler<DynamicMaterial> {

    void handle(Material[][] board, DynamicMaterial material);

    void move(Material[][] board, DynamicMaterial material);

    void changeColor(Material[][] board, DynamicMaterial material);

    boolean isInBounds(Material material, Position position);
}
