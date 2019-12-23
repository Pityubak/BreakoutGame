/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pityubak.breakoutgame;

import com.pityubak.gamelibrary.annotations.Action;
import com.pityubak.gamelibrary.annotations.Binding;
import com.pityubak.gamelibrary.annotations.Button;
import com.pityubak.gamelibrary.annotations.Entity;
import com.pityubak.gamelibrary.annotations.Get;
import com.pityubak.gamelibrary.annotations.Label;
import com.pityubak.gamelibrary.annotations.Message;
import com.pityubak.gamelibrary.annotations.Observeable;
import com.pityubak.gamelibrary.annotations.Panel;
import com.pityubak.gamelibrary.annotations.Window;
import com.pityubak.gamelibrary.components.RunnableComponent;
import com.pityubak.gamelibrary.components.SwingEntity;
import com.pityubak.gamelibrary.components.SwingFrame;
import com.pityubak.gamelibrary.components.SwingPanel;
import com.pityubak.gamelibrary.components.SwingText;
import com.pityubak.gamelibrary.misc.ColorType;
import com.pityubak.gamelibrary.misc.DrawingType;
import com.pityubak.gamelibrary.misc.PredefinedAction;
import com.pityubak.liberator.data.RuntimeObject;
import com.pityubak.gamelibrary.service.ColorConvertService;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 *
 * @author Pityubak
 */
@Observeable("Board")
public final class Board implements RunnableComponent {

    @Window(undecorated = true,
            defaultCloseOperation = javax.swing.WindowConstants.EXIT_ON_CLOSE,
            width = 800, height = 640)
    private SwingFrame frame;

    @Panel(draggable = true, bgColor = ColorType.FLORAL_WHITE)
    @Binding(actionType = PredefinedAction.LEVEL, parent = "frame", targetName = "menuPanel",
            actionName = "pause", targetVar = "isPause")
    @Action(key = KeyEvent.VK_LEFT, actionName = "PaddleLeft", action = "moveLeft")
    @Action(key = KeyEvent.VK_RIGHT, actionName = "PaddleRight", action = "moveRight")
    @Action(key = KeyEvent.VK_ESCAPE, actionName = "showPanel", action = "pause")
    private SwingPanel panel;

    @Entity(draw = DrawingType.DRAW_IMAGE, x = 300,
            y = 500, height = 40, width = 40, image = "/ball.png",
            xDir = 1, yDir = 1)
    private SwingEntity ball;

    @Entity(x = 300, y = 570, color = ColorType.DARK_SLATE_GRAY, width = 200, height = 30,
            xDir = 1, yDir = 1, draw = DrawingType.DRAW_IMAGE, image = "/paddle.png")

    @Binding(actionType = PredefinedAction.LEFT_MOVE, speed = 20, actionName = "moveLeft")
    @Binding(actionType = PredefinedAction.RIGHT_MOVE, speed = 20, actionName = "moveRight")
    private SwingEntity paddle;

    @Button(width = 150, height = 50, text = "Exit", x = 225, y = 540, bgColor = ColorType.CORN_FLOWER_BLUE)
    @Binding(actionType = PredefinedAction.EXIT, targetName = "frame")
    private JButton exitBtn;

    @Button(width = 150, height = 50, text = "Restart Game", x = 425, y = 540, bgColor = ColorType.CORN_FLOWER_BLUE)
    @Binding(actionType = PredefinedAction.RESET, resetComponents = {"Board", "Brick", "Menu"})
    private JButton resetBtn;

    @Message(message = "GAME OVER", color = ColorType.RED, width = 325, height = 300)
    private SwingText text;

    @Label(text = "Score: ", width = 200, height = 50, x = 0, y = 590, fontSize = 25)
    private JLabel scoreLabel;

    private int score;

    @Observeable("mainPanel")
    private final RuntimeObject mainPanel = new RuntimeObject(SwingPanel.class);

    @Get("list")
    private RuntimeObject list;

    @Observeable("isPause")
    private final RuntimeObject isPause = new RuntimeObject(true);

