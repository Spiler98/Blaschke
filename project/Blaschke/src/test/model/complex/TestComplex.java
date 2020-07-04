package test.model.complex;

import main.blaschke.model.complex.Complex;
import org.junit.*;

import static org.junit.Assert.*;

public class TestComplex {

    private Complex complex1;
    private Complex complex2;
    private Complex complex3;
    private Complex complex4;

    @Before
    public void setup() {
        complex1 = new Complex();
        complex2 = new Complex(2, -8);
        complex3 = new Complex(Math.toRadians(45));
        complex4 = new Complex(new Complex(-3, 6));
    }


    @Test
    public void testCtor() {
        assertNotNull(complex1);
        assertNotNull(complex2);
        assertNotNull(complex3);
        assertNotNull(complex4);
    }

    @Test
    public void testModulus() {
        assertEquals(0.0, complex1.modulus(), 0);
        assertEquals(Math.sqrt(68), complex2.modulus(), 0);
        assertEquals(Math.sqrt(2)*0.7071, complex3.modulus(), 0.00001);
        assertEquals(Math.sqrt(45), complex4.modulus(), 0);
    }

    @Test
    public void testArgument() {
        assertEquals(0.0, complex1.argument(), 0);
        assertEquals(Math.atan2(-8, 2), complex2.argument(), 0);
        assertEquals(Math.toRadians(45), complex3.argument(), 0.00001);
        assertEquals(Math.atan2(6, -3), complex4.argument(), 0);
    }

    @Test
    public void testOpposite() {
        assertEquals(new Complex(-0.0, -0.0), complex1.opposite());
        assertEquals(new Complex(-2, 8), complex2.opposite());
        assertEquals(new Complex(-Math.cos(Math.toRadians(45)), -Math.sin(Math.toRadians(45))), complex3.opposite());
        assertEquals(new Complex(3, -6), complex4.opposite());
    }

    @Test(expected = ArithmeticException.class)
    public void testReciprocal() {
        complex1.reciprocal();
        assertEquals(new Complex(1/34.0, 2/17.0), complex2.reciprocal());
        assertEquals(new Complex(1/Math.sqrt(2), -1/Math.sqrt(2)), complex3.reciprocal());
        assertEquals(new Complex(-1/35.0, -2/15.0), complex4.reciprocal());
    }

    @Test
    public void testConjugate() {
        assertEquals(new Complex(0.0, -0.0), complex1.conjugate());
        assertEquals(new Complex(2, 8), complex2.conjugate());
        assertEquals(new Complex(Math.cos(Math.toRadians(45)), -Math.sin(Math.toRadians(45))), complex3.conjugate());
        assertEquals(new Complex(-3, -6), complex4.conjugate());
    }

    @Test
    public void testAdd() {
        Complex c = new Complex(5, -8);

        assertEquals(
                new Complex(5, -8), complex1.add(c)
        );
        assertEquals(
                new Complex(7, -16), complex2.add(c)
        );
        assertEquals(
                new Complex(1/Math.sqrt(2) + 5, 1/Math.sqrt(2) + -8), complex3.add(c)
        );
        assertEquals(
                new Complex(2, -2), complex4.add(c)
        );
    }

    @Test
    public void testSubtract() {
        Complex c = new Complex(5, -8);

        assertEquals(
                new Complex(-5, +8), complex1.subtract(c)
        );
        assertEquals(
                new Complex(-3, 0), complex2.subtract(c)
        );
        assertEquals(
                new Complex(1/Math.sqrt(2) - 5, 1/Math.sqrt(2) - -8), complex3.subtract(c)
        );
        assertEquals(
                new Complex(-8, 14), complex4.subtract(c)
        );
    }

