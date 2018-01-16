package sample;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.lang.model.element.PackageElement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;
import static sample.EditProducts.choose;

/**
 * Created by User on 1/9/2018.
 */
public class SearchController {
    @FXML
    public ChoiceBox Area;
    public ChoiceBox ProductType;
    public ChoiceBox FirstSearch;
    public ChoiceBox SecondSearch;
    public DatePicker CheckOut;
    public DatePicker Checkin;
    public Label FirstLabel;
    static LocalDate checkout;
    static LocalDate checkin;
    public Label SecondLabel;
    public Button home;
    static HashSet<PackageObject>  FinalResults;
    @FXML
    public void initialize() throws SQLException {
        FirstLabel.setVisible(false);
        SecondSearch.setVisible(false);
        SecondLabel.setVisible(false);
        FirstSearch.setVisible(false);
        FinalResults=null;
    }
    // case of choosing Product
    public void ProductChose(ActionEvent actionEvent) {
        FirstLabel.setVisible(true);
        SecondSearch.setVisible(true);
        SecondLabel.setVisible(true);
        FirstSearch.setVisible(true);
        FirstSearch.getItems().clear();
        SecondSearch.getItems().clear();
        String Product=ProductType.getValue().toString();
        if (Product.equals("RealEstate"))
        {
            FirstLabel.setText("Type Of Estate");
            SecondLabel.setText("Min amount of people");
            SecondSearch.getItems().add("1");SecondSearch.getItems().add("2");SecondSearch.getItems().add("3");
            SecondSearch.getItems().add("4");SecondSearch.getItems().add("5");SecondSearch.getItems().add("6");
            SecondSearch.getItems().add("7");SecondSearch.getItems().add("8");SecondSearch.getItems().add("more");
            FirstSearch.getItems().add("Office");FirstSearch.getItems().add("Function hall");FirstSearch.getItems().add("Apartment");
        }
        else if (Product.equals("Second Hand"))
        {
            SecondLabel.setVisible(false);
            SecondSearch.setVisible(false);
            FirstLabel.setText("Type of product");
            FirstSearch.getItems().add("Kitchen");FirstSearch.getItems().add("Living Room");FirstSearch.getItems().add("Office");
            FirstSearch.getItems().add("Garden");FirstSearch.getItems().add("Bedroom");FirstSearch.getItems().add("Toilet");
        }
        else if (Product.equals("Pets"))
        {
            FirstLabel.setText("Type of pet");
            SecondLabel.setText("Age");
            SecondSearch.getItems().add("1");SecondSearch.getItems().add("2");SecondSearch.getItems().add("3");
            SecondSearch.getItems().add("4");SecondSearch.getItems().add("5");SecondSearch.getItems().add("6");
            SecondSearch.getItems().add("7");SecondSearch.getItems().add("8");SecondSearch.getItems().add("more");
            FirstSearch.getItems().add("Dog");FirstSearch.getItems().add("Cat");FirstSearch.getItems().add("Fish Aquarium");
            FirstSearch.getItems().add("Hamster");FirstSearch.getItems().add("Rabbit");FirstSearch.getItems().add("Turtle");
        }
        else if(Product.equals("Vehicle"))
        {
            FirstLabel.setText("Type of vehicle");
            SecondLabel.setText("Min year of prodcution");
            SecondSearch.getItems().add("1999");SecondSearch.getItems().add("2000");SecondSearch.getItems().add("2001");
            SecondSearch.getItems().add("2002");SecondSearch.getItems().add("2003");SecondSearch.getItems().add("2004");
            SecondSearch.getItems().add("2005");SecondSearch.getItems().add("2006");SecondSearch.getItems().add("2007");
            SecondSearch.getItems().add("2008");SecondSearch.getItems().add("2009");SecondSearch.getItems().add("2010");
            SecondSearch.getItems().add("2011");SecondSearch.getItems().add("2012");SecondSearch.getItems().add("2013");
            SecondSearch.getItems().add("2014");SecondSearch.getItems().add("2015");SecondSearch.getItems().add("2016");
            SecondSearch.getItems().add("2017");SecondSearch.getItems().add("2018");
            FirstSearch.getItems().add("Mazda");FirstSearch.getItems().add("Subaru");FirstSearch.getItems().add("Honda");
            FirstSearch.getItems().add("Mercedes");FirstSearch.getItems().add("Hyundai");FirstSearch.getItems().add("BMW");
            FirstSearch.getItems().add("Audi");FirstSearch.getItems().add("Toyota");FirstSearch.getItems().add("Jeep");
        }
    }

