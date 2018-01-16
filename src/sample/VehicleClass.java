package sample;

/**
 * Created by User on 1/11/2018.
 */
public class VehicleClass extends Product {
    public String getManuFac() {
        return ManuFac;
    }

    public String getModel() {
        return Model;
    }

    public String getYear() {
        return Year;
    }

    public String getNumOwner() {
        return numOwner;
    }

    String ManuFac;
        String Model;
        String Year;
        String numOwner;

    public VehicleClass(String UserName,String VehicleName,String pric,String adres,String ManuFa,String model,String year,String numOwn,String pic,String disc)
    {
        super(UserName,VehicleName,pic,pric,disc,adres);
        ManuFac=ManuFa;
        Model=model;
        Year=year;
        numOwner=numOwn;
    }
    public String toShow() {
        return "Address : " + Address + "\nVehicle Modle : " + Model  + "\nVehicle maanufacture: " + ManuFac + "\nYear of production :" + Year + "\nAmount of owners"+ numOwner+ "\nPrice : " + getPrice() + "\n description" + Description;
    }
}
