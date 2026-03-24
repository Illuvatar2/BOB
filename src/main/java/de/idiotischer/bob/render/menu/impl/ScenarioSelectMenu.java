package de.idiotischer.bob.render.menu.impl;

import de.idiotischer.bob.BOB;
import de.idiotischer.bob.render.menu.Component;
import de.idiotischer.bob.render.menu.Menu;
import de.idiotischer.bob.render.menu.components.button.ButtonComp;
import de.idiotischer.bob.render.menu.components.ScrollContainer;
import de.idiotischer.bob.scenario.Scenario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScenarioSelectMenu implements Menu {

    private final JPanel parent;
    private final int layoutScaleX;
    private final int layoutScaleY;
    private Set<Scenario> scenarios;
    private final ScrollContainer scroller;
    private Scenario selectedScenario = null;

    private final ButtonComp nextMenuButton = new ButtonComp("Next", Color.WHITE, Color.DARK_GRAY.darker(),true,320,-208,95,28, 16,16, 15, Color.DARK_GRAY.brighter(), Color.BLACK, true, (b) -> {
        System.out.println("clicked next");
    });

    private final ButtonComp backMenuButton = new ButtonComp("Back to Menu", Color.WHITE, Color.DARK_GRAY.darker(),true,0,-210,138,28, 16,16, 15, Color.DARK_GRAY.brighter(), Color.BLACK, true, (b) -> {
        BOB.getInstance().getMapRenderer().getMenuPanel().setInScenarioSelect(false);
    });

    private final ButtonComp buttonSoThatItLooksBetter = new ButtonComp("Placeholder", Color.WHITE, Color.DARK_GRAY.darker(),true,-320,-208,120,28, 16,16, 15, Color.DARK_GRAY.brighter(), Color.BLACK, true, (b) -> {
        System.out.println("clicked placeholder");
    });

    List<Component> components = new ArrayList<>();

    public ScenarioSelectMenu(JPanel panel, int layoutScaleX, int layoutScaleY) {
        this.parent = panel;

        scroller = new ScrollContainer(panel, new Color(200, 200, 200, 180), true);

        this.layoutScaleX = layoutScaleX;
        this.layoutScaleY = layoutScaleY;

        selectedScenario = BOB.getInstance().getScenarioSceneLoader().getCurrentScenario();

        reload();

        int x = parent.getWidth() / 2 - (layoutScaleX / 2) + 20;
        int y = parent.getHeight() / 2 - (layoutScaleY / 2) + 20;
        int width = layoutScaleX - 40;
        int height = layoutScaleY - 40;

        scroller.setBounds(new Rectangle(x, y, width, height));

        components.add(scroller);
        //button muss drüber deswegen nach scroller
        components.add(nextMenuButton);
        components.add(backMenuButton);
        components.add(buttonSoThatItLooksBetter);
    }

    public int getLayoutScaleX() {
        return layoutScaleX;
    }

    public int getLayoutScaleY() {
        return layoutScaleY;
    }

    public JPanel getParent() {
        return parent;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.DARK_GRAY);

        Graphics2D g2 = (Graphics2D) g;
        //center logic so für alles übernehmen
        int x = parent.getWidth() / 2 - (layoutScaleX / 2);
        int y = parent.getHeight() / 2 - (layoutScaleY / 2);

        g2.setStroke(new BasicStroke(16));

        drawBack(g2, x, y);

        components.forEach(component -> component.paint(g2));

        drawImageFrame(g2);
    }

    private void drawBack(Graphics2D g2, int x, int y) {
        g2.setColor(Color.DARK_GRAY.darker());
        g2.drawRoundRect(x, y, layoutScaleX, layoutScaleY + 40,32,32);

        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(x, y, layoutScaleX, layoutScaleY + 40,32,32);
    }

    public void drawImageFrame(Graphics2D g2) {
        int heightShrink = 40;

        Rectangle bounds = scroller.getActualBounds();

        int x = scroller.isCentered() ? (scroller.getPanel().getWidth() - bounds.width) / 2 : bounds.x;
        int y = scroller.isCentered() ? (scroller.getPanel().getHeight() - bounds.height) / 2 : bounds.y;

        int imgFrameWidth = 350;
        int frameX = x + bounds.width - imgFrameWidth - 2;
        int bufferX = heightShrink / 2;

        g2.setStroke(new BasicStroke(3)); //thickness of the img frame
        g2.drawRoundRect(
                frameX - bufferX,
                y + (heightShrink / 2),
                imgFrameWidth,
                bounds.height - heightShrink,
                24,
                24
        );

        if(selectedScenario == null) return;
        if(selectedScenario.getMapImage() == null) return;

        int width = selectedScenario.getMapImage().getWidth() / 4;
        int height = selectedScenario.getMapImage().getHeight() / 4;


        g2.setStroke(new BasicStroke(5)); //thickness of the img frame
        g2.drawRoundRect(x + 405, y + 40, width, height, 24, 24);

        g2.drawImage(makeRoundedCorner(selectedScenario.getMapImage(), 100), x + 405, y + 40, width,height,null);
    }

    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //this doesnt cause the low quality, prob just the small scale
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }


    @Override
    public void keyPress(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            BOB.getInstance().getMapRenderer().getMenuPanel().setInScenarioSelect(false);
        }
    }

    @Override
    public void mouseClick(MouseEvent e, int x, int y) {
        components.forEach(component -> component.mouseClick(e, x, y));
    }

    @Override
    public void mouseRelease(MouseEvent e, int x, int y) {
        components.forEach(component -> component.mouseRelease(e, x, y));
    }

    @Override
    public void mouseMove(MouseEvent e, int x, int y) {
        components.forEach(component -> component.mouseMove(e, x, y));
    }

    @Override
    public void mouseScroll(MouseWheelEvent e, int x, int y) {
        components.forEach(component -> component.mouseScroll(e, x, y));
    }

    public void reload() {
        this.scenarios = BOB.getInstance().getScenarioManager().getScenarios();

        List<ButtonComp> buttons = new ArrayList<>();
        for (Scenario s : scenarios) {
            //TODO: make it so i dont need to trial and error with these values (bs but gonna leave this todo in anyways)
            ButtonComp b = new ButtonComp(
                    s.getName(),
                    Color.WHITE,
                    Color.BLACK,
                    false,
                    -200, 25,
                    300, 40,
                    16, 16,
                    2, Color.LIGHT_GRAY,
                    Color.DARK_GRAY,
                    true, (b1) -> {System.out.println("clicked settings");});
            b.setPanel(parent);
            buttons.add(b);
        }

        scroller.setChildren(new ArrayList<>(buttons));
    }

    public Set<Scenario> getScenarios() {
        return scenarios;
    }
}
