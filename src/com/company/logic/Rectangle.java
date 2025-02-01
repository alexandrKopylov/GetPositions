package com.company.logic;

import com.company.logic.Point2D;

import java.util.Objects;

public class Rectangle {
    private Point2D pointMin;
    private Point2D pointMax;
    private double shirina;
    private double dlinna;


    public Rectangle(Point2D pointMin, Point2D pointMax) {
        this.pointMin = pointMin;
        this.pointMax = pointMax;
        shirina = pointMax.getY() - pointMin.getY();
        dlinna = pointMax.getX() - pointMin.getX();
    }

    public Point2D getPointMin() {
        return pointMin;
    }

    public Point2D getPointMax() {
        return pointMax;
    }

    public double getShirina() {
        return shirina;
    }

    public double getDlinna() {
        return dlinna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Objects.equals(pointMin, rectangle.pointMin) &&
                Objects.equals(pointMax, rectangle.pointMax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointMin, pointMax);
    }

    @Override
    public String toString() {
        return "Rec{ (" + pointMin + "), (" + pointMax + ")}";
    }
}