    Random random = new Random();

    private boolean collision() {
        return this.ball.getBounds().intersects(this.paddle.getBounds());
    }

    private void collisionWithBricks() {
        List<SwingEntity> bricks = (ArrayList) list.get();

        bricks.stream().filter(brick -> (brick.getBounds().intersects(ball.getBounds()))).forEachOrdered(brick -> {
            int ballLeft = (int) ball.getBounds().getMinX();
            int ballHeight = (int) ball.getBounds().getHeight();
            int ballWidth = (int) ball.getBounds().getWidth();
            int ballTop = (int) ball.getBounds().getMinY();
            var pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
            var pointLeft = new Point(ballLeft - 1, ballTop);
            var pointTop = new Point(ballLeft, ballTop - 1);
            var pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);
            if (!brick.isDestroyed()) {
                modifyBallDirection(brick, pointRight, pointLeft, pointTop, pointBottom);
            }
        });

    }

    private void modifyBallDirection(SwingEntity brick, Point pointRight,
            Point pointLeft, Point pointTop, Point pointBottom) {
        if (brick.getBounds().contains(pointRight)) {

            ball.setxDir(-1);
        } else if (brick.getBounds().contains(pointLeft)) {

            ball.setxDir(1);
        }

        if (brick.getBounds().contains(pointTop)) {

            ball.setyDir(1);
        } else if (brick.getBounds().contains(pointBottom)) {

            ball.setyDir(-1);
        }
        if (random.nextInt(2) == 1) {
            brick.destroy();
            score += brick.getImageName().contains("_broken") ? 5 : 10;

        } else {
            String img = brick.getImageName().replace(".png", "");
            String target = "/bricks/" + img + "_broken.png";
            brick.setImage(new ImageIcon(this.getClass().getResource(target)).getImage());
            score -= brick.getImageName().contains("_broken") ? 10 : 5;
        }
        scoreLabel.setText("Score: " + score);
    }

    private void ballMove() {

        if (ball.getX() + ball.getxDir() < 0) {
            ball.setxDir(1);

        } else if (ball.getX() + ball.getxDir() > panel.getWidth() - 20) {
            ball.setxDir(-1);

        }
        if (ball.getY() + ball.getyDir() < 0) {
            ball.setyDir(1);
        } else if (ball.getBounds().getMaxY() > panel.getHeight()
                && panel.getHeight() > 0) {

            text.setIsDraw(true);
            this.isPause.set(true);
            panel.add(exitBtn);
            panel.add(resetBtn);

        }
        ball.setX(ball.getX() + ball.getxDir());
        ball.setY(ball.getY() + ball.getyDir());

        if (collision()) {
            ball.setyDir(-1);
            ball.setY(paddle.getY() - ball.getHeight());
        }
        collisionWithBricks();

    }

    @Override
    public void init() {

        score = 0;
        scoreLabel.setText("Score: " + score);
        Font font = new Font("Comic Sans MS", Font.BOLD, 30);

        text.setFont(font);
        mainPanel.set(panel);
        panel.setComponent(text);
        panel.addEntity(ball);
        panel.addEntity(paddle);
        panel.add(scoreLabel);
        Border border = BorderFactory.createLineBorder(ColorConvertService.convert(ColorType.GRAY), 4);
        panel.setBorder(border);

        frame.addSwingPanel("menuPanel");
    }

    @Override
    public void afterInit() {
        mainPanel.set(null);
        text.setIsDraw(false);
        panel.removeTextComponent();
        panel.removeEntity(ball);
        panel.removeEntity(paddle);
        panel.remove(scoreLabel);
        panel.remove(exitBtn);
        panel.remove(resetBtn);
        frame.remove(panel);
        ball.setX(300);
        ball.setY(500);
        paddle.setX(300);
        paddle.setY(570);
        panel.removeAllEntity();

    }

    @Override
    public SwingFrame getFrame() {
        return frame;
    }

    @Override
    public void run() {

        if (!(boolean) this.isPause.get()) {
            ballMove();
            panel.repaint();
        }
    }

}
