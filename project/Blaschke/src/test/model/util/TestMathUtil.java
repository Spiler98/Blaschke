package test.model.util;

import javafx.scene.shape.Circle;
import main.blaschke.model.complex.Complex;
import main.blaschke.model.util.MathUtil;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestMathUtil {

    @Test
    public void testCosLaw() {
        assertEquals(117.28, MathUtil.cosLaw(6, 8, 12), 0.001);
        assertEquals(82.8192, MathUtil.cosLaw(4, 5, 6), 0.001);
        assertEquals(48.537, MathUtil.cosLaw(20.6, 4, 18.2), 0.001);
    }

    @Test
    public void testGetDistance() {
        assertEquals(0, MathUtil.getDistance(0, 0, 0, 0), 0);
        assertEquals(5, MathUtil.getDistance(0, 0, 3, 4), 0);
        assertEquals(Math.sqrt(8), MathUtil.getDistance(4, 5, 6, 7), 0);
        assertEquals(Math.sqrt(279157 / 15876.0 + 11 * Math.sqrt(13) / 7.0), MathUtil.getDistance(Math.sqrt(13) / 2, 23 / 9.0, -11 / 7.0, 6), 0);
    }

    @Test
    public void testCramerRule() {
        double[][] A1 = {{6, -3}, {2, 6}};
        double[][] A2 = {{1, 1}, {3, 3}};
        double[][] A3 = {{5, 3}, {7, -2}};
        double[][] A4 = {{Math.sqrt(Math.PI) / 3, Math.sqrt(3) / 2}, {Math.pow(Math.PI, 2) / Math.sqrt(11), 5 / 2.0}};
        double[] b1 = {3, 36};
        double[] b2 = {1, 1};
        double[] b3 = {11, 3};
        double[] b4 = {9.607, 35.354};

        assertArrayEquals(new double[]{3, 5}, MathUtil.cramerRule(A1, b1), 0);
        assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}, MathUtil.cramerRule(A2, b2), 0);
        assertArrayEquals(new double[]{1, 2}, MathUtil.cramerRule(A3, b3), 0);
        assertArrayEquals(new double[]{6, 7}, MathUtil.cramerRule(A4, b4), 0.1);
    }

    @Test
    public void testCircleFrom3Points() {
        Complex start1 = new Complex(2, 0);
        Complex mid1 = new Complex(4, 1);
        Complex end1 = new Complex(-1, 3);

        Complex start2 = new Complex(4, 2);
        Complex mid2 = new Complex(1, 1);
        Complex end2 = new Complex(3, 5);

        Complex start3 = new Complex(Math.sqrt(13) * Math.PI / Math.cbrt(Math.PI), Math.sqrt(Math.E));
        Complex mid3 = new Complex(2, 111);
        Complex end3 = new Complex(Math.sqrt(13) / Math.cbrt(11), Math.pow(13 * Math.E, -0.5));

        Circle circle1 = (Circle) MathUtil.getCircleFrom3Points(start1, mid1, end1, false);
        Circle circle2 = (Circle) MathUtil.getCircleFrom3Points(start2, mid2, end2, false);
        Circle circle3 = (Circle) MathUtil.getCircleFrom3Points(start3, mid3, end3, false);

        assertEquals(11 / 6.0, circle1.getCenterX(), 0);
        assertEquals(17 / 6.0, circle1.getCenterY(), 0);
        assertEquals(Math.sqrt(145 / 18.0), circle1.getRadius(), 0.00001);

        assertEquals(2, circle2.getCenterX(), 0);
        assertEquals(3, circle2.getCenterY(), 0);
        assertEquals(Math.sqrt(5), circle2.getRadius(), 0);

        assertEquals(-8.573258, circle3.getCenterX(), 0.01);
        assertEquals(55.61959, circle3.getCenterY(), 0.01);
        assertEquals(56.3807, circle3.getRadius(), 0.01);
    }

    @Test
    public void testLinear() {
        assertEquals(
                new Complex(0.6956521739130436, 0),
                MathUtil.linear(new Complex(0.3, 0), new Complex(0.5, 0))
        );
        assertEquals(
                new Complex(-0.16783216783216784, -1.202797202797203),
                MathUtil.linear(new Complex(-0.4, -0.1), new Complex(0.8, -0.9))
        );
        assertEquals(
                new Complex(-0.06220114002644207, -0.8128440024425769),
                MathUtil.linear(new Complex(0.456, -0.6), new Complex(-0.8, 0.21))
        );
        assertEquals(
                new Complex(0.8230472895281168, 0.5294625954638896),
                MathUtil.linear(
                        new Complex(
                                0.69, Math.PI * Math.sqrt(13) / Math.pow(Math.E, Math.PI)
                        ),
                        new Complex(
                                Math.pow(6, 0.25) / (2 * Math.cos(Math.toRadians(1))), Math.pow(Math.E, 1 / Math.PI) / Math.pow(Math.PI, Math.E)
                        )
                ));
    }

    @Test
    public void testQuadratic() {
        List<Complex> list1 = MathUtil.quadratic(
                new Complex(0.6, 0),
                new Complex(-0.8, 0),
                new Complex(0.2, 0)
        );
        List<Complex> list2 = MathUtil.quadratic(
                new Complex(0.5, -0.2),
                new Complex(0.2, 0.1),
                new Complex(0.2, 0.3)
        );

        assertEquals(new Complex(0.7180609577308804, 0), list1.get(0));
        assertEquals(new Complex(-0.8640463591907347, 0), list1.get(1));
        assertEquals(new Complex(0.7497832285295972, 0.07078205402563464), list2.get(0));
        assertEquals(new Complex(-0.13231380977099255, -0.38836277320570395), list2.get(1));
    }

    @Test
    public void testCubic() {
        List<Complex> list1 = MathUtil.cubic(
                new Complex(0.6, 0),
                new Complex(-0.8, 0),
                new Complex(-0.5, 0),
                new Complex(0.2, 0)
        );
        List<Complex> list2 = MathUtil.cubic(
                new Complex(0.5, -0.2),
                new Complex(0.2, -0.3),
                new Complex(0.2, 0.1),
                new Complex(0.2, 0.3)
        );

        assertEquals(new Complex(0.7292913128126389, -1.902423686300797E-17), list1.get(0));
        assertEquals(new Complex(-0.7348746640399073, -0.18881588658316106), list1.get(1));
        assertEquals(new Complex(-0.7348746640399073, 0.18881588658316106), list1.get(2));
        assertEquals(new Complex(0.8247679498755892, -0.05073415639888655), list2.get(0));
        assertEquals(new Complex(0.31402713702091944, -0.7235450778626604), list2.get(1));
        assertEquals(new Complex(-0.2590655611698117, 0.4727237982352931), list2.get(2));
    }
}
