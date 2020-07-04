package main.blaschke.model.complex;

import javafx.scene.shape.*;
import main.blaschke.model.util.MathUtil;
import main.blaschke.model.shapes.*;
import main.blaschke.controllers.MainController;

public class Blaschke {

    private static final int scale = MainController.getScale();
    private Complex a;

    public Blaschke() {
        a = new Complex(0, 0);
    }

    public Blaschke(double re, double im) {
        a = new Complex(re, im);
    }

    public Blaschke(double a) {
        this.a = new Complex(a);
    }

    public Blaschke(Complex a) {
        this.a = a;
    }

    public Complex evaluate(Complex z) throws ArithmeticException {
        Complex denominator = Complex.subtract(new Complex(1, 0), Complex.multiply(a.conjugate(), z));
        if (denominator.equals(new Complex(0, 0))) {
            throw new ArithmeticException("Denominator is 0");
        } else {
            Complex numerator = Complex.subtract(z, a);
            return Complex.divide(numerator, denominator);
        }
    }

    public static void transform(TIShape tiShape, Blaschke blaschke) {
        if (tiShape instanceof TIPoint) {
            transformPoint((TIPoint) tiShape, blaschke);
        } else if (tiShape instanceof TILine) {
            transformLine((TILine) tiShape, blaschke);
        } else if (tiShape instanceof TIArc) {
            transformArc((TIArc) tiShape, blaschke);
        } else {
            transformCircle((TICircle) tiShape, blaschke);
        }
    }

    private static void transformPoint(TIPoint tiPoint, Blaschke blaschke) {
        Circle zPoint = tiPoint.getZShape();
        Circle wPoint = tiPoint.getWShape();
        Complex center = blaschke.evaluate(new Complex(
                zPoint.getCenterX() / scale, zPoint.getCenterY() / scale
        ));
        wPoint.setCenterX(center.getReal() * scale);
        wPoint.setCenterY(center.getImaginary() * scale);
    }

    private static void transformLine(TILine tiLine, Blaschke blaschke) {
        Line zLine = tiLine.getZShape();
        if (blaschke.getA().equals(new Complex(0, 0))) {
            tiLine.setWShapeToLine();
        } else {
            double midX = (zLine.getStartX() + zLine.getEndX()) / 2;
            double midY = (zLine.getStartY() + zLine.getEndY()) / 2;
            Complex start = blaschke.evaluate(new Complex(
                    zLine.getStartX() / scale, zLine.getStartY() / scale
            ));
            Complex mid = blaschke.evaluate(new Complex(
                    midX / scale, midY / scale
            ));
            Complex end = blaschke.evaluate(new Complex(
                    zLine.getEndX() / scale, zLine.getEndY() / scale
            ));
            Shape shape = MathUtil.getCircleFrom3Points(start, mid, end, true);
            if (shape instanceof Line) {
                tiLine.setWShapeToLine();
                Line line = (Line) shape;
                Line wLine = (Line) tiLine.getWShape();
                wLine.setStartX(scale * line.getStartX());
                wLine.setStartY(scale * line.getStartY());
                wLine.setEndX(scale * line.getEndX());
                wLine.setEndY(scale * line.getEndY());
            } else {
                tiLine.setWShapeToCircle();
                Circle circle = (Circle) shape;
                Circle wCircle = (Circle) tiLine.getWShape();
                wCircle.setCenterX(scale * circle.getCenterX());
                wCircle.setCenterY(scale * circle.getCenterY());
                wCircle.setRadius(scale * circle.getRadius());
            }
        }
    }

