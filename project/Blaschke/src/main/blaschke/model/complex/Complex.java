package main.blaschke.model.complex;

import java.util.Objects;

public class Complex {

    private double real;
    private double imaginary;

    public Complex() {
        this.real = 0.0;
        this.imaginary = 0.0;
    }

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public Complex(double argument) {
        real = Math.cos(argument);
        imaginary = Math.sin(argument);
    }

    public Complex(Complex complex) {
        real = complex.real;
        imaginary = complex.imaginary;
    }

    public Complex round() {
        return new Complex(Math.round(real * 10000) / 10000.0, Math.round(imaginary * 10000) / 10000.0);
    }

    public double modulus() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public double argument() {
        return Math.atan2(imaginary, real);
    }

    public Complex opposite() {
        return new Complex(-real, -imaginary);
    }

    public Complex reciprocal() throws ArithmeticException {
        double denominator = real*real + imaginary*imaginary;
        if (denominator == 0) {
            throw new ArithmeticException("0+0i doesn't have reciprocal");
        } else {
            return new Complex(real/denominator, -imaginary/denominator);
        }
    }

    public Complex conjugate() {
        return new Complex(real, -imaginary);
    }

    public Complex add(Complex w) {
        real += w.real;
        imaginary += w.imaginary;
        return this;
    }

    public Complex subtract(Complex w) {
        real -= w.real;
        imaginary -= w.imaginary;
        return this;
    }

    public Complex multiply(Complex w) {
        double real = this.real*w.real - this.imaginary*w.imaginary;
        double imaginary = this.real*w.imaginary + this.imaginary*w.real;
        this.real = real;
        this.imaginary = imaginary;
        return this;
    }

    public Complex divide(Complex w) throws IllegalArgumentException {
        double denominator = w.real*w.real + w.imaginary*w.imaginary;
        if (denominator == 0) {
            throw new IllegalArgumentException("Can't divide by zero");
        } else {
            Complex numerator = multiply(this, w.conjugate());
            real = (numerator.real)/denominator;
            imaginary = (numerator.imaginary)/denominator;
            return this;
        }
    }

    public Complex power(int exponent) {
        double args = this.argument();
        double length = this.modulus();
        real = Math.pow(length, exponent) * Math.cos(exponent * args);
        imaginary = Math.pow(length, exponent) * Math.sin(exponent * args);
        return this;
    }

    public Complex sqrt() {
        double args = this.argument();
        double length = this.modulus();
        real = Math.sqrt(length) * Math.cos(args/2);
        imaginary = Math.sqrt(length) * Math.sin(args/2);
        return this;
    }

    public Complex cbrt() {
        double args = this.argument();
        double length = this.modulus();
        real = Math.cbrt(length) * Math.cos(args/3);
        imaginary = Math.cbrt(length) * Math.sin(args/3);
        return this;
    }

    //Static methods
    public static Complex add(Complex z, Complex w) {
        double re = z.real+w.real;
        double im = z.imaginary+w.imaginary;
        return new Complex(re, im);
    }

    public static Complex subtract(Complex z, Complex w) {
        double re = z.real-w.real;
        double im = z.imaginary-w.imaginary;
        return new Complex(re, im);
    }

    public static Complex multiply(Complex z, Complex w) {
        double re = z.real*w.real - z.imaginary*w.imaginary;
        double im = z.real*w.imaginary + z.imaginary*w.real;
        return new Complex(re, im);
    }

    public static Complex divide(Complex z, Complex w) throws IllegalArgumentException {
        double denominator = w.real*w.real + w.imaginary*w.imaginary;
        if (denominator == 0) {
            throw new IllegalArgumentException("Second parameter cannot be 0");
        } else {
            Complex numerator = multiply(z, w.conjugate());
            return new Complex((numerator.real)/denominator, (numerator.imaginary)/denominator);
        }
    }

    public static Complex power(Complex z, int exponent) {
        Complex complex = new Complex(z);
        complex.power(exponent);
        return complex;
    }

    public static Complex sqrt(Complex z) {
        Complex complex = new Complex(z);
        complex.sqrt();
        return complex;
    }

    public static Complex cbrt(Complex z) {
        Complex complex = new Complex(z);
        complex.cbrt();
        return complex;
    }

    //Getters and Setters
    public double getReal() { return real; }

    public double getImaginary() { return imaginary; }

    public void setReal(double real) { this.real = real; }

    public void setImaginary(double imaginary) { this.imaginary = imaginary; }

    //Overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Complex)) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.real, real) == 0 &&
                Double.compare(complex.imaginary, imaginary) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

    @Override
    public String toString() {
        return (imaginary >= 0) ? (real + " + " + imaginary + "i") : (real + " - " + -imaginary + "i");
    }
}