    public void searchFunc(ActionEvent actionEvent) {
        try {
            String startDate = Checkin.getValue().toString();
            String endDate = CheckOut.getValue().toString();
            String prodcuct = ProductType.getValue().toString();
            String area = Area.getValue().toString();
            String First = FirstSearch.getValue().toString();
            String second="6";
            if (!prodcuct.equals("Second Hand"))
                second = SecondSearch.getValue().toString();
            checkin=Checkin.getValue();
            checkout=CheckOut.getValue();
            if (second=="more")
                second="9";
            HashSet<String> FromSearch=getPackage(startDate,endDate,prodcuct,area,First,Integer.parseInt( second));
            FinalResults=getResults(FromSearch);
            if (getamountofDays()<=0)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid dates");
                alert.setHeaderText("please try again with valid date");
                alert.showAndWait();
                return;
            }
            if (FinalResults.size()>0) {
                Stage Result = new Stage();
                Parent root2 = FXMLLoader.load(getClass().getResource("Results.fxml"));
                Scene s = new Scene(root2, 720, 400);
                s.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
                Result.setScene(s);
                Result.show();
                Stage window = (Stage) home.getScene().getWindow();
                window.close();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("There is no result");
                alert.setHeaderText("please try again with another search");
                alert.showAndWait();
                return;
            }

        }
        catch (Exception e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cant finish Searching");
            alert.setHeaderText("You Must Fill All Categoriez For Making A Search");
            alert.showAndWait();
            return;
        }
    }
    private HashSet<PackageObject> getResults(HashSet<String> fromSearch) throws SQLException {
        HashSet<PackageObject> toReturn=new HashSet<>();
        for(String PackageName: fromSearch)
        {
            PackageObject pac=createPackage(PackageName);
            //getting product from RealEstate
            ResultSet rs1 = Main.stat.executeQuery("SELECT * from RealEstate WHERE packageName = '"+PackageName+"'" );
            while (rs1.next())
            {
                Product p=new RealEstateProduct(rs1.getString("address"),rs1.getString("Username"),rs1.getString("id"),""+rs1.getInt("amountofpeople"),rs1.getString("picture"),rs1.getString("price"),rs1.getString("type"),rs1.getString("Priority"),"");
                pac.setProducts(p);
            }
            ResultSet rs2 = Main.stat.executeQuery("SELECT * from Vehicle WHERE packageName = '"+PackageName+"'" );
            while (rs2.next())
            {
                Product p=new VehicleClass(rs2.getString("UserName"),rs2.getString("VehicleName"),rs2.getString("price"),""+rs2.getString("address"),rs2.getString("ManuFa"),rs2.getString("model"),rs2.getString("year"),rs2.getString("numOwn"),rs2.getString("picture"),rs2.getString("disc"));
                pac.setProducts(p);
            }
            ResultSet rs3 = Main.stat.executeQuery("SELECT * from Pets WHERE packageName = '"+PackageName+"'" );
            while (rs3.next())
            {
                Product p=new Pets(rs3.getString("Username"),rs3.getString("petName"),rs3.getInt("petAge"),""+rs3.getInt("address"),rs3.getString("picture"),rs3.getString("price"),rs3.getString("type"),rs3.getString("jender"),rs3.getString("description"));
                pac.setProducts(p);
            }
            ResultSet rs4 = Main.stat.executeQuery("SELECT * from SecondHand WHERE packageName = '"+PackageName+"'" );
            while (rs4.next())
            {
                Product p=new SecondHand(rs4.getString("Username"),rs4.getString("productName"),rs4.getString("picture"),""+rs4.getInt("price"),rs4.getString("description"),rs4.getString("address"),rs4.getString("manufacturer"),rs4.getString("ProductType"),rs4.getString("YearOfManufacture"));
                pac.setProducts(p);
            }
            toReturn.add(pac);
        }
        return toReturn;
    }

    private PackageObject createPackage(String packageName) throws SQLException
    {
        PackageObject pac=null;
        ResultSet rs1 = Main.stat.executeQuery("SELECT * from Packages WHERE PackageName = '"+packageName+"'" );
        while (rs1.next())
        {
            String UserName= rs1.getString("Username");
            String Price= rs1.getString("price");
            String Area= rs1.getString("area");
            String Type= rs1.getString("type");
            int  AmountOfProducts= Integer.parseInt( rs1.getString("amountOfProducts"));
            String PackageType= rs1.getString("PackageType");
            pac=new PackageObject(packageName,UserName,Price,Area,Type,AmountOfProducts,PackageType);
        }
        return pac;
    }

    public HashSet<String> getPackage(String startDate, String endDate, String Product, String area, String First, int Second) throws SQLException
    {
        String PackageName="";
        PreparedStatement prep = Main.conn.prepareStatement("SELECT * from RealEstate WHERE type = ? AND amountofpeople >= ?");
        prep.setString(1,First);prep.setInt(2,Second);
        ResultSet rs = prep.executeQuery();
        HashSet<String> toReturn =new HashSet<>();
        while (rs.next()) {
            PackageName = rs.getString("packageName");
            if (!toReturn.contains(PackageName))
                toReturn.add(PackageName);
        }
        PreparedStatement prep1 = Main.conn.prepareStatement("SELECT * from Vehicle WHERE ManuFa = ? AND year >= ?");
        prep1.setString(1,First);prep1.setString(2,""+Second);
        ResultSet rs1 = prep1.executeQuery();
        while (rs1.next()) {
            PackageName = rs1.getString("packageName");
            if (!toReturn.contains(PackageName))
                toReturn.add(PackageName);
        }
        PreparedStatement prep2 = Main.conn.prepareStatement("SELECT * from Pets WHERE type = ? AND petAge = ?");
        prep2.setString(1,First);prep2.setInt(2,Second);
        ResultSet rs2 = prep2.executeQuery();
        while (rs2.next()) {
            PackageName = rs2.getString("packageName");
            if (!toReturn.contains(PackageName))
                toReturn.add(PackageName);
        }
        PreparedStatement prep3 = Main.conn.prepareStatement("SELECT * from SecondHand WHERE ProductType = ? ");
        prep3.setString(1,First);
        ResultSet rs3 = prep3.executeQuery();
        while (rs3.next()) {
            PackageName = rs3.getString("packageName");
            if (!toReturn.contains(PackageName))
                toReturn.add(PackageName);
        }
        pack(area,toReturn);
        return toReturn;
    }
    public void pack(String area,HashSet<String> toCheck) throws SQLException {
        String are;
        HashSet<String> toRemove=new HashSet<>();
        for (String PackageName : toCheck) {
            String sql1 = "select * from Packages WHERE PackageName = '" + PackageName + "'";
            ResultSet rs1 = Main.stat.executeQuery(sql1);
            while (rs1.next()) {
                are=rs1.getString("area");
                if (!are.equals(area))
                {
                    toRemove.add(PackageName);
                }
            }
        }
        for(String s : toRemove)
        {
            toCheck.remove(s);
        }
    }
    public static LocalDate getCheckIn()
    {
        return checkin;
    }
    public static LocalDate getCheckOut()
    {
        return checkout;
    }
    public static int getamountofDays()
    {
        long daysBetween = DAYS.between(checkin, checkout);
        return (int)daysBetween;
    }
    public static Boolean isAvilable(String packName) throws SQLException {
        Boolean ans=true;
        HashSet<String> toReturn=new HashSet<>();
        PreparedStatement prep = Main.conn.prepareStatement("SELECT * FROM Inventation WHERE ( StartDate <= ? AND EndDate >= ? ) OR ( StartDate <= ? AND EndDate >= ? ) OR ( StartDate >= ? AND EndDate <= ? ) ");
        java.sql.Date StDate = java.sql.Date.valueOf( checkin );
        java.sql.Date EnddA = java.sql.Date.valueOf( checkout );
        prep.setDate(1,StDate);
        prep.setDate(2,StDate);
        prep.setDate(3,EnddA);
        prep.setDate(4,EnddA);
        prep.setDate(5,StDate);
        prep.setDate(6,EnddA);
        ResultSet rs=prep.executeQuery();
        while (rs.next())
        {
           if (rs.getString("PackageName").equals(packName)) {
               if (!rs.getString("status").equals("waiting"))
                  ans = false;
           }
        }
        return ans;
    }

    public void home(ActionEvent actionEvent) {
        Stage window = (Stage) home.getScene().getWindow();
        window.close();
    }
}
