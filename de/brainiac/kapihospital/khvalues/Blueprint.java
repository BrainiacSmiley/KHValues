/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khvalues;

import java.awt.Image;

public class Blueprint {
    private int _ID;
    private Image _Blueprint;

    public Blueprint(int id, Image blueprint) {
        _ID = id;
        _Blueprint = blueprint;
    }

    public int getID() {
        return _ID;
    }

    public Image getImage() {
        return _Blueprint;
    }
}