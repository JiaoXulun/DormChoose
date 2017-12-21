package cn.edu.pku.jiaoxulun.bean;

/**
 * Created by jiaoxulun on 2017/12/19.
 */

public class StudentInfo {
    private String name;
    private String studentid;
    private String gender;
    private String vcode;
    private String room;
    private String building;
    private String location;
    private String grade;

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getBuilding() {
        return building;
    }

    public String getGender() {
        return gender;
    }

    public String getGrade() {
        return grade;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getVcode() {
        return vcode;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "studentid='" + studentid + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", vcode='" + vcode + '\'' +
                ", room='" + room + '\'' +
                ", building='" + building + '\'' +
                ", location='" + location + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
