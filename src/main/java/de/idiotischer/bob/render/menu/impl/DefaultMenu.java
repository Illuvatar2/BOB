package de.idiotischer.bob.render.menu.impl;

import de.idiotischer.bob.BOB;
import de.idiotischer.bob.render.MainRenderer;
import de.idiotischer.bob.render.menu.Menu;

import java.awt.*;

public class DefaultMenu implements Menu {

    private final MainRenderer renderer;

    public DefaultMenu(MainRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        g.drawString("Current country: " + BOB.getInstance().getPlayer().country().countryName(),15,15);
    }
}
