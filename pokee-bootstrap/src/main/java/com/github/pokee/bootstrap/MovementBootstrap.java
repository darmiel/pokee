package com.github.pokee.bootstrap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MovementBootstrap extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovementBootstrap::new);
    }

    public static final int R = 15;

    public MovementBootstrap() throws HeadlessException {
        super("MyFrame");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        this.setVisible(true);

        final MyPanel panel = new MyPanel();
        panel.setBackground(Color.BLACK);
        this.add(panel);
        this.repaint();

        final AtomicBoolean isWPressed = new AtomicBoolean(false);
        final AtomicBoolean isSPressed = new AtomicBoolean(false);
        final AtomicBoolean isAPressed = new AtomicBoolean(false);
        final AtomicBoolean isDPressed = new AtomicBoolean(false);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'w') {
                    isWPressed.set(true);
                }
                if (e.getKeyChar() == 's') {
                    isSPressed.set(true);
                }
                if (e.getKeyChar() == 'a') {
                    isAPressed.set(true);
                }
                if (e.getKeyChar() == 'd') {
                    isDPressed.set(true);
                }
                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == 'w') {
                    isWPressed.set(false);
                }
                if (e.getKeyChar() == 's') {
                    isSPressed.set(false);
                }
                if (e.getKeyChar() == 'a') {
                    isAPressed.set(false);
                }
                if (e.getKeyChar() == 'd') {
                    isDPressed.set(false);
                }
                super.keyReleased(e);
            }


            @Override
            public void keyTyped(final KeyEvent e) {
                if (isWPressed.get()) {
                    if (isAPressed.get()) {
                        // strafe left-up
                        panel.pleaseMove(panel.x - 7, panel.y - 7);
                    } else if (isDPressed.get()) {
                        // strafe right-up
                        panel.pleaseMove(panel.x + 7, panel.y - 7);
                    } else if (!isSPressed.get()) {
                        panel.pleaseMove(panel.x, panel.y - 10);
                    }
                } else if (isSPressed.get()) {
                    if (isAPressed.get()) {
                        // strafe left-down
                        panel.pleaseMove(panel.x - 7, panel.y + 7);
                    } else if (isDPressed.get()) {
                        // strafe right-down
                        panel.pleaseMove(panel.x + 7, panel.y + 7);
                    } else {
                        panel.pleaseMove(panel.x, panel.y + 10);
                    }
                } else if (isAPressed.get()) {
                    panel.pleaseMove(panel.x - 10, panel.y);
                } else if (isDPressed.get()) {
                    panel.pleaseMove(panel.x + 10, panel.y);
                }
                panel.repaint();
            }
        });


        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                panel.pleaseMove(e.getX(), e.getY() - R);
            }
        });

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
//        executor.scheduleAtFixedRate(new Bouncer(panel), 0, 1000 / 30, TimeUnit.MILLISECONDS);

        executor.scheduleAtFixedRate(() -> {
            if (isWPressed.get()) {
                if (isAPressed.get()) {
                    // strafe left-up
                    panel.pleaseMove(panel.x - 7, panel.y - 7);
                } else if (isDPressed.get()) {
                    // strafe right-up
                    panel.pleaseMove(panel.x + 7, panel.y - 7);
                } else if (!isSPressed.get()) {
                    panel.pleaseMove(panel.x, panel.y - 10);
                }
            } else if (isSPressed.get()) {
                if (isAPressed.get()) {
                    // strafe left-down
                    panel.pleaseMove(panel.x - 7, panel.y + 7);
                } else if (isDPressed.get()) {
                    // strafe right-down
                    panel.pleaseMove(panel.x + 7, panel.y + 7);
                } else {
                    panel.pleaseMove(panel.x, panel.y + 10);
                }
            } else if (isAPressed.get()) {
                panel.pleaseMove(panel.x - 10, panel.y);
            } else if (isDPressed.get()) {
                panel.pleaseMove(panel.x + 10, panel.y);
            }
            panel.repaint();
        }, 0, 1000 / 30, TimeUnit.MILLISECONDS);

    }

    @SuppressWarnings("unused")
    static class Bouncer implements Runnable {
        private final MyPanel panel;

        int dx = 15;
        int dy = 15;

        public Bouncer(final MyPanel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            final int newX = this.panel.x + this.dx;
            final int newY = this.panel.y + this.dy;

            if (newX < 0 || newX > this.panel.getWidth()) {
                this.dx *= -1;
            }
            if (newY < 0 || newY > this.panel.getHeight()) {
                this.dy *= -1;
            }

            this.panel.pleaseMove(newX, newY);
        }
    }

    static class MyPanel extends JPanel {

        public int x, y;

        record FromTo(int fromX, int fromY, int toX, int toY) {
        }

        private final List<FromTo> trail = new ArrayList<>();

        public void pleaseMove(final int newX, final int newY) {
            int lastX = this.x;
            int lastY = this.y;

            this.x = newX;
            this.y = newY;

            // interpolate 10 points between last and new position and add to trail list
            final double count = Math.sqrt(Math.pow(this.x - lastX, 2) + Math.pow(this.y - lastY, 2));
            for (int i = 0; i < count; i++) {
                final int x = (int) (lastX + (this.x - lastX) * i / count);
                final int y = (int) (lastY + (this.y - lastY) * i / count);
                this.trail.add(new FromTo(lastX, lastY, x, y));
            }

//            this.trail.add(new FromTo(this.lastX, this.lastY, this.x, this.y));
            this.repaint();
        }

        @Override
        public void paint(final Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            // draw trail
            for (final FromTo fromTo : this.trail) {
                final int distance = (int) Math.sqrt(Math.pow(this.x - fromTo.fromX, 2) + Math.pow(this.y - fromTo.fromY, 2));
                Color color;
                if (distance < 50) {
                    color = Color.GREEN;
                } else if (distance < 100) {
                    color = Color.YELLOW;
                } else {
                    color = Color.RED;
                }
                g.setColor(color);
                g.drawLine(fromTo.fromX, fromTo.fromY, fromTo.toX, fromTo.toY);

                int dr = (int) (R * (1 - distance / 100.0));
                if (dr > 0) {
                    g.fillOval(fromTo.toX - dr, fromTo.toY - dr, dr * 2, dr * 2);
                }
            }

            g.setColor(Color.GREEN);
            g.fillOval(this.x - R, this.y - R, R * 2, R * 2);
        }

        @Override
        public String toString() {
            return "MyPanel{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }


    }

}
