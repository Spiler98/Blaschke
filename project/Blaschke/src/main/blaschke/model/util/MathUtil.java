package main.blaschke.model.util;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import main.blaschke.model.complex.Blaschke;
import main.blaschke.model.complex.Complex;
import main.blaschke.controllers.MainController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class MathUtil {

    public static double cosLaw(double a, double b, double c) {
        return Math.toDegrees(Math.acos((a * a + b * b - c * c) / (2 * a * b)));
    }

    public static Shape getCircleFrom3Points(Complex start, Complex mid, Complex end, boolean canBeLine) {
        Complex center1 = new Complex(
                (start.getReal() + mid.getReal()) / 2,
                (start.getImaginary() + mid.getImaginary()) / 2
        );
        Complex center2 = new Complex(
                (mid.getReal() + end.getReal()) / 2,
                (mid.getImaginary() + end.getImaginary()) / 2
        );
        double slopeNormal1 = -((mid.getReal() - start.getReal()) / (mid.getImaginary() - start.getImaginary()));
        double slopeNormal2 = -((end.getReal() - mid.getReal()) / (end.getImaginary() - mid.getImaginary()));

        double[][] A = {{slopeNormal1, -1}, {slopeNormal2, -1}};
        double[] b = {
                slopeNormal1 * center1.getReal() - center1.getImaginary(),
                slopeNormal2 * center2.getReal() - center2.getImaginary()
        };
        double[] coords = MathUtil.cramerRule(A, b);

        if (canBeLine && (Double.isNaN(coords[0]) && Double.isNaN(coords[1]))) {
            Line wLine = new Line();
            final int delta = 10;
            wLine.setStartX(start.getReal() - delta * (end.getReal() - start.getReal()));
            wLine.setStartY(start.getImaginary() - delta * (end.getImaginary() - start.getImaginary()));
            wLine.setEndX(end.getReal() + delta * (end.getReal() - start.getReal()));
            wLine.setEndY(end.getImaginary() + delta * (end.getImaginary() - start.getImaginary()));
            return wLine;
        }
        Circle wCircle = new Circle();
        wCircle.setCenterX(coords[0]);
        wCircle.setCenterY(coords[1]);
        wCircle.setRadius(MathUtil.getDistance(coords[0], coords[1], start.getReal(), start.getImaginary()));
        return wCircle;
    }

    public static double getDistance(double x0, double y0, double x, double y) {
        return Math.sqrt(Math.pow(x - x0, 2) + Math.pow(y - y0, 2));
    }

    public static double[] cramerRule(double[][] A, double[] b) {
        double determinant = A[0][0] * A[1][1] - A[0][1] * A[1][0];
        double x = (b[0] * A[1][1] - b[1] * A[0][1]) / determinant;
        double y = (A[0][0] * b[1] - A[1][0] * b[0]) / determinant;
        return new double[]{x, y};
    }

    public static Complex linear(Complex blaschkeParameter, Complex constant) {
        return new Blaschke(
                Complex.multiply(new Complex(-1, 0), blaschkeParameter)
        ).evaluate(constant);
    }

    public static List<Complex> quadratic(Complex blaschke1, Complex blaschke2, Complex constant) {
        Complex a = new Complex(1, 0).subtract(
                Complex.multiply(constant, blaschke1.conjugate().multiply(blaschke2.conjugate()))
        );
        Complex b = Complex.subtract(
                Complex.multiply(constant, blaschke1.conjugate().add(blaschke2.conjugate())),
                Complex.add(blaschke1, blaschke2)
        );
        Complex c = Complex.multiply(blaschke1, blaschke2).subtract(constant);

        Complex minusB = new Complex(-1, 0).multiply(b);
        Complex denominator = new Complex(2, 0).multiply(a);
        Complex discriminant = Complex.sqrt(
                Complex.power(b, 2).subtract(new Complex(4, 0).multiply(Complex.multiply(a, c)))
        );

        Complex z1 = Complex.divide(
                Complex.add(minusB, discriminant),
                denominator
        );
        Complex z2 = Complex.divide(
                Complex.subtract(minusB, discriminant),
                denominator
        );

        List<Complex> solutions = new ArrayList<>();
        solutions.add(z1);
        solutions.add(z2);
        return solutions;
    }

    public static List<Complex> cubic(Complex blaschke1, Complex blaschke2, Complex blaschke3, Complex constant) {
        Complex a = new Complex(1, 0).add(Complex.multiply(
                constant,
                blaschke1.conjugate().multiply(blaschke2.conjugate()).multiply(blaschke3.conjugate())
        ));
        Complex p = new Complex(-1, 0).multiply(Complex.add(
                Complex.multiply(
                        constant,
                        Complex.add(
                                blaschke1.conjugate().multiply(blaschke2.conjugate()),
                                blaschke1.conjugate().multiply(blaschke3.conjugate())
                        ).add(blaschke2.conjugate().multiply(blaschke3.conjugate()))
                ),
                Complex.add(blaschke1, blaschke2).add(blaschke3)
        ));
        Complex q = Complex.add(
                Complex.multiply(
                        constant,
                        blaschke1.conjugate().add(blaschke2.conjugate()).add(blaschke3.conjugate())
                ),
                Complex.add(
                        Complex.multiply(blaschke1, blaschke2),
                        Complex.multiply(blaschke1, blaschke3)
                ).add(Complex.multiply(blaschke2, blaschke3))
        );
        Complex r = new Complex(-1, 0).multiply(Complex.add(
                constant,
                Complex.multiply(blaschke1, blaschke2).multiply(blaschke3)
        ));

        p = Complex.divide(p, a);
        q = Complex.divide(q, a);
        r = Complex.divide(r, a);

        Complex A = getA(p, q, r);
        Complex B = getB(p, q, A);
        return getSolutions(p, A, B);
    }


    private static Complex getA(Complex p, Complex q, Complex r) {
        Complex sumUnderSqrt = new Complex(
                new Complex(-1, 0).multiply(
                        Complex.power(p, 2).multiply(Complex.power(q, 2))
                )
        ).add(
                new Complex(4, 0).multiply(Complex.power(q, 3))
        ).add(
                new Complex(4, 0).multiply(Complex.power(p, 3).multiply(r))
        ).subtract(
                new Complex(18, 0).multiply(Complex.multiply(p, q).multiply(r))
        ).add(
                new Complex(27, 0).multiply(Complex.power(r, 2))
        );
        Complex denominator = new Complex(3 * Math.cbrt(2), 0);
        Complex numerator = Complex.cbrt(Complex.add(
                Complex.add(
                        new Complex(-2, 0).multiply(Complex.power(p, 3)),
                        new Complex(9, 0).multiply(Complex.multiply(p, q))
                ).subtract(new Complex(27, 0).multiply(r)),
                Complex.multiply(
                        new Complex(3 * Math.sqrt(3), 0),
                        Complex.sqrt(sumUnderSqrt)
                )
        ));
        return numerator.divide(denominator);
    }

    private static Complex getB(Complex p, Complex q, Complex A) {
        Complex denominator = new Complex(9, 0).multiply(A);
        Complex numerator = Complex.add(
                new Complex(-1, 0).multiply(Complex.power(p, 2)),
                new Complex(3, 0).multiply(q)
        );
        return numerator.divide(denominator);
    }

    private static List<Complex> getSolutions(Complex p, Complex A, Complex B) {
        Complex firstMember = Complex.divide(p, new Complex(-3, 0));
        Complex coeff1 = new Complex(-0.5, 0).add(new Complex(0, Math.sqrt(3) / 2));
        Complex coeff2 = new Complex(-0.5, 0).subtract(new Complex(0, Math.sqrt(3) / 2));

        Complex z1 = Complex.add(firstMember, A).subtract(B);
        Complex z2 = Complex.add(firstMember, Complex.multiply(coeff2, A)).subtract(Complex.multiply(coeff1, B));
        Complex z3 = Complex.add(firstMember, Complex.multiply(coeff1, A)).subtract(Complex.multiply(coeff2, B));

        List<Complex> solutions = new ArrayList<>();
        solutions.add(z1);
        solutions.add(z2);
        solutions.add(z3);
        return solutions;
    }

}
