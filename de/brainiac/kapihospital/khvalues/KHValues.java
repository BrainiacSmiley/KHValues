/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khvalues;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class KHValues {
    public final static int MAXFLOORS = 5;
    public final static int NUMBEROFTREATMENTROOMS = 14;
    public final static int NUMBEROFCOINROOMS = 8;
    public final static int NUMBEROFDECOROOMS = 8;
    public final static int NUMBEROFSPECIALROOMS = 3;
    public final static int NUMBEROFROOMS = 1 + NUMBEROFTREATMENTROOMS + NUMBEROFCOINROOMS + NUMBEROFDECOROOMS + NUMBEROFSPECIALROOMS;

    private BufferedImage _floorButtonsOff;
    private BufferedImage _floorButtonsOn;
    private BufferedImage _diseases15Image;
    private BufferedImage _diseases30Image;
    private BufferedImage _diseases40Image;
    private BufferedImage _diseases50Image;
    private BufferedImage _allBlueprintsImage;
    private Disease[] _allDiseases;
    private Blueprint[] _AllBlueprints;
    private ResourceBundle _RoomCaptions;
    private double[][] _VehicleCosts;
    private int[] _UpgradableRooms;
    private List<UpgradeEvaluation> _allUpgradeEvaluations;
    private List<RoomImage> _actualRoomImages;
    private Locale _actualLanguage;

    public KHValues(Locale language) {
        String path = "/de/brainiac/kapihospital/khvalues/images/";
        _actualLanguage = language;
        _RoomCaptions = ResourceBundle.getBundle("de.brainiac.kapihospital.khvalues.prop.KHValues", _actualLanguage);
        try {
            _diseases15Image = toBufferedImage(ImageIO.read(getClass().getResource(path+"d_15.2.de.png")));
            _diseases15Image = toBufferedImage(ImageIO.read(getClass().getResource(path+"d_15.2.de.png")));
            _diseases30Image = toBufferedImage(ImageIO.read(getClass().getResource(path+"d_30.2.de.png")));
            _diseases40Image = toBufferedImage(ImageIO.read(getClass().getResource(path+"d_40.2.de.png")));
            _diseases50Image = toBufferedImage(ImageIO.read(getClass().getResource(path+"d_50.2.de.png")));
            _floorButtonsOff = toBufferedImage(ImageIO.read(getClass().getResource(path+"floor_jump_off.2.gif")));
            _floorButtonsOn = toBufferedImage(ImageIO.read(getClass().getResource(path+"floor_jump_on.2.gif")));
            _allBlueprintsImage = toBufferedImage(ImageIO.read(getClass().getResource(path+"blueprint_icons.8.png")));
        } catch (IOException ex) {
            Logger.getLogger(KHValues.class.getName()).log(Level.SEVERE, null, ex);
        }
        initAllDiseases();
        initBlueprints();
        initVehicleCosts();
        initUpgradableRooms();
        initAllUpgradePossibilities();
        _actualRoomImages = new ArrayList<RoomImage>();
    }

    public int getPatientsPerLevel(int level) {
        if (level <= 4) {
            return 6;
        } else if ( level < 12) {
            return 8 + level/2;
        } else {
            return 14;
        }
    }

    public double[] getVehicleCosts(int level) {
        double[] vehicleCosts = new double[] {0, 0};
        if (level > -1 && level < 8) {
            for (int x = 0; x <= level; x++) {
                vehicleCosts[0] += _VehicleCosts[x][0];
                vehicleCosts[1] += _VehicleCosts[x][1];
            }
        }
        return vehicleCosts;
    }

    public Room getRoom(int index) {
        Room newRoom;
        if (index == 0) {
            //Behandlungsraum
            newRoom = new Room(0, _RoomCaptions.getString("room0"), 1, 3, new Dimension(3, 3));
            newRoom.setBuildingCosts(new double[] {750, 585.94, 1098.63, 1831.05}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 1, 1, 1});
        } else if (index == 1) {
            //Aufenthaltsraum
            newRoom = new Room(1, _RoomCaptions.getString("room1"), 1, 0, new Dimension(4, 3));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {100});
            newRoom.setUpgradePoints(new int[] {100});
        } else if (index == 2) {
            //Röntgenraum
            newRoom = new Room(2, _RoomCaptions.getString("room2"), 2, 3, new Dimension(4, 3));
            newRoom.setBuildingCosts(new double[] {900, 703.13, 1318.36, 2197.26}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 1, 1, 1});
        } else if (index == 3) {
            //Weihnachtsbaum
            newRoom = new Room(3, _RoomCaptions.getString("room3"), 2, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {3});
            newRoom.setUpgradePoints(new int[] {3});
        } else if (index == 4) {
            //Krankenbett links
            newRoom = new Room(4, _RoomCaptions.getString("room4"), 3, 3, new Dimension(1, 3));
            newRoom.setBuildingCosts(new double[] {150, 117.19, 219.73, 366.21}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 1, 1, 1});
        } else if (index == 499999) {
            //Krankenbett rechts
            newRoom = new Room(4, _RoomCaptions.getString("room4"), 3, 3, new Dimension(1, 3));
            newRoom.setBuildingCosts(new double[] {150, 117.19, 219.73, 366.21}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 1, 1, 1});
        } else if (index == 5) {
            //Blumenkübel
            newRoom = new Room(5, _RoomCaptions.getString("room5"), 4, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {2});
            newRoom.setUpgradePoints(new int[] {2});
        } else if (index == 6) {
            //Müllkübel
            newRoom = new Room(6, _RoomCaptions.getString("room6"), 6, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {3});
            newRoom.setUpgradePoints(new int[] {3});
        } else if (index == 7) {
            //Ultraschall
            newRoom = new Room(7, _RoomCaptions.getString("room7"), 6, 3, new Dimension(4, 3));
            newRoom.setBuildingCosts(new double[] {3000, 2343.75, 4394.52, 7324.20}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 1, 1, 4});
        } else if (index == 8) {
            //Showbühne
            newRoom = new Room(8, _RoomCaptions.getString("room8"), 7, 0, new Dimension(3, 3));
            newRoom.setBuildingCosts(new double[] {10000}, new double[] {0});
            newRoom.setUpgradePoints(new int[] {0});
        } else if (index == 9) {
            //Orthopädie
            newRoom = new Room(9, _RoomCaptions.getString("room9"), 8, 3, new Dimension(4, 4));
            newRoom.setBuildingCosts(new double[] {10000, 7812.50, 14648.40, 24414}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 2, 4, 6});
        } else if (index == 10) {
            //Telefonzelle
            newRoom = new Room(10, _RoomCaptions.getString("room10"), 8, 0, new Dimension(3, 2));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {20});
            newRoom.setUpgradePoints(new int[] {20});
        } else if (index == 11) {
            //Psychotherapie
            newRoom = new Room(11, _RoomCaptions.getString("room11"), 10, 3, new Dimension(3, 3));
            newRoom.setBuildingCosts(new double[] {5000, 3906.25, 7324.22, 12207.03}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 1, 3, 2});
        } else if (index == 12) {
            //Kaffeestube
            newRoom = new Room(12, _RoomCaptions.getString("room12"), 9, 0, new Dimension(2, 3));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {15});
            newRoom.setUpgradePoints(new int[] {15});
        } else if (index == 13) {
            //Kleiner Brunnen
            newRoom = new Room(13, _RoomCaptions.getString("room13"), 10, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {5});
            newRoom.setUpgradePoints(new int[] {5});
        } else if (index == 14) {
            //Colaautomat
            newRoom = new Room(14, _RoomCaptions.getString("room14"), 11, 0, new Dimension(2, 2));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {10});
            newRoom.setUpgradePoints(new int[] {10});
        } else if (index == 15) {
            //EKG / EEG
            newRoom = new Room(15, _RoomCaptions.getString("room15"), 11, 3, new Dimension(3, 3));
            newRoom.setBuildingCosts(new double[] {10000, 7812.50, 14648.40, 24414}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 2, 4, 6});
        } else if (index == 16) {
            //Operationssaal
            newRoom = new Room(16, _RoomCaptions.getString("room16"), 12, 3, new Dimension(4, 3));
            newRoom.setBuildingCosts(new double[] {75000, 58593.75, 109863, 183105}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 11, 27, 49});
        } else if (index == 17) {
            //Laboratorium
            newRoom = new Room(17, _RoomCaptions.getString("room17"), 13, 3, new Dimension(4, 3));
            newRoom.setBuildingCosts(new double[] {200000, 156250, 292968, 488280}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 30, 68, 133});
        } else if (index == 18) {
            //Klapperndes Gebiss
            newRoom = new Room(18, _RoomCaptions.getString("room18"), 13, 0, new Dimension(1,1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {4});
            newRoom.setUpgradePoints(new int[] {4});
        } else if (index == 19) {
            //Toiletten
            newRoom = new Room(19, _RoomCaptions.getString("room19"), 15, 0, new Dimension(3, 2));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {20});
            newRoom.setUpgradePoints(new int[] {20});
        } else if (index == 20) {
            //Putzwagen
            newRoom = new Room(20, _RoomCaptions.getString("room20"), 14, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {12});
            newRoom.setUpgradePoints(new int[] {12});
        } else if (index == 21) {
            //Dunkelkammer
            newRoom = new Room(21, _RoomCaptions.getString("room21"), 16, 3, new Dimension(4, 3));
            newRoom.setBuildingCosts(new double[] {125000, 97656.25, 183105, 305175}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 19, 43, 82});
        } else if (index == 22) {
            //Gummizelle
            newRoom = new Room(22, _RoomCaptions.getString("room22"), 18, 3, new Dimension(3, 4));
            newRoom.setBuildingCosts(new double[] {95000, 74218.75, 139159.80, 231933}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 14, 34, 63});
        } else if (index == 23) {
            //TODO get UpgradePoints
            //Tomographie
            newRoom = new Room(23, _RoomCaptions.getString("room23"), 19, 3, new Dimension(3, 4));
            newRoom.setBuildingCosts(new double[] {375000, 292968.75, 549315, 915525}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 55, 129, -1});
        } else if (index == 24) {
            //Tropenmedizin
            newRoom = new Room(24, _RoomCaptions.getString("room24"), 21, 3, new Dimension(4, 3));
            newRoom.setBuildingCosts(new double[] {500000, 390625, 732420, 1220700}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 74, 172, -1});
        } else if (index == 25) {
            //Nuklearmedizin
            newRoom = new Room(25, _RoomCaptions.getString("room25"), 27, 3, new Dimension(5, 4));
            newRoom.setBuildingCosts(new double[] {800000, 625000, 1171872, 1953120}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 118, -1, -1});
        } else if (index == 26) {
            //Zahnmedizin
            newRoom = new Room(26, _RoomCaptions.getString("room26"), 29, 3, new Dimension(2, 4));
            newRoom.setBuildingCosts(new double[] {350000, 273437.5, 512694, 854490}, new double[] {0, 0, 0, 0});
            newRoom.setUpgradePoints(new int[] {0, 52, 120, -1});
        } else if (index == 27) {
            //Burger-Bude
            newRoom = new Room(27, _RoomCaptions.getString("room27"), 5, 0, new Dimension(2, 2));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {50});
            newRoom.setUpgradePoints(new int[] {50});
        } else if (index == 28) {
            //Kirschblüte
            newRoom = new Room(28, _RoomCaptions.getString("room28"), 1, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {5});
            newRoom.setUpgradePoints(new int[] {5});
        } else if (index == 29) {
            //Lese-Sessel
            newRoom = new Room(29, _RoomCaptions.getString("room29"), 1, 0, new Dimension(2, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {12});
            newRoom.setUpgradePoints(new int[] {12});
        } else if (index == 30) {
            //Pillenwerkstatt
            newRoom = new Room(30, _RoomCaptions.getString("room30"), 1, 3, new Dimension(3, 2));
            newRoom.setBuildingCosts(new double[] {0, 50000, 500000, 0}, new double[] {0, 0, 0, 250});
            newRoom.setUpgradePoints(new int[] {0, 0, 0, 0});
        } else if (index == 31) {
            //Massagebank
            newRoom = new Room(31, _RoomCaptions.getString("room31"), 1, 0, new Dimension(2, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {0});
            newRoom.setUpgradePoints(new int[] {12});
        } else if (index == 32) {
            //Sylvester Blume
            newRoom = new Room(32, _RoomCaptions.getString("room32"), 1, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {12});
            newRoom.setUpgradePoints(new int[] {12});
        } else if (index == 33) {
            //Sylvester Blume
            newRoom = new Room(33, _RoomCaptions.getString("room33"), 1, 0, new Dimension(2, 2));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {50});
            newRoom.setUpgradePoints(new int[] {50});
        } else if (index == -2) {
            //Junk 1
            newRoom = new Room(-2, _RoomCaptions.getString("junk"), 1, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {0});
            newRoom.setDemolitionCosts(5);
            newRoom.setUpgradePoints(new int[] {0});
        } else if (index == -3) {
            //Junk 2
            newRoom = new Room(-3, _RoomCaptions.getString("junk"), 1, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {0});
            newRoom.setDemolitionCosts(10.10);
            newRoom.setUpgradePoints(new int[] {0});
        } else if (index == -4) {
            //Junk 3
            newRoom = new Room(-4, _RoomCaptions.getString("junk"), 1, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {0});
            newRoom.setDemolitionCosts(15.15);
            newRoom.setUpgradePoints(new int[] {0});
        } else if (index == -5) {
            //Junk 4
            newRoom = new Room(-5, _RoomCaptions.getString("junk"), 1, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {0});
            newRoom.setDemolitionCosts(20.20);
            newRoom.setUpgradePoints(new int[] {0});
        } else if (index == -6) {
            //Junk 5
            newRoom = new Room(-6, _RoomCaptions.getString("junk"), 1, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {0});
            newRoom.setDemolitionCosts(25.25);
            newRoom.setUpgradePoints(new int[] {0});
        } else if (index == -7) {
            //Junk 6
            newRoom = new Room(-7, _RoomCaptions.getString("junk"), 1, 0, new Dimension(1, 1));
            newRoom.setBuildingCosts(new double[] {0}, new double[] {0});
            newRoom.setDemolitionCosts(30.30);
            newRoom.setUpgradePoints(new int[] {0});
        } else {
            newRoom = new Room(-1, "", -1, -1, new Dimension(0, 0));
        }

        return newRoom;
    }

    public Image getRoomImage(int index, int level, boolean used, boolean cleaning, boolean building) {
        for (int x = 0; x < _actualRoomImages.size(); x++) {
            RoomImage actualRoomImage = _actualRoomImages.get(x);
            //Behandlungsräume (Buldining|Upgrades|Used|Cleaning)
            if (index == 0 || index == 2 || index == 7 || index == 9 || index == 11 || index == 15 || index == 16 || index == 17 || index == 21 || index == 22 || index == 23 || index == 24 || index == 25 || index == 26) {
                if (actualRoomImage.getID() == index && actualRoomImage.getLevel() == level && actualRoomImage.isUsed() == used && actualRoomImage.isCleaning() == cleaning && actualRoomImage.isBuilding() == building) {
                    return actualRoomImage.getImage();
                }
            }
            //not useable Deco
            else if (index == -7 || index == -6 || index == -5 || index == -4 || index == -3 || index == -2 || index == 3 || index == 5 || index == 6 || index == 8 || index == 13 || index == 18 || index == 20 || index == 28 || index == 32) {
                if (actualRoomImage.getID() == index) {
                    return actualRoomImage.getImage();
                }
            }
            //useable Deco
            else if (index == 1 || index == 10 || index == 12 || index == 14 || index == 19 || index == 27 || index == 29 || index == 31 || index == 33) {
                if (actualRoomImage.getID() == index && actualRoomImage.isUsed() == used) {
                    return actualRoomImage.getImage();
                }
            }
            //upgradeable + useable Rooms
            else if (index == 4 || index == 499999 || index == 30) {
                if (actualRoomImage.getID() == index && actualRoomImage.getLevel() == level && actualRoomImage.isUsed() == used) {
                    return actualRoomImage.getImage();
                }
            }
        }
        //first request of Image
        return loadRoomImage(index, level, used, cleaning, building);
    }

    public int getNumberOfBlueprints() {
        return _AllBlueprints.length;
    }

    public Blueprint[] getBluprints(int startIndex) {
        Blueprint[] Blueprints;
        if (_AllBlueprints.length - startIndex >= 16) {
            Blueprints = new Blueprint[16];
        } else {
            Blueprints = new Blueprint[_AllBlueprints.length - startIndex];
        }

        for (int x = 0; x < Blueprints.length; x++) {
            Blueprints[x] = _AllBlueprints[startIndex+x];
        }

        return Blueprints;
    }

    public int[][][] getJunk() {
        return new int[][][] {
            //Level 1
            {{-2, -4, -2, -7, -2, -6, -5, -2, -7, -4, -5, -6, -3, -5, -3, -6, -7},
             {-4, -3, -6, -2, -3, -2, -3, -6, -6, -3, -5, -2, -7, -7, -2, -4, -7},
             {-5, -7, -4, -7, -2, -2, -2, -5, -5, -6, -7, -4, -6, -5, -7, -3, -6},
             {-6, -7, -2, -3, -5, -2, -6, -7, -7, -7, -4, -4, -2, -5, -3, -2, -5},
             {-4, -3, -4, -4, -6, -7, -6, -4, -7, -5, -6, -4, -2, -5, -3, -3, -2},
             {-3, -4, -5, -4, -3, -5, -5, -4, -7, -5, -4, -2, -3, -2, -4, -7, -2},
             {-3, -4, -6, -1, -1, -1, -2, -2, -2, -2, -6, -5, -5, -3, -7, -6, -3},
             {-2, -7, -7, -1, -1, -1, -2, -2, -2, -2, -4, -2, -5, -2, -3, -3, -7},
             {-3, -3, -7, -1, -1, -1, -2, -2, -2, -2, -3, -4, -6, -3, -5, -6, -4}},
            //Level -3
            {{-5, -2, -5, -2, -7, -5, -7, -3, -2, -4, -5, -4, -7, -4, -4, -7, -6},
             {-2, -7, -2, -6, -4, -4, -5, -4, -6, -7, -2, -7, -6, -6, -7, -6, -5},
             {-7, -2, -5, -7, -5, -7, -6, -5, -2, -3, -2, -5, -3, -2, -4, -4, -2},
             {-4, -7, -2, -4, -7, -5, -2, -2, -4, -4, -2, -6, -7, -5, -4, -6, -6},
             {-2, -7, -6, -7, -3, -7, -4, -5, -4, -5, -3, -5, -5, -7, -6, -7, -3},
             {-7, -2, -4, -2, -3, -7, -4, -6, -7, -3, -4, -4, -3, -7, -7, -2, -7},
             {-5, -2, -6, -1, -1, -1, -6, -4, -7, -6, -7, -2, -6, -4, -2, -6, -7},
             {-6, -5, -7, -1, -1, -1, -2, -4, -5, -4, -2, -3, -6, -7, -6, -5, -2},
             {-2, -7, -4, -1, -1, -1, -4, -4, -7, -2, -7, -6, -4, -3, -3, -7, -2}},
            //Level -4
            {{-4, -7, -4, -5, -3, -2, -5, -3, -6, -2, -7, -6, -3, -5, -3, -5, -2},
             {-5, -4, -7, -7, -4, -4, -4, -6, -7, -4, -2, -2, -4, -5, -2, -3, -5},
             {-7, -6, -7, -2, -2, -2, -2, -3, -4, -5, -3, -6, -5, -5, -2, -5, -7},
             {-2, -2, -7, -7, -3, -5, -4, -4, -7, -3, -3, -5, -4, -6, -3, -3, -7},
             {-6, -4, -2, -2, -7, -7, -4, -7, -6, -6, -4, -4, -2, -3, -6, -3, -6},
             {-6, -2, -4, -7, -6, -5, -7, -7, -4, -7, -6, -7, -4, -4, -4, -6, -7},
             {-2, -4, -3, -1, -1, -1, -5, -5, -2, -3, -6, -4, -3, -4, -2, -2, -3},
             {-5, -2, -3, -1, -1, -1, -6, -6, -5, -5, -3, -7, -2, -7, -5, -5, -3},
             {-6, -6, -4, -1, -1, -1, -5, -3, -6, -2, -6, -3, -3, -4, -2, -4, -5}}
        };
    }

    private void initBlueprints() {
        _AllBlueprints = new Blueprint[NUMBEROFROOMS];

        _AllBlueprints[0] = new Blueprint(0, getBlueprintImage(1)); //Behandlungsraum
        _AllBlueprints[1] = new Blueprint(28, getBlueprintImage(37)); //Kirschblüte
        _AllBlueprints[2] = new Blueprint(1, getBlueprintImage(13)); //Aufenthaltsraum
        _AllBlueprints[3] = new Blueprint(2, getBlueprintImage(2)); //Röntgenraum
        _AllBlueprints[4] = new Blueprint(3, getBlueprintImage(32)); //Weihnachtsbaum
        _AllBlueprints[5] = new Blueprint(4, getBlueprintImage(5)); //Krankenbett
        _AllBlueprints[6] = new Blueprint(27, getBlueprintImage(36)); //Burger-Bude
        _AllBlueprints[7] = new Blueprint(5, getBlueprintImage(21)); //Blumenkübel
        _AllBlueprints[8] = new Blueprint(7, getBlueprintImage(8)); //Ultraschall
        _AllBlueprints[9] = new Blueprint(6, getBlueprintImage(23)); //Müllkübel
        _AllBlueprints[10] = new Blueprint(8, getBlueprintImage(26)); //Showbühne
        _AllBlueprints[11] = new Blueprint(9, getBlueprintImage(11)); //Orthopädie
        _AllBlueprints[12] = new Blueprint(10, getBlueprintImage(18)); //Telefonzelle
        _AllBlueprints[13] = new Blueprint(12, getBlueprintImage(19)); //Kaffeestube
        _AllBlueprints[14] = new Blueprint(11, getBlueprintImage(12)); //Psychotherapie
        _AllBlueprints[15] = new Blueprint(13, getBlueprintImage(22)); //Kleiner Brunnen
        _AllBlueprints[16] = new Blueprint(14, getBlueprintImage(17)); //Colaautomat
        _AllBlueprints[17] = new Blueprint(15, getBlueprintImage(4)); //EKG EEG
        _AllBlueprints[18] = new Blueprint(16, getBlueprintImage(0)); //Operationssaal
        _AllBlueprints[19] = new Blueprint(18, getBlueprintImage(25)); //Klapperndes Gebiss
        _AllBlueprints[20] = new Blueprint(17, getBlueprintImage(3)); //Laboratorium
        _AllBlueprints[21] = new Blueprint(19, getBlueprintImage(20)); //Toiletten
        _AllBlueprints[22] = new Blueprint(21, getBlueprintImage(7)); //Dunkelkammer
        _AllBlueprints[23] = new Blueprint(20, getBlueprintImage(24)); //Putzwagen
        _AllBlueprints[24] = new Blueprint(22, getBlueprintImage(9)); //Gummizelle
        _AllBlueprints[25] = new Blueprint(23, getBlueprintImage(6)); //Tomographie
        _AllBlueprints[26] = new Blueprint(24, getBlueprintImage(14)); //Tropenmedizin
        _AllBlueprints[27] = new Blueprint(25, getBlueprintImage(15)); //Nuklearmedizin
        _AllBlueprints[28] = new Blueprint(26, getBlueprintImage(16)); //Zahnmedizin
        _AllBlueprints[29] = new Blueprint(29, getBlueprintImage(27)); //Lese-Sessel
        _AllBlueprints[30] = new Blueprint(30, getBlueprintImage(38)); //Pillenwerkstatt
        _AllBlueprints[31] = new Blueprint(31, getBlueprintImage(30)); //Massagebank
        _AllBlueprints[32] = new Blueprint(32, getBlueprintImage(40)); //Sylvester-Blume
        _AllBlueprints[33] = new Blueprint(33, getBlueprintImage(41)); //Entlaufender Elefant
    }

    private Image getBlueprintImage(int index) {
        int xOffset = index * 91;
        int yOffset = 0;
        return new ImageIcon(_allBlueprintsImage.getSubimage(xOffset, yOffset, 91, 70)).getImage();
    }

    private void initVehicleCosts() {
        _VehicleCosts = new double[][] {{0, 0}, {1000, 0}, {15000, 0}, {50000, 0}, {150000, 0}, {250000, 0}, {0, 50}, {0, 150}};
    }

    private void initUpgradableRooms() {
    	_UpgradableRooms = new int[] {0, 2, 4, 499999, 7, 9, 11, 15, 16, 17, 21, 22, 23, 24, 25, 26};
    }

    private void initAllUpgradePossibilities() {
        _allUpgradeEvaluations = new ArrayList<UpgradeEvaluation>();
        
        for (int x = 0; x < _UpgradableRooms.length; x++) {
            Room actualRoom = getRoom(_UpgradableRooms[x]);
            for (int y = 1; y <= actualRoom.getMaxUpgradeLevel(); y++) {
                _allUpgradeEvaluations.add(new UpgradeEvaluation(actualRoom.getID(), actualRoom.getName(), y-1, y, actualRoom.getBuildingCosts(y)[0], actualRoom.getUpgradePoints(y)));
            }
            //Upgrade von 0 auf 2
             _allUpgradeEvaluations.add(new UpgradeEvaluation(actualRoom.getID(), actualRoom.getName(), 0, 2, actualRoom.getBuildingCosts(1)[0]+actualRoom.getBuildingCosts(2)[0], actualRoom.getUpgradePoints(1)+actualRoom.getUpgradePoints(2)));
            //Upgrade von 1 auf 3
             _allUpgradeEvaluations.add(new UpgradeEvaluation(actualRoom.getID(), actualRoom.getName(), 1, 3, actualRoom.getBuildingCosts(2)[0]+actualRoom.getBuildingCosts(3)[0], actualRoom.getUpgradePoints(2)+actualRoom.getUpgradePoints(3)));
            //Upgrade von 0 auf 3
             _allUpgradeEvaluations.add(new UpgradeEvaluation(actualRoom.getID(), actualRoom.getName(), 0, 3, actualRoom.getBuildingCosts(1)[0]+actualRoom.getBuildingCosts(2)[0]+actualRoom.getBuildingCosts(3)[0], actualRoom.getUpgradePoints(1)+actualRoom.getUpgradePoints(2)+actualRoom.getUpgradePoints(3)));
        }
        Comparator<UpgradeEvaluation> comparator = new UpgradeEvaluationComparator();
        Collections.sort(_allUpgradeEvaluations, comparator);
    }

    public UpgradeEvaluation getBestUpgradeOption(int roomID, int actualUpgradeLevel) {
        for (int x = 0; x < _allUpgradeEvaluations.size(); x++) {
            if (_allUpgradeEvaluations.get(x).getRoomID() == roomID && _allUpgradeEvaluations.get(x).getForWichLevel() == actualUpgradeLevel) {
                return _allUpgradeEvaluations.get(x);
            }
        }
        return null;
    }

    public List<UpgradeEvaluation> getAllUpgradeOptions(int roomID, int actualUpgradeLevel) {
        List<UpgradeEvaluation> allOptions = new ArrayList<UpgradeEvaluation>();
        Room roomToGetUpgradeOptionsFrom = this.getRoom(roomID);
        for (int x = 0; x < actualUpgradeLevel; x++) {
            roomToGetUpgradeOptionsFrom.upgradeRoom();
        }
        while(getBestUpgradeOption(roomToGetUpgradeOptionsFrom.getID(), roomToGetUpgradeOptionsFrom.getUpgradeLevel()) != null) {
            UpgradeEvaluation actualEvaluation = getBestUpgradeOption(roomToGetUpgradeOptionsFrom.getID(), roomToGetUpgradeOptionsFrom.getUpgradeLevel());
            allOptions.add(actualEvaluation);
            for (int x = 0; x < actualEvaluation.getNumberOfUpgrades(); x++) {
                roomToGetUpgradeOptionsFrom.upgradeRoom();
            }
        }
        return allOptions;
    }

    public ImageIcon getDiseasesImage(int solution, int pictureOffset) {
        int x = pictureOffset % 10;
        int y = pictureOffset / 10;
        int xOffset = 0;
        int yOffset = 0;
        if (solution == 15) {
            xOffset = x * 15;
            yOffset = y * 15;
            return new ImageIcon(_diseases15Image.getSubimage(xOffset, yOffset, 15, 15));
        } else if (solution == 30) {
            xOffset = x * 30;
            yOffset = y * 30;
            return new ImageIcon(_diseases30Image.getSubimage(xOffset, yOffset, 30, 30));
        } else if (solution == 40) {
            xOffset = x * 40;
            yOffset = y * 40;
            return new ImageIcon(_diseases40Image.getSubimage(xOffset, yOffset, 40, 40));
        } else if (solution == 50) {
            xOffset = x * 50;
            yOffset = y * 50;
            return new ImageIcon(_diseases50Image.getSubimage(xOffset, yOffset, 50, 50));
        } else {
            xOffset = x * 50;
            yOffset = y * 50;
            return new ImageIcon(_diseases50Image.getSubimage(xOffset, yOffset, 50, 50));
        }
    }

    public Image getFloorImage(int level, boolean selected) {
        int yOffset = (MAXFLOORS - level) * 16;
        if (selected) {
            return _floorButtonsOn.getSubimage(0, yOffset, 16, 16);
        } else {
            return _floorButtonsOff.getSubimage(0, yOffset, 16, 16);
        }
    }

    public static BufferedImage toBufferedImage(Image image) {
        //wenn das Bild schon ein BufferedImage ist
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
            e.printStackTrace(System.out);
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        // Copy image to buffered image
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    private static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace(System.out);
        }
        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        return cm.hasAlpha();
    }

    public Patient parsePatientFromHTML(String input) {
        Pattern p;
        Matcher matcher;
        String[] parsedValues = new String[9];
        //Name
        p = Pattern.compile("large; \">(.*?)</div><div id=");
        matcher = p.matcher(input);
        matcher.find();
        parsedValues[0] = matcher.group(1);

        //Krankheiten
        p = Pattern.compile("<div id=\"s[1-6]name\" class=\"ref_detsickinfo\" style=\"left: 55px; position: absolute; top: 0px; font-weight: bold; \">(.*?)</div>");
        matcher = p.matcher(input);
        int illnessCounter = 1;
        while (matcher.find()) {
            parsedValues[illnessCounter] = matcher.group(1);
            illnessCounter++;
        }

        //Preis
        p = Pattern.compile("([0-9]+,[0-9]{2}) hT");
        matcher = p.matcher(input);

        int priceCounter = 7;
        while (matcher.find()) {
            parsedValues[priceCounter] = matcher.group(1);
            priceCounter++;
        }
        parsedValues[7] = parsedValues[7].replaceAll(",", ".");
        parsedValues[8] = parsedValues[8].replaceAll(",", ".");

        //Create Patient
        int numberOfDiseases = 0;
        for (int i = 1; i < 7; i++) {
            if (parsedValues[i] != null) {
                numberOfDiseases++;
            }
        }
        int[] diseasesArray = new int[numberOfDiseases];
        for (int i = 1; i < 1 + numberOfDiseases; i++) {
            diseasesArray[i - 1] = getDiseaseIDFromName(parsedValues[i]);
        }
        return new Patient(parsedValues[0], diseasesArray, Double.parseDouble(parsedValues[7]), Double.parseDouble(parsedValues[8]), _actualLanguage);
    }

    private int getDiseaseIDFromName(String diseasseName) {
        for (Disease actualDisease : _allDiseases) {
            if (actualDisease.getName().equalsIgnoreCase(diseasseName)) {
                return actualDisease.getId();
            }
        }
        return -1;
    }

    public Disease getDiseaseById(int id) {
        for (Disease actualDisease : _allDiseases) {
            if (actualDisease.getId() == id) {
                return new Disease(actualDisease.getId(), actualDisease.getName(), actualDisease.getDurationAsString(), actualDisease.getRoomNeeded(), actualDisease.getLevelNeeded(), actualDisease.getPictureOffset(), actualDisease.getMedicinId(), actualDisease.getBasePoints());
            }
        }
        return null;
    }

    public Disease[] getAllDiseases() {
        return _allDiseases;
    }

    private void initAllDiseases() {
        _allDiseases = new Disease[123];
	_allDiseases[0] = new Disease(0, "Nasenflügelakne", "00:10:00", 1, 1, 3, 0, 3.0);
	_allDiseases[1] = new Disease(1, "Brecheritis", "00:10:00", 1, 1, 2, 1, 3.0);
	_allDiseases[2] = new Disease(2, "Gebrochener Arm", "01:20:00", 2, 2, 0, 2, 11.0);
	_allDiseases[3] = new Disease(3, "Schürfwunde", "00:30:00", 1, 3, 5, 3, 6.82);
	_allDiseases[4] = new Disease(4, "Schädelbrummen", "02:40:00", 1, 4, 4, 4, 27.61);
	_allDiseases[5] = new Disease(5, "Gebrochenes Bein", "02:40:00", 2, 4, 1, 5, 27.6);
	_allDiseases[6] = new Disease(6, "Zwergfellentzündung", "02:40:00", 1, 5, 11, 6, 28.6);
	_allDiseases[7] = new Disease(7, "Wechselbalgjahre", "02:00:00", 3, 6, 25, 7, 21.7);
	_allDiseases[8] = new Disease(8, "Segelohrentzündung", "02:00:00", 1, 6, 10, 8, 21.7);
	_allDiseases[9] = new Disease(9, "Fränkisches Wurzelfieber", "01:30:00", 2, 6, 17, 9, 18.15);
	_allDiseases[10] = new Disease(10, "Galliensteine", "01:45:00", 3, 7, 43, 10, 22.7);
	_allDiseases[11] = new Disease(11, "Luftpocken", "02:20:00", 3, 7, 29, 11, 26.5);
	_allDiseases[12] = new Disease(12, "Aufgerollte Fußnägel", "02:30:00", 4, 8, 47, 12, 31.635);
	_allDiseases[13] = new Disease(13, "Knorpelverknautschung", "02:30:00", 4, 8, 65, 13, 31.635);
	_allDiseases[14] = new Disease(14, "Eierkopf", "03:00:00", 3, 9, 112, 14, 37.0);
	_allDiseases[15] = new Disease(15, "Rummelplatzkrankheit", "02:40:00", 5, 10, 7, 15, 33.65);
	_allDiseases[16] = new Disease(16, "Faulenzia", "02:20:00", 5, 10, 8, 16, 29.51);
	_allDiseases[17] = new Disease(17, "Herzklappenklappern", "02:30:00", 6, 11, 70, 17, 34.65);
	_allDiseases[18] = new Disease(18, "Herzschmerzgeschnulze", "03:00:00", 6, 11, 66, 18, 38.875);
	_allDiseases[19] = new Disease(19, "Furunkelfunkeln", "04:00:00", 7, 12, 18, 19, 54.55);
	_allDiseases[20] = new Disease(20, "Zungenverknotung", "03:00:00", 7, 12, 38, 20, 40.0);
	_allDiseases[21] = new Disease(21, "Wasserallergie", "03:00:00", 5, 13, 33, 21, 40.8);
	_allDiseases[22] = new Disease(22, "Nierenkiesel", "03:30:00", 8, 13, 60, 22, 50.48);
	_allDiseases[23] = new Disease(23, "Stinkekäsefuß", "03:20:00", 8, 14, 20, 23, 46.6);
	_allDiseases[24] = new Disease(24, "Nagelbettwackeln", "02:30:00", 4, 15, 48, 24, 38.64);
	_allDiseases[25] = new Disease(25, "Schlaflosigkeit", "01:40:00", 9, 16, 35, 25, 28.16);
	_allDiseases[26] = new Disease(26, "Milchbart", "04:45:00", 3, 16, 93, 26, 75.1);
	_allDiseases[27] = new Disease(27, "Koffeinhypersensibilität", "01:40:00", 9, 16, 44, 27, 28.18);
	_allDiseases[28] = new Disease(28, "Brett vorm Kopf", "04:00:00", 4, 17, 79, 28, 59.45);
	_allDiseases[29] = new Disease(29, "Arbeitsallergie", "00:30:00", 10, 18, 34, 29, 21.85);
	_allDiseases[30] = new Disease(30, "Marserianerimpfung", "02:05:00", 10, 18, 30, 30, 37.5);
	_allDiseases[31] = new Disease(31, "Lykantropitis", "06:00:00", 9, 19, 114, 31, 1499, 1799);
	_allDiseases[32] = new Disease(32, "Rot-Grün-Fieber", "05:00:00", 11, 19, 28, 32, 78.09);
	_allDiseases[33] = new Disease(33, "Haarbruch", "05:10:00", 11, 19, 15, 33, 84.0);
	_allDiseases[34] = new Disease(34, "Flederhöhlenkopf", "03:00:00", 9, 19, 77, 34, 47.0);
	_allDiseases[35] = new Disease(35, "Dicker Kopf", "03:30:00", 8, 19, 84, 35, 56.5);
	_allDiseases[36] = new Disease(36, "Emosyndrom", "06:00:00", 5, 20, 116, 36, 999, 1199);
	_allDiseases[37] = new Disease(37, "Silberblick", "02:20:00", 6, 20, 78, 37, 39.5);
	_allDiseases[38] = new Disease(38, "Vampitis", "06:00:00", 10, 21, 115, 38, 1799, 2159);
	_allDiseases[39] = new Disease(39, "Frosch im Hals", "03:40:00", 12, 21, 50, 39, 58.5);
	_allDiseases[40] = new Disease(40, "Sonnenbankbrand", "16:00:00", 12, 21, 14, 40, 354.6);
	_allDiseases[41] = new Disease(41, "Drehwurm", "02:00:00", 12, 22, 86, 41, 37.7);
	_allDiseases[42] = new Disease(42, "Zweckenbefall", "06:00:00", 11, 22, 90, 42, 99.36);
	_allDiseases[43] = new Disease(43, "Darmvergilbung", "04:45:00", 7, 23, 76, 43, 82.0);
	_allDiseases[44] = new Disease(44, "Dauermüdigkeit", "01:00:00", 6, 23, 36, 44, 29.2);
	_allDiseases[45] = new Disease(45, "Schnarcheritis", "02:00:00", 9, 23, 83, 45, 38.4);
	_allDiseases[46] = new Disease(46, "Heimweh", "04:00:00", 5, 24, 87, 46, 66.5);
	_allDiseases[47] = new Disease(47, "Barthaarverzwirbelung", "03:20:00", 3, 24, 98, 47, 56.65);
	_allDiseases[48] = new Disease(48, "viereckige Augen", "04:00:00", 10, 24, 81, 48, 66.5);
	_allDiseases[49] = new Disease(49, "Bad-Hair-Day-Syndrom", "05:10:00", 9, 25, 55, 49, 90.0);
	_allDiseases[50] = new Disease(50, "Kussmundatem", "05:00:00", 8, 25, 21, 50, 84.1);
	_allDiseases[51] = new Disease(51, "Sauklaue", "03:45:00", 6, 25, 82, 51, 67.5);
	_allDiseases[52] = new Disease(52, "Rapunzelsyndrom", "02:00:00", 5, 26, 95, 52, 41.7);
	_allDiseases[53] = new Disease(53, "Frostbeulen", "03:35:00", 8, 26, 97, 53, 63.3);
	_allDiseases[54] = new Disease(54, "Schlitzohr", "02:50:00", 7, 26, 106, 54, 54.0);
	_allDiseases[55] = new Disease(55, "Sturmfrisur", "08:00:00", 9, 27, 89, 55, 145.55);
	_allDiseases[56] = new Disease(56, "Innenohr voller Hammer, Amboss und Steigbügel", "24:00:00", 13, 27, 68, 56, 639.0);
	_allDiseases[57] = new Disease(57, "Rosengürtel", "12:00:00", 13, 27, 53, 57, 244.0);
	_allDiseases[58] = new Disease(58, "Pferdefuß", "04:00:00", 4, 28, 102, 58, 70.5);
	_allDiseases[59] = new Disease(59, "Holopocken", "06:00:00", 13, 28, 117, 59, 1499, 1799);
	_allDiseases[60] = new Disease(60, "Dickdarmdiätsyndrom", "06:00:00", 14, 29, 67, 60, 106.3);
	_allDiseases[61] = new Disease(61, "Gehirndurchzug", "03:20:00", 6, 29, 99, 61, 61.5);
	_allDiseases[62] = new Disease(62, "Spülwurm", "03:40:00", 14, 29, 27, 62, 66.5);
	_allDiseases[63] = new Disease(63, "Knoblauchfahne", "04:22:00", 9, 30, 110, 63, 83.4);
	_allDiseases[64] = new Disease(64, "Strohkopf", "06:00:00", 14, 30, 120, 64);
	_allDiseases[65] = new Disease(65, "Dauerschnupfen", "04:00:00", 12, 31, 6, 65, 73.143);
	_allDiseases[66] = new Disease(66, "Zahnschmerzen", "03:00:00", 14, 31, 84, 66);
	_allDiseases[67] = new Disease(67, "Fleuchhusten", "01:30:00", 13, 31, 22, 67, 42.86);
	_allDiseases[68] = new Disease(68, "Hummeln im Hintern", "04:00:00", 12, 32, 103, 68);
	_allDiseases[69] = new Disease(69, "Stumpfe Zähne", "04:00:00", 14, 32, 101, 69, 74.4);
	_allDiseases[70] = new Disease(70, "Grippeimpfung", "01:30:00", 1, 33, 9, 70);
	_allDiseases[71] = new Disease(71, "Venenverwesung", "05:00:00", 11, 33, 12, 71);
	_allDiseases[72] = new Disease(72, "Brüllfroschrülpsen", "05:00:00", 10, 33, 92, 72);
	_allDiseases[73] = new Disease(73, "Feuermal", "04:00:00", 11, 34, 96, 73);
	_allDiseases[74] = new Disease(74, "Ringfingerschwur", "06:00:00", 6, 34, 121, 74);
	_allDiseases[75] = new Disease(75, "Ohrschneckenauswanderung", "06:00:00", 12, 35, 13, 75);
	_allDiseases[76] = new Disease(76, "Zombiefizitis", "06:00:00", 4, 36, 118, 76);
	_allDiseases[77] = new Disease(77, "Tomaten auf den Augen", "04:45:00", 13, 36, 80, 77);
	_allDiseases[78] = new Disease(78, "Hippiephilie", "06:00:00", 1, 37, 123, 78);
	_allDiseases[79] = new Disease(79, "(Ohr-)Schmalzlocke", "07:00:00", 9, 37, 19, 79);
	_allDiseases[80] = new Disease(80, "Lampenfieber", "05:00:00", 4, 38, 109, 80);
	_allDiseases[81] = new Disease(81, "Currywurstjieper", "02:00:00", 13, 38, 111, 81);
	_allDiseases[82] = new Disease(82, "Würfelhusten", "08:00:00", 5, 39, 26, 82);
	_allDiseases[83] = new Disease(83, "Hitzewallung", "12:00:00", 9, 39, 31, 83);
	_allDiseases[84] = new Disease(84, "Bierbauch", "07:20:00", 13, 40, 91, 84);
	_allDiseases[85] = new Disease(85, "Tollwutfiffi", "06:00:00", 8, 40, 119, 85);
	_allDiseases[86] = new Disease(86, "Mumpitzimpfung", "01:30:00", 5, 41, 32, 86);
	_allDiseases[87] = new Disease(87, "Kopflausbuben", "14:00:00", 8, 41, 37, 87);
	_allDiseases[88] = new Disease(88, "Glatzitis", "08:00:00", 12, 42, 113, 88);
	_allDiseases[89] = new Disease(89, "Tränensäcke", "05:00:00", 11, 42, 105, 89);
	_allDiseases[90] = new Disease(90, "Fingernagelbruch", "04:30:00", 7, 43, 39, 90);
	_allDiseases[91] = new Disease(91, "Kampfadern", "16:00:00", 6, 43, 40, 91);
	_allDiseases[92] = new Disease(92, "Holzkopf", "02:00:00", 5, 44, 107, 92);
	_allDiseases[93] = new Disease(93, "Stinkebefall", "08:00:00", 8, 44, 104, 93);
	_allDiseases[94] = new Disease(94, "Pobackenflattern", "03:30:00", 13, 45, 41, 94);
	_allDiseases[95] = new Disease(95, "Schleimbeutelüberproduktion", "07:00:00", 8, 45, 42, 95);
	_allDiseases[96] = new Disease(96, "Feuchte Aussprache", "08:00:00", 13, 46, 108, 96);
	_allDiseases[97] = new Disease(97, "Zwei linke Hände", "12:00:00", 7, 46, 100, 97);
	_allDiseases[98] = new Disease(98, "Koffeinmangel", "01:30:00", 6, 47, 45, 98);
	_allDiseases[99] = new Disease(99, "Fußfliegenpilz", "04:00:00", 12, 47, 46, 99);
	_allDiseases[100] = new Disease(100, "Wurzelekzem", "06:00:00", 12, 48, 122, 100);
	_allDiseases[101] = new Disease(101, "Zahnstein", "03:45:00", 14, 48, 88, 101);
	_allDiseases[102] = new Disease(102, "Rotes Kniekehlchen", "06:00:00", 5, 49, 49, 102);
	_allDiseases[103] = new Disease(103, "Lippenblütlerwarze", "04:20:00", 12, 50, 16, 103);
	_allDiseases[104] = new Disease(104, "Flatterndes Nabelpiercing", "04:00:00", 7, 51, 52, 104);
	_allDiseases[105] = new Disease(105, "Rundrücken", "12:00:00", 10, 52, 94, 105);
	_allDiseases[106] = new Disease(106, "Peitschenfloh", "04:00:00", 10, 53, 56, 106);
	_allDiseases[107] = new Disease(107, "Gurgelhupf", "18:00:00", 3, 54, 51, 107);
	_allDiseases[108] = new Disease(108, "Leberkäserkältung", "05:00:00", 13, 55, 58, 108);
	_allDiseases[109] = new Disease(109, "Seekrankheit", "10:00:00", 10, 56, 23, 109);
	_allDiseases[110] = new Disease(110, "Kiefernzapfenverrenkung", "06:00:00", 14, 57, 61, 110);
	_allDiseases[111] = new Disease(111, "Salzfässchen", "08:00:00", 4, 58, 54, 111);
	_allDiseases[112] = new Disease(112, "Milzbierüberdruss", "06:00:00", 10, 59, 63, 112);
	_allDiseases[113] = new Disease(113, "Schieldrüsensyndrom", "12:00:00", 2, 60, 57, 113);
	_allDiseases[114] = new Disease(114, "Katzenjammern", "03:30:00", 14, 61, 69, 114);
	_allDiseases[115] = new Disease(115, "Muskelkätzchen", "12:00:00", 4, 62, 59, 115);
	_allDiseases[116] = new Disease(116, "Akkuschrauberpunkturlöcher", "07:30:00", 2, 63, 72, 116);
	_allDiseases[117] = new Disease(117, "Lungenseifenblasen", "04:00:00", 11, 64, 62, 117);
	_allDiseases[118] = new Disease(118, "Solarplexusdämmerung", "04:00:00", 9, 65, 75, 118);
	_allDiseases[119] = new Disease(119, "Tomatenmarkgefüllte Knochen", "08:00:00", 7, 66, 64, 119);
	_allDiseases[120] = new Disease(120, "Einhornhautentzündung", "06:30:00", 11, 67, 71, 120);
	_allDiseases[121] = new Disease(121, "Zwirbeldrüsenverzwirbelung", "05:00:00", 8, 68, 73, 121);
	_allDiseases[122] = new Disease(122, "gestörtes Wurzelchakra", "08:00:00", 3, 69, 74, 122);
    }

    public boolean isFloorUpgradeable(int floor) {
        switch (floor) {
            case 0 :
                return true;
            case 1 :
                return true;
            case 2 :
                return true;
            case 3 :
                return true;
            case 4 :
                return false;
            default :
                return false;
        }
    }

    private Image loadRoomImage(int id, int level, boolean used, boolean cleaning, boolean building) {
        String roomImageNameToLoad = "";
        //Construction Pictures
        /*if (building) {
            //3x3
            if (id == 0 || id == 11 || id == 15) {
                roomImageNameToLoad = "building/building_3x3.gif";
            }
            //3x4
            else if (id == 2 || id == 7 || id == 16 || id == 17 || id == 21 || id == 24) {
                roomImageNameToLoad = "building/building_3x4.gif";
            }
            //4x4
            else if (id == 9) {
                roomImageNameToLoad = "building/building_4x4.gif";
            }
            //4x3
            else if (id == 22 || id == 23) {
                roomImageNameToLoad = "building/building_4x3.gif";
            }
            //4x5
            else if (id == 25) {
                roomImageNameToLoad = "building/building_4x5.gif";
            }
            //4x2
            else if (id == 26) {
                roomImageNameToLoad = "building/building_4x2.gif";
            }
        } else {*/
            //Behandlungsräume mit Level 0-3, Used 0-3 and Cleaning 0-3
            if (id == 0 || id == 2 || id == 7 || id == 9 || id == 11 || id == 15 || id == 16 || id == 17 || id == 21 || id == 22 || id == 23 || id == 24 || id == 25 || id == 26) {
                String actionLevel = "2";
                if (used) {
                    actionLevel = "3";
                }/* else if (cleaning) {
                    actionLevel = "6";
                }*/
                roomImageNameToLoad = "treatment/treatment_" + id + "_" + level + "_" + actionLevel + ".gif";
            }
            //non useable Deco
            else if (id == 3 || id == 5 || id == 6 || id == 8 || id == 13 || id == 18 || id == 20 || id == 28 || id == 32) {
                roomImageNameToLoad = "deco/deco_" + id + ".gif";
            }
            //Junk
            else if (id == -7 || id == -6 || id == -5 || id == -4 || id == -3 || id == -2) {
                roomImageNameToLoad = "deco/deco_" + id + ".png";
            }
            //useable Deco
            else if (id == 1 || id == 10 || id == 12 || id == 14 || id == 19 || id == 27 || id == 29 || id == 31 || id == 33) {
                String actionLevel = "2";
                if (used) {
                    actionLevel = "3";
                }
                roomImageNameToLoad = "deco/deco_" + id + "_" + actionLevel + ".gif";
            }
            //upgradeable + useable Rooms
            else if (id == 4 || id == 499999 || id == 30) {
                String actionLevel = "2";
                if (used) {
                    actionLevel = "3";
                }
                roomImageNameToLoad = "room_" + id + "_" + level + "_" + actionLevel + ".gif";
            }
        /*}*/
        try {
            Image image = new ImageIcon(getClass().getResource("/de/brainiac/kapihospital/khvalues/images/rooms/"+roomImageNameToLoad)).getImage();
            _actualRoomImages.add(new RoomImage(image, id, level, used, cleaning, building));
            return image;
        } catch (Exception ex) {
            System.out.println("ID: " + id + " | Level: " + level);
            System.out.println("ImageName: " + "images/rooms/" + roomImageNameToLoad);
            System.out.println("URL: " + getClass().getResource("/images/rooms/"+roomImageNameToLoad).toString());
            Logger.getLogger(KHValues.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ImageIcon().getImage();
    }

    public void clearRoomImageList() {
        _actualRoomImages.clear();
    }

    public int getNumberOfRoomsAllowed(int roomId) {
        switch (roomId) {
            case 4:
                return 12;
            case 8:
                return 1;
            case 31:
                return 1;
            default:
                return -1;
        }
    }

    public boolean hasRoomPremiumLock(int roomID) {
        switch (roomID) {
            case 1:
                return true;
            default:
                return false;
        }
    }

    public boolean isRoomCoinsRoom(int roomID) {
        switch (roomID) {
            case 1:
            case 3:
            case 5:
            case 6:
            case 10:
            case 12:
            case 13:
            case 14:
            case 18:
            case 19:
            case 20:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
                return true;
            default:
                return false;
        }
    }
}