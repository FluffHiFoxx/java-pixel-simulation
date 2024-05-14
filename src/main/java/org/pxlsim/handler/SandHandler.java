package org.pxlsim.handler;

import org.pxlsim.model.Position;
import org.pxlsim.model.materials.Material;
import org.pxlsim.model.materials.dynamic.DynamicMaterial;
import org.pxlsim.model.materials.dynamic.GasMaterial;
import org.pxlsim.model.materials.dynamic.SandMaterial;
import org.pxlsim.model.materials.dynamic.WaterMaterial;

import java.util.Random;

public class SandHandler implements DynamicMaterialHandler<SandMaterial> {

    @Override
    public void handle(Material[][] board, SandMaterial material) {
        move(board, material);
        changeColor(board, material);
    }

    @Override
    public void move(Material[][] board, SandMaterial material) {
        int nextY = material.getPosition().getY() + Math.min(material.getLimit().getY() - material.getPosition().getY(), material.getFallSpeed());
        int xModifier = new Random().nextBoolean() ? 1 : -1;
        int nextX = material.getPosition().getX();
        Position nextPosition = new Position(nextX + xModifier, material.getPosition().getY());

        if (isInBounds(material, nextPosition)) {
            nextX += xModifier;
        }
        board[material.getPosition().getY()][material.getPosition().getX()] = null;

        for (int i = material.getPosition().getY(); i <= nextY; i++) {
            if (board[i][material.getPosition().getX()] != null) {
                if (board[i][nextX] == null
                        && board[material.getPosition().getY()][nextX] == null) {
                    if (board[i][nextX] instanceof WaterMaterial || board[i][nextX] instanceof GasMaterial) {
                        DynamicMaterial lesser = (DynamicMaterial) board[i][nextX];
                        lesser.getPosition().setY(i + 1);
                        board[lesser.getPosition().getY()][lesser.getPosition().getX()] = lesser;
                    }
                    material.getPosition().setY(i);
                    material.getPosition().setX(nextX);
                } else {
                    if (board[i][material.getPosition().getX()] instanceof WaterMaterial || board[i][material.getPosition().getX()] instanceof GasMaterial) {
                        DynamicMaterial lesser = (DynamicMaterial) board[i][material.getPosition().getX()];
                        lesser.getPosition().setY(i - 1);
                        material.getPosition().setY(i);
                        board[lesser.getPosition().getY()][lesser.getPosition().getX()] = lesser;
                    } else {
                        material.getPosition().setY(i - 1);
                    }
                }
                board[material.getPosition().getY()][material.getPosition().getX()] = material;
                return;
            }
        }
        material.getPosition().setY(nextY);
        board[material.getPosition().getY()][material.getPosition().getX()] = material;
    }

    @Override
    public void changeColor(Material[][] board, SandMaterial material) {

    }

    @Override
    public boolean isInBounds(Material material, Position position) {
        return position.getX() >= 0 && position.getX() <= material.getLimit().getX();
    }
}
