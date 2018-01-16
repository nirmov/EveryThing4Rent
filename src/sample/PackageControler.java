package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 12/28/2017.
 */
public class PackageControler {
    public Label TypeLabel;
    public TextArea Discription;
    public Label  discountLabel;
    public Label pricePackage;
    public ChoiceBox InfoPack;
    public ChoiceBox TypeOption;
    public Button Addpackage;
    public static int amountofproducts;
    public static int price;
    public Label ProductsAmount;
    public Label ProductsPrice;
    public TextField nameofPackage;
    public TextField Discount;
    static StringProperty amount;
    boolean Edit=false;
    public ChoiceBox Area;
    public ChoiceBox TypeOfPackage;
    static StringProperty pricebind;
    static List<Product> ProductsToAdd;
    @FXML
    public void initialize() throws SQLException {
        if (!(EditProducts.packageChoose==null))
        {
            Edit=true;
            String name=Main.User.getValue().toString();
            name=name.substring(name.indexOf(" ")+1);
            String sql = "select * from Packages WHERE PackageName = '" + EditProducts.packageChoose + "' AND Username = '" + name + "'";
            ResultSet rs = Main.stat.executeQuery(sql);
            //PackageName,Username,price,area,type,amountOfProducts,PackageType
            while (rs.next()) {
                String packName=rs.getString("PackageName");
                InfoPack.setValue(rs.getString("PackageType"));
                amount = new SimpleStringProperty(rs.getString("amountOfProducts"));
                amountofproducts=Integer.parseInt(rs.getString("amountOfProducts"));
                String prices=rs.getString("price");
                nameofPackage.setText(packName);
                Area.setValue(rs.getString("area"));
                String typ=rs.getString("type");
                TypeOfPackage.setValue(typ);
                if (InfoPack.getValue().equals("Trade in"))
                {
                    int pric=0;
                    String sql1 = "select * from RealEstate WHERE packageName = '" + packName + "' AND Username = '" + name + "'";
                    ResultSet rs1 = Main.stat.executeQuery(sql1);
                    while (rs1.next()) {
                        String cur=rs1.getString("price");
                        pric+= Integer.parseInt(cur);
                    }
                    pricebind = new SimpleStringProperty(""+pric);
                    Discription.setText(prices);
                    price=pric;
                }
                else {
                    pricebind = new SimpleStringProperty(prices);
                    price=Integer.parseInt(prices);
                }
            }
        }
        else {
            amount = new SimpleStringProperty("0");
            pricebind = new SimpleStringProperty("0");
            price=0;
            amountofproducts=0;
        }
        ProductsAmount.textProperty().bind(amount);
        ProductsPrice.textProperty().bind(pricebind);
        ProductsToAdd = new ArrayList<>();
        Discription.setVisible(false);
        discountLabel.setVisible(false);
        Discount.setVisible(false);
        pricePackage.setVisible(false);
        ProductsPrice.setVisible(false);
        if (Edit)
        TypeOp();
    }

