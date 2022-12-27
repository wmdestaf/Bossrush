package physics;

import java.awt.Dimension;

import javax.print.attribute.standard.MediaSize.Other;

import util.Utils;

public class Vec2 {
    private double x;
    private double y;

    public static final Vec2 ZERO = new Vec2(0,0);
    public static final Vec2 UNIT = new Vec2(1,1);
    
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vec2(Vec2 other) {
    	this.x = other.x;
    	this.y = other.y;
    }
    
    public Vec2(Dimension other) {
    	this.x = other.getWidth();
    	this.y = other.getHeight();
    }
    
    public Vec2 intVec() {
    	return new Vec2(getXi(), getYi());
    }

    public Vec2 dup() {
    	return new Vec2(x, y);
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public int getXi() {
    	return (int)x;
    }
    
    public int getYi() {
    	return (int)y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double len() {
        return Math.sqrt(x * x + y * y);
    }

    public double dist(Vec2 other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 add(double a) {
    	return new Vec2(x + a, y + a);
    }
    
    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }
    
    public Vec2 sub(double a) {
    	return new Vec2(x - a, y - a);
    }
    
    public Vec2 mul(Vec2 other) {
        return new Vec2(x * other.x, y * other.y);
    }

    public Vec2 div(Vec2 other) {
        return new Vec2(x / other.x, y / other.y);
    }

    public Vec2 div(double a) {
    	return new Vec2(x / a, y / a);
    }
    
    public Vec2 scl(double scalar) {
        return new Vec2(x * scalar, y * scalar);
    }

    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public double crs(Vec2 other) {
        return x * other.y - y * other.x;
    }
    
    public Vec2 neg() {
    	return new Vec2(-x, -y);
    }
    
    public Vec2 negX() {
    	return new Vec2(-x, y);
    }
    
    public Vec2 negY() {
    	return new Vec2(x, -y);
    }

    public double ang(Vec2 other) {
        double dotProduct = dot(other);
        double lengthProduct = len() * other.len();
        return Math.acos(dotProduct / lengthProduct);
    }

    public Vec2 rot(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double newX = x * cos - y * sin;
        double newY = x * sin + y * cos;
        return new Vec2(newX, newY);
    }

    public Vec2 addX(double x) {
        return new Vec2(this.x + x, y);
    }

    public Vec2 addY(double y) {
        return new Vec2(x, this.y + y);
    }

    public Vec2 subX(double x) {
        return new Vec2(this.x - x, y);
    }

    public Vec2 subY(double y) {
        return new Vec2(x, this.y - y);
    }

    public Vec2 mulX(double x) {
        return new Vec2(this.x * x, y);
    }

    public Vec2 mulY(double y) {
        return new Vec2(x, this.y * y);
    }

    public Vec2 divX(double x) {
        return new Vec2(this.x / x, y);
    }

    public Vec2 divY(double y) {
        return new Vec2(x, this.y / y);
    }
    
    public Vec2 addX(Vec2 other) {
        return new Vec2(x + other.x, y);
    }

    public Vec2 addY(Vec2 other) {
        return new Vec2(x, y + other.y);
    }

    public Vec2 subX(Vec2 other) {
        return new Vec2(x - other.x, y);
    }

    public Vec2 subY(Vec2 other) {
        return new Vec2(x, y - other.y);
    }

    public Vec2 mulX(Vec2 other) {
        return new Vec2(x * other.x, y);
    }

    public Vec2 mulY(Vec2 other) {
        return new Vec2(x, y * other.y);
    }

    public Vec2 divX(Vec2 other) {
        return new Vec2(x / other.x, y);
    }

    public Vec2 divY(Vec2 other) {
        return new Vec2(x, y / other.y);
    }
    
    /**
     * 
     * @return Normalized vec2
     */
    public Vec2 norm() {
        double length = len();
        return new Vec2(x / length, y / length);
    }
    
    public double L1() {
        return Math.abs(x) + Math.abs(y);
    }

    public double L2() {
        return len();
    }

    public double LINF() {
        return Math.max(Math.abs(x), Math.abs(y));
    }
    
    public Vec2 sign() {
    	return new Vec2(Utils.signum(getX()), Utils.signum(getY()));
    }

    public static Vec2 clamp(Vec2 p, Vec2 min, Vec2 max) {
		return new Vec2(
			Utils.clamp(p.getX(), min.getX(), max.getX()),
			Utils.clamp(p.getY(), min.getY(), max.getY())
		);		
	}
    
    public Vec2 clamp(Vec2 min, Vec2 max) {
    	return clamp(this, min, max);
    }
    
    public Vec2 clampX(double min, double max) {
    	return new Vec2(Utils.clamp(x, min, max), y);		
    }
    
    public Vec2 clampY(double min, double max) {
    	return new Vec2(x, Utils.clamp(y, min, max));
    }
    
    @Override
    public String toString() {
    	return String.format("[%03.3g,%03.3g]", getX(), getY());
    }

	public void set(Vec2 other) {
		this.x = other.x;
		this.y = other.y;
	}

	public Vec2 mul(double d) {
		return this.scl(d);
	}
}
