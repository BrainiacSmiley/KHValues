/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khvalues;

import java.awt.Dimension;
import java.io.Serializable;

public class Room implements Serializable {
    private boolean _isBuild;
    private boolean _isUsed;
    private boolean _isCleaned;

    private int _UpgradeLevel;
    private final int _MaxUpgradeLevel;
    private int _LevelNeeded;
    private int _ID;
    private Dimension _Size;

    private String _Name;

    private double[] _BuildingCostsHT;
    private double[] _BuildingCostsCoins;
    private double _DemolitionCosts;
    private int[] _UpgradePoints;

    public Room() {
        _MaxUpgradeLevel = 0;
    }

    public Room(int id, String name, int levelNeeded, int maxUpgradeLevel, Dimension size) {
        _ID = id;
        _Name = name;
        _LevelNeeded = levelNeeded;
        _isBuild = false;
        _isUsed = false;
        _isCleaned = false;
        _UpgradeLevel = 0;
        _MaxUpgradeLevel = maxUpgradeLevel;
        _Size = size;
    }

    public int getID() {
        return _ID;
    }

    public String getName() {
        return _Name;
    }

    public int getLevelNeeded() {
        return _LevelNeeded;
    }

    public boolean isBuild() {
        return _isBuild;
    }

    public void isBuild(boolean b) {
        _isBuild = b;
    }

    public boolean isUsed() {
        return _isUsed;
    }

    public void isUsed(boolean b) {
        _isUsed = b;
    }

    public boolean isCleaned() {
        return _isCleaned;
    }

    public void isCleaned(boolean b) {
        _isCleaned = b;
    }

    public void setBuildingCosts(double[] costsHT, double[] costsCoins) {
        _BuildingCostsHT = costsHT;
        _BuildingCostsCoins = costsCoins;
        _DemolitionCosts = costsHT[0]/10;
    }

    public double[] getBuildingCosts(int level) {
        double[] buildingCosts = new double[2];

        if (level <= _MaxUpgradeLevel) {
            buildingCosts[0] = _BuildingCostsHT[level];
            buildingCosts[1] = _BuildingCostsCoins[level];
        } else {
            buildingCosts[0] = -2;
            buildingCosts[1] = -2;
        }
        return buildingCosts;
    }

    public double[] getBuildingCostsSumFromLevel() {
        return getBuildingCostsSumFromLevel(_UpgradeLevel);
    }

    public double[] getBuildingCostsSumFromLevel(int level) {
        double[] buildingCostsSum = new double[2];
        buildingCostsSum[0] = 0;
        buildingCostsSum[1] = 0;
        if (level <= _MaxUpgradeLevel) {
            for (int x = level; x < _BuildingCostsHT.length; x++) {
                buildingCostsSum[0] += _BuildingCostsHT[x];
                buildingCostsSum[1] += _BuildingCostsCoins[x];
            }
        } else {
                buildingCostsSum[0] = -2;
                buildingCostsSum[1] = -2;
        }
        return buildingCostsSum;
    }

    public double[] getBuildingCostsSumUpToLevel() {
        return getBuildingCostsSumUpToLevel(_UpgradeLevel);
    }

    public double[] getBuildingCostsSumUpToLevel(int level) {
        double[] buildingCostsSum = new double[2];
        buildingCostsSum[0] = 0;
        buildingCostsSum[1] = 0;
        if (level <= _MaxUpgradeLevel) {
            for (int x = 0; x <= level; x++) {
                buildingCostsSum[0] += _BuildingCostsHT[x];
                buildingCostsSum[1] += _BuildingCostsCoins[x];
            }
        } else {
                buildingCostsSum[0] = -2;
                buildingCostsSum[1] = -2;
        }
        return buildingCostsSum;
    }

    public void setDemolitionCosts(double d) {
        _DemolitionCosts = d;
    }

    public double getDemolitionCosts() {
        return _DemolitionCosts;
    }

    public void setUpgradePoints(int[] points) {
        _UpgradePoints = points;
    }

    public int getUpgradePoints() {
        int upgradePoints = 0;
        for (int x = 0; x < _UpgradeLevel+1; x++) {
            if (_UpgradePoints[x] != -1) {
                upgradePoints += _UpgradePoints[x];
            }
        }
        return upgradePoints;
    }

    public int getUpgradePoints(int level) {
        return _UpgradePoints[level];
    }

    public boolean isUpgradable() {
        if (_UpgradeLevel < _MaxUpgradeLevel) {
            return true;
        } else {
            return false;
        }
    }

    public void upgradeRoom() {
        if (_UpgradeLevel < _MaxUpgradeLevel) {
            _UpgradeLevel ++;
        }
    }

    public void downgradeRoom() {
        if (_UpgradeLevel > 0) {
            _UpgradeLevel--;
        }
    }

    public int getUpgradeLevel() {
        return _UpgradeLevel;
    }

    public Dimension getSize() {
        return _Size;
    }

    public int getMaxUpgradeLevel() {
        return _MaxUpgradeLevel;
    }
}