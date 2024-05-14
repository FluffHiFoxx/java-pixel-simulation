package org.pxlsim.handler;

import javafx.scene.paint.Color;
import org.pxlsim.model.Position;
import org.pxlsim.model.materials.Material;
import org.pxlsim.model.materials.dynamic.GasMaterial;
import org.pxlsim.model.materials.dynamic.WaterMaterial;

public class WaterHandler implements DynamicMaterialHandler<WaterMaterial> {

    @Override
    public void handle(Material[][] board, WaterMaterial material) {
        move(board, material);
        changeColor(board, material);
    }

    @Override
    public void move(Material[][] board, WaterMaterial material) {
        int nextY = material.getPosition().getY() + Math.min(material.getLimit().getY() - material.getPosition().getY(), material.getFallSpeed());
        int nextX = material.getPosition().getX();
        Position nextPosition = new Position(nextX + material.getFlowDirection(), material.getPosition().getY());

        if (isInBounds(material, nextPosition)) {
            nextX += material.getFlowDirection();
        } else {
            material.reverseFlow();
        }
        board[material.getPosition().getY()][material.getPosition().getX()] = null;

        for (int i = material.getPosition().getY(); i <= nextY; i++) {
            if (board[i][material.getPosition().getX()] != null) {
                if (board[i][nextX] == null
                        && board[material.getPosition().getY()][nextX] == null) {
                    if (board[i][nextX] instanceof GasMaterial lesser) {
                        lesser.getPosition().setY(i + 1);
                        board[lesser.getPosition().getY()][lesser.getPosition().getX()] = lesser;
                    }
                    material.getPosition().setY(i);
                    material.getPosition().setX(nextX);
                } else if (board[material.getPosition().getY()][nextX] == null) {
                    if (board[material.getPosition().getY()][nextX] instanceof GasMaterial lesser) {
                        lesser.getPosition().setY(material.getPosition().getY() + 1);
                        board[lesser.getPosition().getY()][lesser.getPosition().getX()] = lesser;
                    }
                    material.getPosition().setX(nextX);
                } else {
                    if (board[i][material.getPosition().getX()] instanceof GasMaterial lesser) {
                        lesser.getPosition().setY(i + 1);
                        material.getPosition().setY(i);
                        board[lesser.getPosition().getY()][lesser.getPosition().getX()] = lesser;
                    } else {
                        material.getPosition().setY(i - 1);
                        material.reverseFlow();
                    }
                }
                board[material.getPosition().getY()][material.getPosition().getX()] = material;
                return;
            }
        }
        material.getPosition().setY(nextY);
        if (board[material.getPosition().getY()][nextX] == null && material.getPosition().getY() == material.getLimit().getY()) {
            material.getPosition().setX(nextX);
        } else if (material.getPosition().getY() == material.getLimit().getY()) {
            material.reverseFlow();
        }
        board[material.getPosition().getY()][material.getPosition().getX()] = material;
    }

    @Override
    public void changeColor(Material[][] board, WaterMaterial material) {
        Material waterLeft = isInBounds(material, new Position(material.getPosition().getX() - 1, material.getPosition().getY()))
                ? board[material.getPosition().getY()][material.getPosition().getX() - 1]
                : null;

        Material waterRight = isInBounds(material, new Position(material.getPosition().getX() + 1, material.getPosition().getY()))
                ? board[material.getPosition().getY()][material.getPosition().getX() + 1]
                : null;

        if (board[Math.max(material.getPosition().getY() - 1, 0)][material.getPosition().getX()] == null && !material.getColor().equals(Color.DEEPSKYBLUE)) {
            material.setColor(Color.DEEPSKYBLUE);
        } else if (board[Math.max(material.getPosition().getY() - 1, 0)][material.getPosition().getX()] instanceof WaterMaterial waterMaterial) {
            Color colorAbove = waterMaterial.getColor();
            double r = colorAbove.getRed() - 0.1 >= 0 ? colorAbove.getRed() - 0.1 : 0;
            double g = colorAbove.getGreen() - 0.1 >= 0 ? colorAbove.getGreen() - 0.1 : 0;
            double b = colorAbove.getGreen() == 0 && colorAbove.getRed() == 0 && colorAbove.getBlue() > 0.4 ? colorAbove.getBlue() - 0.1 : colorAbove.getBlue();
            material.setColor(Color.color(r, g, b));
        } else if (waterLeft != null || waterRight != null) {
            double left = waterLeft != null ? waterLeft.getColor().getGreen() : 1;
            double right = waterRight != null ? waterRight.getColor().getGreen() : 1;
            if (Math.min(left, right) == left && waterLeft instanceof WaterMaterial) {
                material.setColor(waterLeft.getColor());
            } else if (waterRight instanceof WaterMaterial) {
                material.setColor(waterRight.getColor());
            }
        }
    }

    @Override
    public boolean isInBounds(Material material, Position position) {
        return position.getX() >= 0 && position.getX() <= material.getLimit().getX();
    }

}
