/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pityubak.breakoutgame;

import com.pityubak.liberator.data.RuntimeObject;

import com.pityubak.swinglibrary.annotations.EntityList;
import com.pityubak.swinglibrary.annotations.Get;
import com.pityubak.swinglibrary.annotations.Observeable;
import com.pityubak.swinglibrary.components.SwingEntity;
import com.pityubak.swinglibrary.components.SwingPanel;
import com.pityubak.swinglibrary.components.SwingComponent;
import com.pityubak.swinglibrary.misc.DrawingType;
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

    //
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
