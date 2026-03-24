package de.idiotischer.bob.render.menu.components;

import de.idiotischer.bob.BOB;
import de.idiotischer.bob.render.menu.Component;
import de.idiotischer.bob.render.menu.components.button.ButtonComp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class ScrollContainer implements Component {
    private final JPanel panel;
    private final Color color;

    private final boolean centered;

    private Rectangle bounds;
    private List<ButtonComp> children = new ArrayList<>();

    private int scrollOffset = 0;
    private int contentHeight = 0;

    private int padding = 10;
    private int spacing = 10;

    private int scrollbarWidth = 8;
    private Rectangle scrollBounds;

    public ScrollContainer(JPanel panel, Color color, boolean centered) {
        bounds = new Rectangle(0, 0, 0, 0);
        this.panel = panel;
        this.color = color;
        this.centered = centered;
    }

    public void setChildren(List<ButtonComp> children) {
        this.children = children;
        layoutChildren();
    }

    private void layoutChildren() {
        int yOffset = padding;

        for (Component child : children) {
            if (child instanceof ButtonComp btn) {
                Rectangle b = btn.getBounds();
                b.y += yOffset;
                yOffset += b.height + spacing;
            }
        }

        contentHeight = yOffset;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        JPanel pl = panel != null ? panel : BOB.getInstance().getMapRenderer().getGamePanel();

        Rectangle bounds = getActualBounds();

        int x = centered ? (pl.getWidth() - bounds.width) / 2 : bounds.x;
        int y = centered ? (pl.getHeight() - bounds.height) / 2 : bounds.y;

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, bounds.width, bounds.height, 24, 24);

        g2.setClip(x, y, bounds.width, bounds.height);

        //this will be the image container btu i am too stupdi so i just copied the scrollabr xD
        int heightShrink = 40;

        int imgFrameWidth = 350;
        int frameX = x + bounds.width - imgFrameWidth - 2;
        int bufferX = heightShrink / 2;

        g2.drawRoundRect(
                frameX - bufferX,
                y + (heightShrink / 2),
                imgFrameWidth,
                bounds.height - heightShrink,
                24,
                24
        );

        //scrollbar ykyk
        int barX = x + (bounds.width / 2) - scrollbarWidth - 2;
        
        this.scrollBounds = new Rectangle(barX - 15, y + (heightShrink / 2), scrollbarWidth, bounds.height - heightShrink);
       
        g2.drawRoundRect(
                scrollBounds.x,
                scrollBounds.y,
                scrollbarWidth,
                bounds.height - heightShrink,
                12,
                12
        );
        
        for (ButtonComp child : children) {
            child.paint(g2);
        }

        g2.dispose();
    }

    @Override
    public void mouseScroll(MouseWheelEvent e, int x, int y) {
    }

    public void setBounds(Rectangle rectangle) {
        this.bounds = rectangle;
    }

    public Rectangle getActualBounds() {
        JPanel pl = panel != null ? panel : BOB.getInstance().getMapRenderer().getGamePanel();
        int x = centered ? pl.getWidth() / 2 - (bounds.width / 2) : bounds.x;
        int y = centered ? pl.getHeight() / 2 - (bounds.height / 2) : bounds.y;
        x += bounds.x;
        y -= bounds.y;
        return new Rectangle(x, y, bounds.width, bounds.height);
    }

    public Rectangle getActualScrollBounds() {
        JPanel pl = panel != null ? panel : BOB.getInstance().getMapRenderer().getGamePanel();
        int x = centered ? pl.getWidth() / 2 - (scrollBounds.width / 2) : scrollBounds.x;
        int y = centered ? pl.getHeight() / 2 - (scrollBounds.height / 2) : scrollBounds.y;
        x += scrollBounds.x;
        y -= scrollBounds.y;
        return new Rectangle(x, y, scrollBounds.width, scrollBounds.height);
    }

    public Rectangle getScrollBounds() {
        return scrollBounds;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isCentered() {
        return centered;
    }

    public JPanel getPanel() {
        return panel;
    }
}