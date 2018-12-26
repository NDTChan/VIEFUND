package mobile.bts.com.viefund.Model;

import java.util.ArrayList;

/**
 * BT Company
 * Created by Administrator on 3/10/2018.
 */

public class PlanModel {
    public String Name;
    public String PlanID;

    public Double PlanGroupTotal;
    public ArrayList<PlanDetail> DataDetails;

    public PlanModel(String name,Double planGroupTotal, ArrayList<PlanDetail> dataDetails) {
        Name = name;
        PlanGroupTotal = planGroupTotal;
        DataDetails = new ArrayList<>();
        DataDetails.addAll(dataDetails);
    }

    public PlanModel() {
        DataDetails = new ArrayList<>();
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Double getPlanGroupTotal() {
        return PlanGroupTotal;
    }

    public void setPlanGroupTotal(Double planGroupTotal) {
        PlanGroupTotal = planGroupTotal;
    }

}

