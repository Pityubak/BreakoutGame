/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pityubak.breakoutgame;

import com.pityubak.gamelibrary.GameBuilder;



/**
 *
 * @author Pityubak
 */
public class Game {

    public static void main(String[] args) {
        
        GameBuilder builder=new GameBuilder(new Class<?>[]{Board.class,Menu.class,Brick.class});
        
        builder.run(5);
    }
}