    private static void transformArc(TIArc tiArc, Blaschke blaschke) {
        Arc zArc = tiArc.getZShape();
        double zCenterX = zArc.getCenterX();
        double zCenterY = zArc.getCenterY();
        double zR = zArc.getRadiusX();
        double zStartAngle = zArc.getStartAngle();
        double zAngle = zArc.getLength();
        if (blaschke.getA().equals(new Complex(0, 0))) {
            tiArc.setWShapeToArc();
        } else {
            Complex p1 = blaschke.evaluate(new Complex(
                    (zCenterX + zR * Math.cos(Math.toRadians(zStartAngle))) / scale,
                    (zCenterY - zR * Math.sin(Math.toRadians(zStartAngle))) / scale
            ));
            Complex p2 = blaschke.evaluate(new Complex(
                    (zCenterX + zR * Math.cos(Math.toRadians(zStartAngle + zAngle / 2))) / scale,
                    (zCenterY - zR * Math.sin(Math.toRadians(zStartAngle + zAngle / 2))) / scale
            ));
            Complex p3 = blaschke.evaluate(new Complex(
                    (zCenterX + zR * Math.cos(Math.toRadians(zStartAngle + zAngle))) / scale,
                    (zCenterY - zR * Math.sin(Math.toRadians(zStartAngle + zAngle))) / scale
            ));
            Circle circle = (Circle) MathUtil.getCircleFrom3Points(p1, p2, p3, false);
            double wCenterX = scale * circle.getCenterX();
            double wCenterY = scale * circle.getCenterY();
            double wR = scale * circle.getRadius();
            double disP1ToP3 = MathUtil.getDistance(scale * p1.getReal(), scale * p1.getImaginary(), scale * p3.getReal(), scale * p3.getImaginary());
            double c2 = MathUtil.getDistance(scale * p1.getReal(), scale * p1.getImaginary(), wCenterX + wR, wCenterY);
            double disP1ToP2 = scale * MathUtil.getDistance(p1.getReal(), p1.getImaginary(), p2.getReal(), p2.getImaginary());
            double wStartAngle = MathUtil.cosLaw(wR, wR, c2);
            double wAngle = MathUtil.cosLaw(wR, wR, disP1ToP3);
            if (zAngle >= 360) {
                tiArc.setWShapeToArc();
                Arc wArc = (Arc) tiArc.getWShape();
                wArc.setCenterX(wCenterX);
                wArc.setCenterY(wCenterY);
                wArc.setRadiusX(wR);
                wArc.setRadiusY(wR);
                wArc.setStartAngle(0);
                wArc.setLength(360);
            } else {
                tiArc.setWShapeToPath();
                Path wArc = (Path) tiArc.getWShape();
                wArc.getElements().clear();
                MoveTo moveTo = new MoveTo(p1.getReal() * scale, p1.getImaginary() * scale);
                ArcTo arcTo = new ArcTo();
                arcTo.setX(p3.getReal() * scale);
                arcTo.setY(p3.getImaginary() * scale);
                arcTo.setRadiusX(wR);
                arcTo.setRadiusY(wR);
                if (zAngle > 180) {
                    arcTo.setLargeArcFlag(true);
                    arcTo.setSweepFlag(false);
                }
                wArc.getElements().addAll(moveTo, arcTo);
            }
        }
    }

    private static void transformCircle(TICircle tiCircle, Blaschke blaschke) {
        Circle zCircle = tiCircle.getZShape();
        Circle wCircle = tiCircle.getWShape();
        double centerX = zCircle.getCenterX();
        double centerY = zCircle.getCenterY();
        double r = zCircle.getRadius();
        if (blaschke.getA().equals(new Complex(0, 0))) {
            wCircle.setCenterX(centerX);
            wCircle.setCenterY(centerY);
            wCircle.setRadius(r);
        } else {
            Complex p1 = blaschke.evaluate(new Complex((r + centerX) / scale, centerY / scale));
            Complex p2 = blaschke.evaluate(new Complex(centerX / scale, (r + centerY) / scale));
            Complex p3 = blaschke.evaluate(new Complex((-r + centerX) / scale, centerY / scale));
            Circle circle = (Circle) MathUtil.getCircleFrom3Points(p1, p2, p3, false);
            wCircle.setCenterX(scale * circle.getCenterX());
            wCircle.setCenterY(scale * circle.getCenterY());
            wCircle.setRadius(scale * circle.getRadius());
        }
    }

    public Complex getA() {
        return new Complex(a);
    }

    public void setA(Complex a) {
        this.a = a;
    }

    public void setA(double re, double im) {
        a.setReal(re);
        a.setImaginary(im);
    }

    public void setAReal(double re) {
        a.setReal(re);
    }

    public void setAImaginary(double im) {
        a.setImaginary(im);
    }

    @Override
    public String toString() {
        return "a = " + a;
    }

}
