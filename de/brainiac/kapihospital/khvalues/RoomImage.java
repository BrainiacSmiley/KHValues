/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khvalues;

import java.awt.Image;

public class RoomImage {
    private Image _RoomImage;
    private int _ID, _Level;
    private boolean _Used, _Cleaning, _Building;

    public RoomImage(Image roomImage, int id, int level, boolean used, boolean cleaning, boolean building) {
        _RoomImage = roomImage;
        _ID = id;
        _Level = level;
        _Used = used;
        _Cleaning = cleaning;
        _Building = building;
    }

    public Image getImage() {
        return _RoomImage;
    }

    public int getID() {
        return _ID;
    }

    public int getLevel() {
        return _Level;
    }

    public boolean isUsed() {
        return _Used;
    }

    public boolean isCleaning() {
        return _Cleaning;
    }

    public boolean isBuilding() {
        return _Building;
    }

    @Override
    public boolean equals(Object o) {
        return o != null 
            && o.getClass() == getClass()
            && equals((RoomImage)o);
    }
 
    private boolean equals(RoomImage other){
        //Behandlungsräume (Buldining|Upgrades|Used|Cleaning)
        if (_ID == 0 || _ID == 2 || _ID == 7 || _ID == 9 || _ID == 11 || _ID == 15 || _ID == 16 || _ID == 17 || _ID == 21 || _ID == 22 || _ID == 23 || _ID == 24 || _ID == 25 || _ID == 26) {
            return _RoomImage.equals(other.getImage())
                && _ID == other.getID()
                && _Level == other.getLevel()
                && _Used == other.isUsed()
                && _Cleaning == other.isCleaning()
                && _Building == other.isBuilding();
                //&& hashCode() == other.hashCode();
        }
        //not useable Deco
        else if (_ID == -7 || _ID == -6 || _ID == -5 || _ID == -4 || _ID == -3 || _ID == -2 || _ID == 3 || _ID == 5 || _ID == 6 || _ID == 8 || _ID == 13 || _ID == 18 || _ID == 20 || _ID == 28) {
            return _RoomImage.equals(other.getImage())
                && _ID == other.getID();
                //&& hashCode() == other.hashCode();
        }
        //useable Deco
        else if (_ID == 1 || _ID == 10 || _ID == 12 || _ID == 14 || _ID == 19 || _ID == 27 || _ID == 29) {
            return _RoomImage.equals(other.getImage())
                && _ID == other.getID()
                && _Used == other.isUsed();
                //&& hashCode() == other.hashCode();
        }
        //upgradeable + useable Rooms
        else if (_ID == 4 || _ID == 499999 || _ID == 30) {
            return _RoomImage.equals(other.getImage())
                && _ID == other.getID()
                && _Level == other.getLevel()
                && _Used == other.isUsed();
                //&& hashCode() == other.hashCode();
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this._RoomImage != null ? this._RoomImage.hashCode() : 0);
        hash = 71 * hash + this._ID;
        hash = 71 * hash + (this._Used ? 1 : 0);
        hash = 71 * hash + (this._Cleaning ? 1 : 0);
        hash = 71 * hash + (this._Building ? 1 : 0);
        return hash;
    }
}