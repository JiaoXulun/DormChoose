package cn.edu.pku.jiaoxulun.bean;

/**
 * Created by jiaoxulun on 2017/12/24.
 */

public class ChooseInfo {
    private int num;
    private String stuid;
    private String stu1id;
    private String v1code;
    private String stu2id;
    private String v2code;
    private String stu3id;
    private String v3code;
    private int buildingNo;

    public int getNum() {
        return num;
    }

    public int getBuildingNo() {
        return buildingNo;
    }

    public String getStu1id() {
        return stu1id;
    }

    public String getStu2id() {
        return stu2id;
    }

    public String getStu3id() {
        return stu3id;
    }

    public String getStuid() {
        return stuid;
    }

    public String getV1code() {
        return v1code;
    }

    public String getV2code() {
        return v2code;
    }

    public String getV3code() {
        return v3code;
    }

    public void setBuildingNo(int buildingNo) {
        this.buildingNo = buildingNo;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setStu1id(String stu1id) {
        this.stu1id = stu1id;
    }

    public void setStu2id(String stu2id) {
        this.stu2id = stu2id;
    }

    public void setStu3id(String stu3id) {
        this.stu3id = stu3id;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public void setV1code(String v1code) {
        this.v1code = v1code;
    }

    public void setV2code(String v2code) {
        this.v2code = v2code;
    }

    public void setV3code(String v3code) {
        this.v3code = v3code;
    }

    @Override
    public String toString() {
        return "num=" + num + '&' +
                "stuid=" + stuid + '&' +
                "stu1id=" + stu1id + '&' +
                "v1code=" + v1code + '&' +
                "stu2id=" + stu2id + '&' +
                "v2code=" + v2code + '&' +
                "stu3id=" + stu3id + '&' +
                "v3code=" + v3code + '&' +
                "buildingNo=" + buildingNo;
    }
}
