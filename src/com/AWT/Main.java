package com.AWT;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;


class Surface extends JPanel {

    // переменные
    private ZRectangle zrect;
    private ZEllipse zell;

    public Surface() {// интерфейс
        initUI();
    }

    private void initUI() { // инициализация

        MovingAdapter ma = new MovingAdapter(); // наш класс с методами мышки

        addMouseMotionListener(ma); // добавляем его к основынм событиям мышки
        addMouseListener(ma);
        addMouseWheelListener(new ScaleHandler());

        zrect = new ZRectangle(50, 50, 50, 50); // 2 объекта
        zell = new ZEllipse(150, 70, 80, 80);
    }

    private void doDrawing(Graphics g) { //------------------------------------------------------------ прорисовка

        Graphics2D g2d = (Graphics2D) g; // графика

        Font font = new Font("Serif", Font.BOLD, 40); // шрифт
        g2d.setFont(font); // установка его

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // рендеринг
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setPaint(new Color(0, 0, 200)); // цвет
        g2d.fill(zrect);                            // прорисовка
        g2d.setPaint(new Color(0, 200, 0));
        g2d.fill(zell);
    }

    @Override
    public void paintComponent(Graphics g) { // прорисовка компонента
        super.paintComponent(g);
        doDrawing(g);
    }

    //------------------------------------------------------------------------------------ наши фигуры

    class ZEllipse extends Ellipse2D.Float { // наш круг

        public ZEllipse(float x, float y, float width, float height) { // позиция и размеры
            setFrame(x, y, width, height);
        }

        public boolean isHit(float x, float y) { // отдаёт координаты
            return getBounds2D().contains(x, y);
        }

        public void addX(float x) { // добавляет координаты Х
            this.x += x;
        }

        public void addY(float y) {// добавляет координаты y

            this.y += y;
        }

        public void addWidth(float w) {// добавляет координаты w

            this.width += w;
        }

        public void addHeight(float h) {// добавляет координаты h

            this.height += h;
        }
    }

    class ZRectangle extends Rectangle2D.Float { // наш квадрат

        public ZRectangle(float x, float y, float width, float height) { // позиция и размеры
            setRect(x, y, width, height);
        }

        public boolean isHit(float x, float y) { // отдаёт координаты
            return getBounds2D().contains(x, y);
        }

        public void addX(float x) {// добавляет координаты Х

            this.x += x;
        }

        public void addY(float y) {// добавляет координаты y

            this.y += y;
        }

        public void addWidth(float w) {// добавляет координаты w

            this.width += w;
        }

        public void addHeight(float h) {// добавляет координаты h

            this.height += h;
        }
    }

    //-------------------------------------------------------------------------------------- события мышки

    class MovingAdapter extends MouseAdapter { // обработка действий

        // переменные
        private int x;
        private int y;

        @Override
        public void mousePressed(MouseEvent e) { // нажать
            x = e.getX(); // получем значения координат
            y = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            doMove(e); // передвижение
        }

        private void doMove(MouseEvent e) {
            int dx = e.getX() - x; // новые переменные
            int dy = e.getY() - y;

            if (zrect.isHit(x, y)) { // если на квадрате
                zrect.addX(dx);
                zrect.addY(dy); // новые значени
                repaint(); // перерисовать
            }

            if (zell.isHit(x, y)) {
                zell.addX(dx);// новые значени
                zell.addY(dy);
                repaint();// перерисовать
            }

            x += dx;
            y += dy;
        }
    }

    class ScaleHandler implements MouseWheelListener { // наш класс событий мышки для изменения размера

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            doScale(e);
        }

        private void doScale(MouseWheelEvent e) {

            int x = e.getX();
            int y = e.getY();

            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

                if (zrect.isHit(x, y)) { // если на квадрате

                    float amount =  e.getWheelRotation() * 5f; // получить новые размеры, от значений, прокрутки колёсика мышки
                    zrect.addWidth(amount);
                    zrect.addHeight(amount); // установка размера
                    repaint(); //перерисовать
                }

                if (zell.isHit(x, y)) {

                    float amount =  e.getWheelRotation() * 5f;
                    zell.addWidth(amount);// установка размера
                    zell.addHeight(amount);
                    repaint();//перерисовать
                }
            }
        }
    }
}

public class Main extends JFrame { //------------------------------------------------------------- Основной класс

    public Main() { // конструктор

        initUI();
    }

    private void initUI() { // инициализация

        add(new Surface()); // добавить компонент на форму

        setTitle("Moving and scaling"); // заголовок
        setSize(300, 300); // размер
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // выход
        setLocationRelativeTo(null); // по центру
    }

    public static void main(String[] args) { // основной метод -> запуск

        EventQueue.invokeLater(new Runnable() { // наш поток

            @Override
            public void run() { // запуск
                Main ex = new Main(); // создание класса
                ex.setVisible(true); // видимость
            }
        });
    }
}
