/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pityubak.breakoutgame;

import com.pityubak.gamelibrary.annotations.Binding;
import com.pityubak.gamelibrary.annotations.Button;
import com.pityubak.gamelibrary.annotations.Get;
import com.pityubak.gamelibrary.annotations.Observeable;
import com.pityubak.gamelibrary.annotations.Panel;
import com.pityubak.gamelibrary.components.SwingComponent;
import com.pityubak.gamelibrary.components.SwingPanel;
import com.pityubak.gamelibrary.misc.ColorType;
import com.pityubak.gamelibrary.misc.PredefinedAction;
import com.pityubak.liberator.data.RuntimeObject;
import com.pityubak.gamelibrary.service.ColorConvertService;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 *
 * @author Pityubak
 */
@Observeable("Menu")
public class Menu implements SwingComponent {

    @Panel(bgColor = ColorType.MOCCASIN,
            draggable = true, width = 450, height = 500)
    private SwingPanel menuPanel;

    @Button(width = 150, height = 50, text = "Exit", x = 325, y = 350, bgColor = ColorType.CORN_FLOWER_BLUE)
    @Binding(actionType = PredefinedAction.EXIT, targetName = "frame")
    private JButton exitBtn;

    @Button(width = 150, height = 50, text = "Start game",
            x = 325, y = 150, bgColor = ColorType.CORN_FLOWER_BLUE)
    @Binding(actionType = PredefinedAction.LEVEL, parent = "frame", targetName = "panel")
    private JButton startGame;

    @Get("isPause")
    private RuntimeObject isPause;

    @Override
    public void init() {
        startGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPause.set(false);
            }
        });
        Border border = BorderFactory.createLineBorder(ColorConvertService.convert(ColorType.LAWN_GREEN), 2);
        menuPanel.setBorder(border);
        menuPanel.add(exitBtn);
        menuPanel.add(startGame);
    }

}