    @Test
    public void testMultiply() {
        Complex c = new Complex(5, -8);

        assertEquals(
                new Complex(0, 0), complex1.multiply(c)
        );
        assertEquals(
                new Complex(-54, -56), complex2.multiply(c)
        );
        assertEquals(
                new Complex(9.192388155425117, -2.1213203435596433), complex3.multiply(c)
        );
        assertEquals(
                new Complex(33, 54), complex4.multiply(c)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDivide() {
        Complex c = new Complex(5, -8);
        complex1.divide(new Complex(0, 0));

        assertEquals(
                new Complex(0, 0), complex1.multiply(c)
        );
        assertEquals(
                new Complex(74/89.0, -24/89.0), complex2.multiply(c)
        );
        assertEquals(
                new Complex(13/89.0/Math.sqrt(2), 3/89.0/Math.sqrt(2)), complex3.multiply(c)
        );
        assertEquals(
                new Complex(-63/89.0, 6/89.0), complex4.multiply(c)
        );
    }

    @Test
    public void testPower() {
        int exponent = 5;

        assertEquals(new Complex(0, 0), complex1.power(exponent));
        assertEquals(new Complex(35872, -12928.000000000007), complex2.power(exponent));
        assertEquals(new Complex(-0.7071067811865477, -0.7071067811865475), complex3.power(exponent));
        assertEquals(new Complex(-9962.999999999995, -9234.00000000001), complex4.power(exponent));
    }

    @Test
    public void testSqrt() {
        assertEquals(new Complex(0, 0), complex1.sqrt());
        assertEquals(new Complex(2.2634278485557388, -1.7672310617510267), complex2.sqrt());
        assertEquals(new Complex(0.9238795325112867, 0.3826834323650898), complex3.sqrt());
        assertEquals(new Complex(1.3616541287161303, 2.203202661184323), complex4.sqrt());
    }

    @Test
    public void testCbrt() {
        assertEquals(new Complex(0, 0), complex1.cbrt());
        assertEquals(new Complex(1.8262072221962422, -0.8640730776477378), complex2.cbrt());
        assertEquals(new Complex(0.9659258262890683, 0.25881904510252074), complex3.cbrt());
        assertEquals(new Complex(1.468674748379278, 1.1831685374295275), complex4.cbrt());
    }

    @Test
    public void testStaticAdd() {
        Complex c = new Complex(5, -8);

        assertEquals(new Complex(5, -8), Complex.add(complex1, c));
        assertEquals(new Complex(7, -16), Complex.add(complex2, c));
        assertEquals(new Complex(1/Math.sqrt(2) + 5, 1/Math.sqrt(2) + -8), Complex.add(complex3, c));
        assertEquals(new Complex(2, -2), Complex.add(complex4, c));
    }

    @Test
    public void testStaticSubtract() {
        Complex c = new Complex(5, -8);

        assertEquals(new Complex(-5, +8), Complex.subtract(complex1, c));
        assertEquals(new Complex(-3, 0), Complex.subtract(complex2, c));
        assertEquals(new Complex(1/Math.sqrt(2) - 5, 1/Math.sqrt(2) - -8), Complex.subtract(complex3, c));
        assertEquals(new Complex(-8, 14), Complex.subtract(complex4, c));
    }

    @Test
    public void testStaticMultiply() {
        Complex c = new Complex(5, -8);

        assertEquals(new Complex(0, 0), Complex.multiply(complex1, c));
        assertEquals(new Complex(-54, -56), Complex.multiply(complex2, c));
        assertEquals(new Complex(9.192388155425117, -2.1213203435596433), Complex.multiply(complex3, c));
        assertEquals(new Complex(33, 54), Complex.multiply(complex4, c));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStaticDivide() {
        Complex c = new Complex(5, -8);
        Complex.divide(c, new Complex(0, 0));

        assertEquals(new Complex(0, 0), Complex.divide(complex1, c));
        assertEquals(new Complex(74/89.0, -24/89.0), Complex.divide(complex2, c));
        assertEquals(new Complex(13/89.0/Math.sqrt(2), 3/89.0/Math.sqrt(2)), Complex.divide(complex3, c));
        assertEquals(new Complex(-63/89.0, 6/89.0), Complex.divide(complex4, c));
    }

    @Test
    public void testStaticPower() {
        int exponent = 5;

        assertEquals(new Complex(0, 0), Complex.power(complex1, exponent));
        assertEquals(new Complex(35872, -12928.000000000007), Complex.power(complex2, exponent));
        assertEquals(new Complex(-0.7071067811865477, -0.7071067811865475), Complex.power(complex3, exponent));
        assertEquals(new Complex(-9962.999999999995, -9234.00000000001), Complex.power(complex4, exponent));
    }

    @Test
    public void testStaticSqrt() {
        assertEquals(new Complex(0, 0), Complex.sqrt(complex1));
        assertEquals(new Complex(2.2634278485557388, -1.7672310617510267), Complex.sqrt(complex2));
        assertEquals(new Complex(0.9238795325112867, 0.3826834323650898), Complex.sqrt(complex3));
        assertEquals(new Complex(1.3616541287161303, 2.203202661184323), Complex.sqrt(complex4));
    }

    @Test
    public void testStaticCbrt() {
        assertEquals(new Complex(0, 0), Complex.cbrt(complex1));
        assertEquals(new Complex(1.8262072221962422, -0.8640730776477378), Complex.cbrt(complex2));
        assertEquals(new Complex(0.9659258262890683, 0.25881904510252074), Complex.cbrt(complex3));
        assertEquals(new Complex(1.468674748379278, 1.1831685374295275), Complex.cbrt(complex4));
    }

    @Test
    public void testGetReal() {
        assertEquals(0, complex1.getReal(), 0);
        assertEquals(2, complex2.getReal(), 0);
        assertEquals(0.7071, complex3.getReal(), 0.00001);
        assertEquals(-3, complex4.getReal(), 0);
    }

    @Test
    public void testGetImaginary() {
        assertEquals(0, complex1.getImaginary(), 0);
        assertEquals(-8, complex2.getImaginary(), 0);
        assertEquals(0.7071, complex3.getImaginary(), 0.00001);
        assertEquals(6, complex4.getImaginary(), 0);
    }

    @Test
    public void testSetReal() {
        complex1.setReal(5);
        complex2.setReal(6);
        complex3.setReal(7);
        complex4.setReal(8);

        assertEquals(5, complex1.getReal(), 0);
        assertEquals(6, complex2.getReal(), 0);
        assertEquals(7, complex3.getReal(), 0);
        assertEquals(8, complex4.getReal(), 0);
    }

    @Test
    public void testSetImaginary() {
        complex1.setImaginary(5);
        complex2.setImaginary(6);
        complex3.setImaginary(7);
        complex4.setImaginary(8);

        assertEquals(5, complex1.getImaginary(), 0);
        assertEquals(6, complex2.getImaginary(), 0);
        assertEquals(7, complex3.getImaginary(), 0.00001);
        assertEquals(8, complex4.getImaginary(), 0);
    }

    @Test
    public void testEquals() {
        Complex c1 = complex1;
        Complex c2 = new Complex(complex1);
        Complex c3 = new Complex(0, 0);

        assertEquals(complex1, c1);
        assertEquals(complex1, c2);
        assertEquals(complex1, c3);
        assertNotEquals(complex1, 2);
        assertNotEquals(complex1, complex2);
    }
}
