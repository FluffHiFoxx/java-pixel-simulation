package org.pxlsim.handler;

import javafx.scene.paint.Color;
import org.pxlsim.model.Position;
import org.pxlsim.model.materials.Material;
import org.pxlsim.model.materials.dynamic.GasMaterial;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GasHandler implements DynamicMaterialHandler<GasMaterial> {

    @Override
    public void handle(Material[][] board, GasMaterial material) {
        move(board, material);
        changeColor(board, material);
    }

    @Override
    public void move(Material[][] board, GasMaterial material) {
        int xModifier = new Random().nextInt(-1, 2);
        int nextX = material.getPosition().getX();

        if (isInBounds(material, new Position(material.getPosition().getX() + xModifier, material.getPosition().getY()))) {
            nextX += xModifier;
        } else if (isInBounds(material, new Position(material.getPosition().getX() - xModifier, material.getPosition().getY()))) {
            nextX -= xModifier;
        }
        int nextY = material.getPosition().getY() - Math.min(material.getPosition().getY(), new Random().nextBoolean() ? material.getFallSpeed() : 0);
        board[material.getPosition().getY()][material.getPosition().getX()] = null;

        for (int i = material.getPosition().getY(); i >= nextY; i--) {
            if (board[i][material.getPosition().getX()] != null) {
                if (board[i][nextX] == null
                        && board[material.getPosition().getY()][nextX] == null) {
                    material.getPosition().setY(i);
                    material.getPosition().setX(nextX);
                } else if (board[material.getPosition().getY()][nextX] == null) {
                    material.getPosition().setX(nextX);
                } else {
                    material.getPosition().setY(i + 1);
                }
                board[material.getPosition().getY()][material.getPosition().getX()] = material;
                return;
            }
        }

        material.getPosition().setY(nextY);
        if (board[material.getPosition().getY()][nextX] == null) {
            material.getPosition().setX(nextX);
        }

        board[material.getPosition().getY()][material.getPosition().getX()] = material;
    }

    @Override
    public void changeColor(Material[][] board, GasMaterial material) {
        List<BigDecimal> gasCount = new ArrayList<>();
        getBoardMaterial(board, material, new Position(material.getPosition().getX(), material.getPosition().getY() + 1))
                .ifPresent(gasMaterial ->
                        gasCount.add(BigDecimal.valueOf(gasMaterial.getColor().getBrightness())));

        getBoardMaterial(board, material, new Position(material.getPosition().getX(), material.getPosition().getY() - 1))
                .ifPresent(gasMaterial ->
                        gasCount.add(BigDecimal.valueOf(gasMaterial.getColor().getBrightness())));

        getBoardMaterial(board, material, new Position(material.getPosition().getX() + 1, material.getPosition().getY()))
                .ifPresent(gasMaterial ->
                        gasCount.add(BigDecimal.valueOf(gasMaterial.getColor().getBrightness())));

        getBoardMaterial(board, material, new Position(material.getPosition().getX() - 1, material.getPosition().getY()))
                .ifPresent(gasMaterial ->
                        gasCount.add(BigDecimal.valueOf(gasMaterial.getColor().getBrightness())));

        if (gasCount.size() >= 3) {
            BigDecimal brightness = gasCount.stream()
                    .min(BigDecimal::compareTo)
                    .get();
            material.setColor(Color.hsb(
                    Color.DARKKHAKI.getHue(),
                    Color.DARKKHAKI.getSaturation(),
                    Math.min(Color.DARKKHAKI.getBrightness(), brightness.add(BigDecimal.valueOf(0.05))
                            .doubleValue()),
                    Color.DARKKHAKI.getOpacity()));
        } else {
            material.setColor(Color.hsb(
                    Color.DARKKHAKI.getHue(),
                    Color.DARKKHAKI.getSaturation(),
                    BigDecimal.valueOf(Color.DARKKHAKI.getBrightness())
                            .subtract(BigDecimal.valueOf(0.3))
                            .doubleValue(),
                    Color.DARKKHAKI.getOpacity()));
        }
    }

    @Override
    public boolean isInBounds(Material material, Position position) {
        return (position.getX() >= 0 && position.getX() <= material.getLimit().getX())
                && (position.getY() >= 0 && position.getY() <= material.getLimit().getY());
    }

    private Optional<GasMaterial> getBoardMaterial(Material[][] board, GasMaterial material, Position position) {
        if (isInBounds(material, position)) {
            if (board[position.getY()][position.getX()] instanceof GasMaterial) {
                return Optional.of((GasMaterial) board[position.getY()][position.getX()]);
            }
        }
        return Optional.empty();
    }

}
