package cn.edu.pku.jiaoxulun.bean;

/**
 * Created by jiaoxulun on 2017/12/21.
 */

public class RoomInfo {
    private int building_5;
    private int building_13;
    private int building_14;
    private int building_8;
    private int building_9;

    public int getBuilding_5() {
        return building_5;
    }

    public int getBuilding_13() {
        return building_13;
    }

    public int getBuilding_14() {
        return building_14;
    }

    public int getBuilding_8() {
        return building_8;
    }

    public int getBuilding_9() {
        return building_9;
    }

    public void setBuilding_5(int building_5) {
        this.building_5 = building_5;
    }

    public void setBuilding_13(int building_13) {
        this.building_13 = building_13;
    }

    public void setBuilding_14(int building_14) {
        this.building_14 = building_14;
    }

    public void setBuilding_8(int building_8) {
        this.building_8 = building_8;
    }

    public void setBuilding_9(int building_9) {
        this.building_9 = building_9;
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "5号楼剩余床位数量='" + building_5 + '\'' +
                ", 13号楼剩余床位数量='" + building_13 + '\'' +
                ", 14号楼剩余床位数量='" + building_14 + '\'' +
                ", 8号楼剩余床位数量='" + building_8 + '\'' +
                ", 9号楼剩余床位数量='" + building_9 + '\'' +
                '}';
    }
}
