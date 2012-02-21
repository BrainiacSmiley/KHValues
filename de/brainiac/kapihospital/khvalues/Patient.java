/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khvalues;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;

public class Patient extends Object {
    private int _id;
    private String _patientName;
    private String _dob, _pob, _job, _height, _weight, _hobbies;
    private Disease[] _diseases;
    private double _minPrice, _maxPrice;
    private KHValues _KHValues;
    
    public Patient(String name, int[] diseases, double minPrice, double maxPrice, Locale language) {
        _KHValues = new KHValues(language);
        _patientName = name;
        _dob = "01.01.2011";
        _pob = "MeinGeburtsort";
        _job = "MeinJob";
        _height = "180 cm";
        _weight = "75 kg";
        _hobbies = "MeineHobbies";
        _diseases = new Disease[diseases.length];
        for (int x = 0; x < _diseases.length; x++) {
            _diseases[x] = _KHValues.getDiseaseById(diseases[x]);
        }
        _minPrice = minPrice;
        _maxPrice = maxPrice;
    }

    public Patient(int id, String name, int[] diseases, boolean[] treatedDiseases, double minPrice, double maxPrice, Locale language) {
        _KHValues = new KHValues(language);
        _id = id;
        _patientName = name;
        _dob = "01.01.2011";
        _pob = "MeinGeburtsort";
        _job = "MeinJob";
        _height = "180 cm";
        _weight = "75 kg";
        _hobbies = "MeineHobbies";
        _diseases = new Disease[diseases.length];
        for (int x = 0; x < _diseases.length; x++) {
            _diseases[x] = _KHValues.getDiseaseById(diseases[x]);
            _diseases[x].setTreated(treatedDiseases[x]);
        }
        _minPrice = minPrice;
        _maxPrice = maxPrice;        
    }

    public int getID() {
        return _id;
    }

    public void setID(int i) {
        _id = i;
    }

    public String getName() {
        return _patientName;
    }

    public Disease[] getDiseases() {
        return _diseases;
    }

    public int getNumberOfDiseases() {
        int numberOfDiseases = 0;
        for (Disease d : _diseases) {
            if (!d.isTreated()) {
                numberOfDiseases++;
            }
        }
        return numberOfDiseases;
    }

    public double getMinPrice() {
        return _minPrice;
    }

    public double getMaxPrice() {
        return _maxPrice;
    }

    @Override
    public boolean equals(Object o) {
        return o != null 
            && o.getClass() == getClass()
            && equals((Patient)o);
    }
 
    private boolean equals(Patient other){
        return _patientName.equalsIgnoreCase(other._patientName)
            && Arrays.equals(_diseases, other._diseases)
            && _minPrice == other._minPrice
            && _maxPrice == other._maxPrice
            && hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this._patientName != null ? this._patientName.hashCode() : 0);
        hash = 47 * hash + Arrays.hashCode(this._diseases);
        hash = 47 * hash + (int) (Double.doubleToLongBits(this._minPrice) ^ (Double.doubleToLongBits(this._minPrice) >>> 32));
        hash = 47 * hash + (int) (Double.doubleToLongBits(this._maxPrice) ^ (Double.doubleToLongBits(this._maxPrice) >>> 32));
        return hash;
    }

    public String getDOB() {
        return _dob;
    }

    public String getPOB() {
        return _pob;
    }

    public String getJob() {
        return _job;
    }

    public String getHeight() {
        return _height;
    }

    public String getWeight() {
        return _weight;
    }

    public String getHobbies() {
        return _hobbies;
    }

    public String getPoints() {
        DecimalFormat pointsFormat = new DecimalFormat("#,##0 Punkte");
        double points = 0.0;
        
        for (int x = 0; x < _diseases.length; x++) {
            points += _diseases[x].getBasePoints();
        }
        
        points += _diseases.length-1;
        
        return pointsFormat.format(Math.floor(points));
    }
}