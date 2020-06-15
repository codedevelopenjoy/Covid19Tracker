
package com.covid19.tracker.entitty;

public class ItemData {

    public ItemData delta = null;
    public ItemData yesterday = null;   //ONLY IN TOTAL CASES
    public String active = "";
    public String confirmed = "";
    public String deaths = "";
    public String recovered = "";
    public String state = "";
    public String statecode = "";
    public String lastUpdated = "";
    public String critical = "";
    public String tests = "";
    public String flag = "";

    public String getRecoveryRate(){
        if(confirmed.equalsIgnoreCase("0"))
            return "0%";
        return (Integer.parseInt(recovered) * 100) / Integer.parseInt(confirmed) + "%";
    }

    public String getDeathRate(){
        if(confirmed.equalsIgnoreCase("0"))
            return "0%";
        return (Integer.parseInt(deaths) * 100) / Integer.parseInt(confirmed) + "%";
    }

}

