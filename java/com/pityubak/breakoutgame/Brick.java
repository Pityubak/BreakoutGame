/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pityubak.breakoutgame;

import com.pityubak.gamelibrary.annotations.EntityList;
import com.pityubak.gamelibrary.annotations.Get;
import com.pityubak.gamelibrary.annotations.Observeable;
import com.pityubak.gamelibrary.components.SwingComponent;
import com.pityubak.gamelibrary.components.SwingEntity;
import com.pityubak.gamelibrary.components.SwingPanel;
import com.pityubak.gamelibrary.misc.DrawingType;
import com.pityubak.liberator.data.RuntimeObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Pityubak
 */
@Observeable("Brick")
public class Brick implements SwingComponent {

    Random random = new Random();

    @Get("mainPanel")
    private RuntimeObject mainPanel;

    @EntityList(draw = DrawingType.DRAW_IMAGE, width = 100, height = 30,
            path = "/bricks/", filter = "broken")
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<SwingEntity> entityList;

    @Observeable("list")
    private final RuntimeObject list = new RuntimeObject(ArrayList.class);

    @Override
    public void init() {
        SwingPanel panel = (SwingPanel) this.mainPanel.get();
        list.set(fill(panel));

    }

    private List<SwingEntity> fill(SwingPanel panel) {
        List<SwingEntity> brickEntities = new ArrayList<>();
        SwingEntity[] entities = new SwingEntity[entityList.size()];
        entities = entityList.toArray(entities);

        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 8; j++) {
                SwingEntity ent;

                ent = new SwingEntity(entities[random.nextInt(10)]);
                ent.setX(i * 100);
                ent.setY(j * 30);
                brickEntities.add(ent);
                panel.addEntity(ent);

            }
        }
        return brickEntities;
    }

}
