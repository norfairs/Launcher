/*
 * Copyright 2019 creationreborn.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.creationreborn.launcher;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FancyBackgroundPanel extends JPanel {

    private BufferedImage background;
    private BufferedImage barBottom;
    private BufferedImage barLeft;
    private BufferedImage barRight;
    private BufferedImage barTop;

    public FancyBackgroundPanel() {
        try {
            background = ImageIO.read(com.skcraft.launcher.FancyBackgroundPanel.class.getResourceAsStream("launcher_background.png"));
            barBottom = ImageIO.read(com.skcraft.launcher.FancyBackgroundPanel.class.getResourceAsStream("launcher_bar_bottom.png"));
            barLeft = ImageIO.read(com.skcraft.launcher.FancyBackgroundPanel.class.getResourceAsStream("launcher_bar_left.png"));
            barRight = ImageIO.read(com.skcraft.launcher.FancyBackgroundPanel.class.getResourceAsStream("launcher_bar_right.png"));
            barTop = ImageIO.read(com.skcraft.launcher.FancyBackgroundPanel.class.getResourceAsStream("launcher_bar_top.png"));
        } catch (IOException ex) {
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.drawImage(background, 222, 0, getWidth() - 263, getHeight() - 47, this);

        // Bottom
        g.drawImage(barBottom, 0, getHeight() - barBottom.getHeight(), getWidth(), barBottom.getHeight(), this);

        // Left
        g.drawImage(barLeft, 0, 0, barLeft.getWidth(), getHeight(), this);

        // Right
        g.drawImage(barRight, getWidth() - barRight.getWidth(), 0, barRight.getWidth(), getHeight(), this);

        // Top
        g.drawImage(barTop, 222, 0, getWidth() - 222, barTop.getHeight(), this);
    }
}