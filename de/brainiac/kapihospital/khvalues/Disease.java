/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khvalues;

public class Disease {
    public static final double LEVELBONUS = 0.025;
    private int _id;
    private String _name;
    private int _pictureOffset;
    private int _durationInSeconds;
    private int _roomNeeded;
    private int _levelNeeded;
    private int _medicinId;
    private boolean _medicinUsed, _treated, _epidemic;
    private double _basePointValue;
    private int _epidemicPointsPA, _epidemicPoints;

    //Nasenflügelakne - Stahlbürste - 00:10:00 - 0,21 hT - Behandlungsraum - 1 - Blutiger Anfänger
    public Disease(int id, String name, String durationString, int roomNeeded, int levelNeeded, int pictureOffset, int medicinId) {
        _id = id;
        _name = name;
        _roomNeeded = roomNeeded;
        _levelNeeded = levelNeeded;
        _pictureOffset = pictureOffset;
        _medicinId = medicinId;
        _durationInSeconds = convertDurationStringToSeconds(durationString);
        _basePointValue = 0;
        _epidemic = false;
        _treated = false;
    }

    public Disease(int id, String name, String durationString, int roomNeeded, int levelNeeded, int pictureOffset, int medicinId, double basePointValue) {
        _id = id;
        _name = name;
        _roomNeeded = roomNeeded;
        _levelNeeded = levelNeeded;
        _pictureOffset = pictureOffset;
        _medicinId = medicinId;
        _durationInSeconds = convertDurationStringToSeconds(durationString);
        _basePointValue = basePointValue;
        _epidemic = false;
        _treated = false;
    }

    public Disease(int id, String name, String durationString, int roomNeeded, int levelNeeded, int pictureOffset, int medicinId, int epidemicPoints, int epidemicPointsPA) {
        _id = id;
        _name = name;
        _roomNeeded = roomNeeded;
        _levelNeeded = levelNeeded;
        _pictureOffset = pictureOffset;
        _medicinId = medicinId;
        _durationInSeconds = convertDurationStringToSeconds(durationString);
        _basePointValue = 0;
        _epidemicPoints = epidemicPoints;
        _epidemicPointsPA = epidemicPointsPA;
        _epidemic = true;
        _treated = false;
    }

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getPictureOffset() {
        return _pictureOffset;
    }

    public int getDurationInSeconds() {
        if (_medicinUsed)
            return (_durationInSeconds / 2);
        else
            return _durationInSeconds;
    }

    public String getDurationAsString() {
        String duration = "";
        int durationInSeconds = getDurationInSeconds();
        int hours = durationInSeconds / 3600;
        int minutes = (durationInSeconds - hours*3600) / 60;
        int seconds = durationInSeconds - (hours*3600) - (minutes*60);
        if (hours < 10) {
            duration += "0" + hours;
        } else {
            duration += "" + hours;
        }
        duration += ":";
        if (minutes < 10) {
            duration += "0" + minutes;
        } else {
            duration += "" + minutes;
        }
        duration += ":";
        if (seconds == 0) {
            duration += "00";
        } else if (seconds < 10) {
            duration += "0" + seconds;
        } else {
            duration += "" + seconds;
        }
        return duration;
    }

    public int getRoomNeeded() {
        return _roomNeeded;
    }

    public int getLevelNeeded() {
        return _levelNeeded;
    }

    public int getMedicinId() {
        return _medicinId;
    }

    public boolean getMedicinUsed() {
        return _medicinUsed;
    }

    private int convertDurationStringToSeconds(String duration) {
        String[] durations = duration.split(":");
        return Integer.parseInt(durations[0])*3600 + Integer.parseInt(durations[1])*60 + Integer.parseInt(durations[2]);
    }

    public int getPoints(int level) {
        if (_levelNeeded > level) {
            return -1;
        }
        if (_basePointValue == 0) {
            return 0;
        }
        if (level == 1) {
            return (int)Math.floor(_basePointValue);
        } else {
            return (int)Math.floor(_basePointValue * ((level-1)*LEVELBONUS+1));
        }
    }

    public String getPointsAsString(int level) {
        if (_levelNeeded > level) {
            return "Krankheit nicht für dieses Level";
        }
        if (_basePointValue == 0) {
            if (_epidemic) {
                return _epidemicPoints + "(" + _epidemicPointsPA + ")";
            } else {
                return "Noch kein Wert vorhanden";
            }
        }
        if (_epidemic) {
            return _epidemicPoints + " (" + _epidemicPointsPA + ")";
        }
        if (level == 1) {
            return "" + (int)Math.floor(_basePointValue);
        } else {
            return "" + (int)Math.floor(_basePointValue * ((level-1)*LEVELBONUS+1));
        }
    }

    public double getBasePoints() {
        return _basePointValue;
    }

    public boolean isTreated(){
        return _treated;
    }

    public void setTreated(boolean b) {
        _treated = b;
    }
}