package mobile.bts.com.viefund.Model;

/**
 * BT Company
 * Created by Administrator on 3/10/2018.
 */

public class PlanDetail{
    public String ID;
    public String PlanID;
    public String Owner;
    public String Description;
    public String MKV;

    public PlanDetail() {
    }

    public PlanDetail(String ID,String PlanID, String owner, String description, String MKV) {
        this.ID = ID;
        this.PlanID = PlanID;
        Owner = owner;
        Description = description;
        this.MKV = MKV;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMKV() {
        return MKV;
    }

    public void setMKV(String MKV) {
        this.MKV = MKV;
    }

    public String getPlanID() {
        return PlanID;
    }

    public void setPlanID(String planID) {
        PlanID = planID;
    }
}
