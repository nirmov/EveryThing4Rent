package sample;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by User on 1/9/2018.
 */
public class ResultsController {
    public ListView ListOfResults;
    protected ListProperty<String> listProperty ;
    public TextField packname;
    public TextField UserName;
    public ChoiceBox products;
    public Label pric;
    public Label LogInFirst;
    public Button Order;
    public Button SortByPrice;
    Boolean login;
    public Label PackForTrade;
    public ChoiceBox PackagesOfUser;
    public Label Available;
    public Label orderType;
    public ChoiceBox OrderType;
    HashMap<String,PackageObject> packages;
    HashMap<String,Product> productsPac;
    HashMap<String,Boolean> productsAvailabile;
    List<String> PackOfUser;
    @FXML
    public void initialize() throws SQLException {
        Available.setVisible(false);
        productsAvailabile=new HashMap<>();
        String name1=Main.User.getValue().toString();
        name1=name1.substring(name1.indexOf(" ")+1);
        if (!name1.contains("Guest"))
        {
            login=true;
            LogInFirst.setVisible(false);
        }
        else
        {
            login=false;
            Order.setDisable(true);
        }
        productsPac=new HashMap<>();
        packages=new HashMap<>();
        if (SearchController.FinalResults!=null) {
            for (PackageObject pac : SearchController.FinalResults) {
                String name=pac.getPackageName();
               ListOfResults.getItems().add(name);
               packages.put(name,pac);
               for(Product pro:pac.getProducts())
               {
                   String product=pro.getName();
                   productsPac.put(product,pro);
               }
            }
            SortByPrice.setVisible(false);
            OrderType.setValue("All");
        }
        PackagesOfUser.setVisible(false);
        PackForTrade.setVisible(false);
        PackOfUser=Main.getPackOfUser();
        initiateAvailability();
    }
    public void initiateAvailability()  throws SQLException
    {
        for(String prod: packages.keySet())
        {
            Boolean toPut=SearchController.isAvilable(prod);
           productsAvailabile.put(prod,toPut);
        }
    }
    public void mousedKlick(MouseEvent mouseEvent) throws SQLException {
        LogInFirst.setVisible(false);
        Available.setVisible(false);
        if (login)
        Order.setDisable(false);
        else
            LogInFirst.setVisible(true);
        PackagesOfUser.setVisible(false);
        PackForTrade.setVisible(false);
       String choose= ListOfResults.getSelectionModel().getSelectedItem().toString();
        if (!productsAvailabile.get(choose))
        {
            Available.setVisible(true);
            Order.setDisable(true);
        }
       PackageObject pac=packages.get(choose);
        orderType.setText("Order type : "+pac.getPackageType());
        String name1=Main.User.getValue().toString();
        name1=name1.substring(name1.indexOf(" ")+1);
        if (pac.getUserName().equals(name1))
        {
            LogInFirst.setVisible(true);
            LogInFirst.setText("You can not order your own package");
            Order.setDisable(true);
        }
        products.getItems().clear();
       packname.setText(pac.getPackageName());
       UserName.setText(pac.getUserName());
       for(Product p: pac.getProducts())
       {
           products.getItems().add(p.getName());
       }
        if (pac.getPackageType().equals("For Rent")) {
            Order.setText("Rent");
            pric.setText("Price of package per day: "+pac.getPrice()+" total price = "+Integer.parseInt(pac.getPrice())*SearchController.getamountofDays());
        }
        else if(pac.getPackageType().equals("Trade in")) {
           if (Main.getAmountOfProducts()==0&&login)
           {
               LogInFirst.setText("For Trade in you must have at least one package");
               LogInFirst.setVisible(true);
               Order.setDisable(true);
           }
           else if (login)
           {
               PackagesOfUser.getItems().clear();
               for(String pack:PackOfUser)
               {
                   PreparedStatement prep = Main.conn.prepareStatement("SELECT * from Packages WHERE PackageName = ?");
                   prep.setString(1,pack);
                   ResultSet rs = prep.executeQuery();
                   if (rs.getString("PackageType").equals("Trade in")) {
                       if (SearchController.isAvilable(pack)) {
                           PackagesOfUser.getItems().add(pack);
                       }
                   }
               }
               if (PackagesOfUser.getItems().size()==0)
               {
                   Order.setDisable(true);
               }
               PackagesOfUser.setVisible(true);
               PackForTrade.setVisible(true);
           }
            Order.setText("Trade in");
            pric.setText("Discription of other Package: "+pac.getPrice());
        }
        else
        {
            Order.setText("Lending");
            pric.setText("Deposit of package: "+pac.getPrice());
        }

    }

