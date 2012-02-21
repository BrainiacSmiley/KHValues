/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khvalues;

public class UpgradeEvaluation {
    private Double _Costs;
    private Double _CostsPerPoint;
    private int _RoomID;
    private int _UpgradePoints;
    private int _FromLevel;
    private int _ToLevel;
    private String _RoomName;

    public UpgradeEvaluation(int roomID, String roomName, int fromLevel, int toLevel, Double costs, int upgradePoints) {
        _RoomID = roomID;
        _RoomName = roomName;
        _FromLevel = fromLevel;
        _ToLevel = toLevel;
        _Costs = costs;
        _UpgradePoints = upgradePoints;
        if (upgradePoints < 0) {
            _CostsPerPoint = 99999.99;
        } else {
            _CostsPerPoint = costs/upgradePoints;
        }
    }

    public String getUpgradeDescription() {
        return "Upgrade des " + _RoomName + " von Level " + _FromLevel + " auf Level " + _ToLevel;
    }

    public Double getCostsPerPoint() {
        return _CostsPerPoint;
    }

    public String getRoom() {
        return _RoomName;
    }

    public int getRoomID() {
        return _RoomID;
    }

    public int getForWichLevel() {
        return _FromLevel;
    }

    public int getToWichLevel() {
        return _ToLevel;
    }

    public Double getCosts() {
        return _Costs;
    }

    public int getUpgradePoints() {
        return _UpgradePoints;
    }

    public int getNumberOfUpgrades() {
        return _ToLevel-_FromLevel;
    }
}