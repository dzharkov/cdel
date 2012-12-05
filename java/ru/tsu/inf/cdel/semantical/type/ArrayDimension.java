/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.tsu.inf.cdel.semantical.type;

public class ArrayDimension {
    private int from, to;

    public ArrayDimension(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

}