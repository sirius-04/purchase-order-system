/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Chan Yong Liang
 */
public class IdMeta {
    public final String prefix;
    public final int padLength;
    public final String fileName;

    public IdMeta(String prefix, int padLength, String fileName) {
        this.prefix = prefix;
        this.padLength = padLength;
        this.fileName = fileName;
    }
}