    public void productShow(ActionEvent actionEvent) {

    }

    public void WatchProd(ActionEvent actionEvent) {
        String ProductChoosen=products.getValue().toString();
        Product Curent= productsPac.get(ProductChoosen);
        String toShow=Curent.toShow();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText("                                     Product_Name: "+ ProductChoosen);

        DialogPane dialogPane = alert.getDialogPane();
        GridPane grid = new GridPane();
        ColumnConstraints graphicColumn = new ColumnConstraints();
        graphicColumn.setFillWidth(false);
        graphicColumn.setHgrow(Priority.NEVER);
        ColumnConstraints textColumn = new ColumnConstraints();
        textColumn.setFillWidth(true);
        textColumn.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().setAll(graphicColumn, textColumn);
        grid.setPadding(new Insets(5));

        Image image1 = new Image("file:"+Curent.Picture);
        ImageView imageView = new ImageView(image1);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        StackPane stackPane = new StackPane(imageView);
        stackPane.setAlignment(Pos.CENTER);
        grid.add(stackPane, 100, 0);

        Label headerLabel = new Label(toShow);
        headerLabel.setWrapText(true);
        headerLabel.setAlignment(Pos.CENTER_LEFT);
        headerLabel.setMaxWidth(Double.MAX_VALUE);
        headerLabel.setMaxHeight(Double.MAX_VALUE);
        grid.add(headerLabel, 1, 0);

        dialogPane.setHeader(grid);
        dialogPane.setGraphic(null);

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> System.out.println("The alert was approved"));


    }

    public void OrderTyp(ActionEvent actionEvent) {
        SortByPrice.setVisible(false);
        String chosen=OrderType.getValue().toString();
        ListOfResults.getItems().clear();
        if (SearchController.FinalResults!=null) {
            for (PackageObject pac : SearchController.FinalResults) {
                String name = pac.getPackageName();
                String Type=pac.getPackageType();
                if (chosen.equals("For Rent")&&Type.equals("For Rent")) {
                    ListOfResults.getItems().add(name);
                    SortByPrice.setVisible(true);
                }
                else if (chosen.equals("Trade in")&&Type.equals("Trade in"))
                        ListOfResults.getItems().add(name);
                else if (chosen.equals("For Lending")&&Type.equals("For Lending")) {
                    ListOfResults.getItems().add(name);
                }
                    else if (chosen.equals("All"))
                            ListOfResults.getItems().add(name);
            }
        }
    }

    public void sortByPrice(ActionEvent actionEvent) {
        HashMap<String,PackageObject> toSort= new HashMap<>();
        for (Object s: ListOfResults.getItems())
        {
            String pack=(String)s;
            toSort.put(pack,packages.get(pack));
        }
        TreeMap<String, PackageObject> sortedMap = sortMapByValue(toSort);
        HashSet<String> packshow=new HashSet<>(ListOfResults.getItems());
        ListOfResults.getItems().clear();
        for(String s: sortedMap.keySet())
        {
                ListOfResults.getItems().add(s);
        }
    }
    public TreeMap<String, PackageObject> sortMapByValue(HashMap<String,PackageObject> map){
        Comparator<String> comparator = new ValueComparator(map);
        TreeMap<String, PackageObject> result = new TreeMap<String, PackageObject>(comparator);
        result.putAll(map);
        return result;
    }

    public void order(ActionEvent actionEvent) throws SQLException {
        String status="";
        String PackName=packname.getText();
        PackageObject pac=packages.get(PackName);
        String packOrder=pac.getPackageType();
        if (packOrder.equals("Trade in"))
        {
            try {
                String otherPck = PackagesOfUser.getValue().toString();
            }
            catch (Exception e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("You must choose a product for trade in ");
                alert.setHeaderText("please choose a package from your packages");
                alert.showAndWait();
                return;
            }
        }
        String typeOfInventation=pac.getType();
        if (typeOfInventation.equals("First come First served"))
            status="confirmed";
        if (typeOfInventation.equals("Conservative"))
            status="confirmed";
        if (typeOfInventation.equals("Safe"))
            status="waiting";
            LocalDate StartDate = SearchController.getCheckIn();
            LocalDate EndDate = SearchController.getCheckOut();
            java.sql.Date StDate = java.sql.Date.valueOf(StartDate);
            java.sql.Date EnddA = java.sql.Date.valueOf(EndDate);
            if (packOrder.equals("Trade in"))
            {
                PreparedStatement prep = Main.conn.prepareStatement(
                        "insert into Inventation values (?, ?,?, ?,?,?);");
                prep.setString(1, pac.getUserName());
                prep.setDate(2, StDate);
                prep.setDate(3, EnddA);
                prep.setString(4, PackagesOfUser.getValue().toString());
                prep.setString(5, status);
                prep.setString(6, null);
                prep.execute();
                PreparedStatement prep1 = Main.conn.prepareStatement(
                        "insert into Inventation values (?, ?,?, ?,?,?);");
                prep1.setString(1, Main.getUserName());
                prep1.setDate(2, StDate);
                prep1.setDate(3, EnddA);
                prep1.setString(4, PackName);
                prep1.setString(5, status);
                prep.setString(6, null);
                prep1.execute();
                //Username1Id,Username2Id,StartDate,EndDate,Package1Id,Package2Id,status
                PreparedStatement prep2 = Main.conn.prepareStatement(
                        "insert into TradeIn values (?, ?,?, ?,?,?,?);");
                prep2.setString(1, Main.getUserName());
                prep2.setString(2, pac.getUserName());
                prep2.setDate(3, StDate);
                prep2.setDate(4, EnddA);
                prep2.setString(5, PackName);
                prep2.setString(6,PackagesOfUser.getValue().toString() );
                prep2.setString(7, status);
                prep2.execute();
            }
            else {
                PreparedStatement prep = Main.conn.prepareStatement(
                        "insert into Inventation values (?, ?,?, ?,?,?);");
                prep.setString(1, Main.getUserName());
                prep.setDate(2, StDate);
                prep.setDate(3, EnddA);
                prep.setString(4, PackName);
                prep.setString(5, status);
                prep.setString(6, "" + Integer.parseInt(pac.getPrice()) * SearchController.getamountofDays());
                prep.execute();
            }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You have Completed your order ");
        alert.setHeaderText("the satus of your order is "+ status +"\nYou received mail with all the details of your invitation");
    /*    String mail=Main.userNameToMale.get(Main.getUserName());
        if (status!="waiting") {
            Main.SendMail(mail, "Thank you for your invitation\nYour invitation status is " + status + "\nYour invitation contains 1 Package.\n" +
                    "the package contains " + pac.getAmountOfProducts() + " products" + "\nThe owner of the package is " + pac.getUserName()+"\n", "Confirmation Mail");
            Main.SendMail(Main.userNameToMale.get(pac.getUserName()),"Your package "+pac.getPackageName()+" has been invited by "+Main.getUserName()+" between "+SearchController.getCheckIn().toString()+" to "+SearchController.getCheckOut().toString(),"Invitation information");

        }
        else {
            Main.SendMail(mail, "Thank you for your invitation\nYour invitation status is " + status + "\nYour invitation contains 1 Package.\n"+
                    "the package contains " + pac.getAmountOfProducts() + " products" + "\nThe owner of the package is " + pac.getUserName() + "\nYour order will be confirmed after the package owner will aprove ur invitation.", "Confirmation Mail");
            Main.SendMail(Main.userNameToMale.get(pac.getUserName()),"Your package "+pac.getPackageName()+" has been invited by "+Main.getUserName()+" between "+SearchController.getCheckIn().toString()+" to "+SearchController.getCheckOut().toString()+"\n Please Log in to our system and confirm this invitation ","Invitation information");
        }
        */
        alert.showAndWait();
        Stage window = (Stage) Order.getScene().getWindow();
        window.close();
    }

    public void home(ActionEvent actionEvent) {
        Stage window = (Stage) Order.getScene().getWindow();
        window.close();
    }
}
class ValueComparator implements Comparator<String>{

    HashMap<String, PackageObject> map = new HashMap<String, PackageObject>();

    public ValueComparator(HashMap<String, PackageObject> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(Integer.parseInt(map.get(s2).getPrice()) >= Integer.parseInt(map.get(s1).getPrice())){
            return -1;
        }else{
            return 1;
        }
    }
}
