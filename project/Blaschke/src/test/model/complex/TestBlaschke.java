package test.model.complex;

import main.blaschke.model.complex.Blaschke;
import main.blaschke.model.complex.Complex;
import org.junit.*;

import static org.junit.Assert.assertEquals;

public class TestBlaschke {

    private Blaschke blaschke1;
    private Blaschke blaschke2;
    private Blaschke blaschke3;
    private Blaschke blaschke4;
    private Blaschke blaschke5;

    @Before
    public void setup() {
        blaschke1 = new Blaschke();
        blaschke2 = new Blaschke(2, -8);
        blaschke3 = new Blaschke(45);
        blaschke4 = new Blaschke(new Complex(-3, 6));
        blaschke5 = new Blaschke(new Complex(0.5, 0.2));

    }

    @Test(expected = ArithmeticException.class)
    public void testEvaluate() {
        Complex z = new Complex(0.3, -0.2);
        Blaschke blaschkeEx = new Blaschke(1, 0);
        blaschkeEx.evaluate(new Complex(1, 0));

        assertEquals(new Complex(0.3, -0.2), blaschke1.evaluate(z));
        assertEquals(new Complex(-2.492647058823529, -2.3455882352941178), blaschke2.evaluate(z));
        assertEquals(new Complex(-0.707107, 0.707107), blaschke3.evaluate(z));
        assertEquals(new Complex(0.2524886877828054, -2.0977375565610859), blaschke4.evaluate(z));
        assertEquals(new Complex(-0.29595206065794301, 0.3962333374098079), blaschke5.evaluate(z));
    }

}