    public void RealEstateFunc () throws IOException {
        Stage RealEstate = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("RealEstate.fxml"));
        Scene s = new Scene(root2, 720, 400);
        s.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
        RealEstate.setScene(s);
        RealEstate.show();
    }
    public void PetsFunc () throws IOException {
        Stage Pets = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("Pets.fxml"));
        Scene s = new Scene(root2, 720, 400);
        s.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
        Pets.setScene(s);
        Pets.show();
    }
    public void   SecondHandFunc() throws IOException {
        Stage SecondHand = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("SecondHand.fxml"));
        Scene s = new Scene(root2, 720, 400);
        s.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
        SecondHand.setScene(s);
        SecondHand.show();
    }
    public void   VehiclerFunc() throws IOException {
        Stage Vehicle = new Stage();
        Parent root2 = FXMLLoader.load(getClass().getResource("Vehicle.fxml"));
        Scene s = new Scene(root2, 720, 400);
        s.getStylesheets().add(getClass().getResource("LoginStyle.css").toExternalForm());
        Vehicle.setScene(s);
        Vehicle.show();
    }
    public void home()
    {

        Stage window = (Stage) Addpackage.getScene().getWindow();
        window.close();
    }
    public void AddPackage () throws SQLException {
        if (!Edit) {
            if (amountofproducts == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cant finish Adding a Package");
                alert.setHeaderText("Please add 1 Product Before adding a Package");
                alert.showAndWait();
                return;
            }
            try {
                if (Main.packagesNames.contains(nameofPackage.getText()))
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Please choose other name to your package ");
                    alert.setHeaderText("this name is already exists in our system");
                    alert.showAndWait();
                    return;

                }
                String ChoosenType = InfoPack.getValue().toString();
                String pri = "";
                if (ChoosenType.equals("For Rent")) {
                    String discount = Discount.getText();
                    String price = ProductsPrice.getText();
                    int pric = Integer.parseInt(price) * (100 - Integer.parseInt(discount)) / 100;
                    pri = "" + pric;
                } else if(ChoosenType.equals("Trade in")) {
                    pri = Discription.getText();
                }
                else
                    pri=ProductsPrice.getText();
                String PackageName = nameofPackage.getText();
                String area = Area.getValue().toString();
                String type = TypeOfPackage.getValue().toString();
                String amount = ProductsAmount.getText();
                String name = Main.User.getValue().toString();
                name = name.substring(name.indexOf(" ") + 1);
                WriteToProduct(Integer.parseInt(ProductsAmount.getText()), PackageName);
                PreparedStatement prep = Main.conn.prepareStatement(
                        "insert into Packages values (?, ?,?, ?,?, ?,?);");
                prep.setString(1, PackageName);
                prep.setString(2, name);
                prep.setString(3, pri);
                prep.setString(4, area);
                prep.setString(5, type);
                prep.setString(6, amount);
                prep.setString(7, ChoosenType);
                prep.addBatch();
                prep.executeBatch();
                System.out.println("insert to Packages 1");
                Main.packagesNames.add(PackageName);
                Stage window = (Stage) Addpackage.getScene().getWindow();
                window.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cant finish Adding a Package");
                alert.setHeaderText("You must fill all of the fields for Adding Package");
                alert.showAndWait();
            }
        }
        else
        {
            //PackageName,Username,price,area,type,amountOfProducts,PackageType
            String ChoosenType = InfoPack.getValue().toString();
            String PackageName = nameofPackage.getText();
            String area = Area.getValue().toString();
            String type = TypeOfPackage.getValue().toString();
            String amount = ProductsAmount.getText();
            String name = Main.User.getValue().toString();
            name = name.substring(name.indexOf(" ") + 1);
            String pri="";
            if (ChoosenType.equals("For Rent")) {
                String discount = Discount.getText();
                String price = ProductsPrice.getText();
                int pric = Integer.parseInt(price) * (100 - Integer.parseInt(discount)) / 100;
                System.out.println("Price is  " + pric);
                pri = "" + pric;
            } else if (ChoosenType.equals("Trade in")) {
                pri = Discription.getText();
            }
            else
            {
                pri=ProductsPrice.getText();
            }
            PreparedStatement statement1 = Main.conn.prepareStatement("UPDATE Packages SET price = ? , area = ? , type = ? , amountOfProducts = ? , PackageType = ?  WHERE PackageName=?");
            statement1.setString(1,pri);
            statement1.setString(2,area);
            statement1.setString(3,type);
            statement1.setString(4,amount);
            statement1.setString(5,ChoosenType);
            statement1.setString(6,PackageName);
            statement1.executeUpdate();
            if (this.ProductsToAdd.size()>0)
                WriteToProduct(Integer.parseInt(ProductsAmount.getText()), PackageName);
            Stage window = (Stage) Addpackage.getScene().getWindow();
            window.close();
        }

    }
    public static void WriteToProduct(int amount,String packagename) throws SQLException {
        for (int i=0;i<ProductsToAdd.size();i++) {
            Product p=ProductsToAdd.get(i);
            if (p instanceof RealEstateProduct)
                writeRealEstate((RealEstateProduct)p,packagename);
            else
                if (p instanceof VehicleClass)
                    writeToVehicle((VehicleClass)p,packagename);
            else
                if (p instanceof Pets)
                    writeToPets((Pets)p,packagename);
            else
                if (p instanceof SecondHand)
                    writeToSecondHand((SecondHand) p,packagename);

        }
    }
    private static void writeToSecondHand(SecondHand p, String packagename) throws SQLException {
        PreparedStatement prep = Main.conn.prepareStatement(
                "insert into SecondHand values (?,?,?,?,?,?,?,?,?,?);");
        //(username, productName,ProductType,manufacturer,YearOfManufacture,address,picture,description,packageName,price);")
        p.getManufacture();
        prep.setString(1, p.UserName);
        prep.setString(2, p.getName());
        prep.setString(3, p.getType());
        prep.setString(4, p.getManufacture());
        prep.setString(5, p.getManYear());
        prep.setString(6, p.getAddress());
        prep.setString(7, p.getPicture());
        prep.setString(8, p.Description);
        prep.setString(9, packagename);
        prep.setString(10, p.getPrice());

        prep.addBatch();
        prep.executeBatch();

    }
     private static void writeToPets(Pets p, String packagename) throws SQLException {
        //(Username, petName,petAge,address,picture,type,jender,description,packageName)
        PreparedStatement prep = Main.conn.prepareStatement(
                "insert into Pets values (?,?,?,?,?,?,?,?,?,?);");
        prep.setString(1, p.UserName);
        prep.setString(2, p.petName);
        prep.setInt(3, p.petAge);
        prep.setString(4, p.Address);
        prep.setString(5, p.Picture);
        prep.setString(6, p.Type);
        prep.setString(7, p.Jender);
        prep.setString(8, p.Description);
        prep.setString(9, packagename);
        prep.setString(10, p.getPrice());

        prep.addBatch();
        prep.executeBatch();
    }
    //(UserName, VehicleName,price,address,ManuFa,model,year,picture,packageName,disc,numOwn)
    private static void writeToVehicle(VehicleClass p, String packagename) throws SQLException {
        PreparedStatement prep = Main.conn.prepareStatement(
                "insert into Vehicle values (?, ?,?, ?,?, ?,?,?,?,?,?);");
        prep.setString(1, p.UserName);
        prep.setString(2, p.Name);
        prep.setString(3, p.getPrice());
        prep.setString(4, p.Address);
        prep.setString(5, p.ManuFac);
        prep.setString(6, p.Model);
        prep.setString(7, p.Year);
        prep.setString(8, p.getPicture());
        prep.setString(9,packagename);
        prep.setString(10, p.getDescription());
        prep.setString(11, p.numOwner);
        prep.addBatch();
        prep.executeBatch();
    }

    public static void writeRealEstate(RealEstateProduct real,String PackageName) throws SQLException {
        PreparedStatement prep = Main.conn.prepareStatement(
                "insert into RealEstate values (?, ?,?, ?,?, ?,?,?,?);");
        prep.setString(1, real.UserName);
        prep.setString(2, real.getProcudtName());
        prep.setString(3, real.getPrice());
        prep.setString(4, real.Address);
        prep.setInt(5, Integer.parseInt(real.AmountOfPeople));
        prep.setString(6, real.Picture);
        prep.setString(7, real.Priority);
        prep.setString(8, real.Type);
        prep.setString(9, PackageName);
        prep.addBatch();
        prep.executeBatch();
    }

    public void TypeFunc()
    {
        String type=TypeOfPackage.getValue().toString();
        if (type.equals("Conservative"))
        {
            TypeLabel.setVisible(true);
            TypeOption.setVisible(true);
            TypeLabel.setText("Min Level Of Rank:");
            TypeOption.getItems().add("1");     TypeOption.getItems().add("2");     TypeOption.getItems().add("3");
            TypeOption.getItems().add("4");     TypeOption.getItems().add("5");     TypeOption.getItems().add("6");
            TypeOption.getItems().add("7");     TypeOption.getItems().add("8");     TypeOption.getItems().add("9");
            TypeOption.getItems().add("10");
        }
        else
        {
            TypeLabel.setVisible(false);
            TypeOption.setVisible(false);
        }
    }
    public void TypeOp()
    {
        String type=InfoPack.getValue().toString();
        Discription.setVisible(false);
        discountLabel.setVisible(false);
        Discount.setVisible(false);
        pricePackage.setVisible(false);
        ProductsPrice.setVisible(false);
        if (type.equals("For Rent"))
        {
            discountLabel.setText("Discount Percent");
            discountLabel.setVisible(true);
            Discount.setVisible(true);
            pricePackage.setVisible(true);
            ProductsPrice.setVisible(true);
            pricePackage.setText("Curent price of package");
        }
        else if (type.equals("Trade in"))
        {
            Discription.setVisible(true);
            discountLabel.setText("Information of Other Package");
            discountLabel.setVisible(true);
        }
        else
            if (type.equals("For Lending"))
            {
                pricePackage.setVisible(true);
                ProductsPrice.setVisible(true);
                pricePackage.setText("Curent deposit of package");
            }
    }


}
