package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by User on 12/28/2017.
 */
public class SignUpController {
    @FXML
    public Button Finish_btn;
    @FXML
    public javafx.scene.control.TextField Username_Input;
    String pathToImg=null;
    @FXML public javafx.scene.control.TextField Firstname_Input;
    @FXML public javafx.scene.control.TextField Lastname_Input;
    @FXML public javafx.scene.control.PasswordField Password_Input;
    @FXML public javafx.scene.control.TextField Email_Input;
    @FXML public javafx.scene.control.TextField Phone_Input;
    @FXML public javafx.scene.control.TextField Paypal_Input;
    @FXML public javafx.scene.control.TextField Address_Input;
    public void imgButton()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter fileExtensions2 = new FileChooser.ExtensionFilter("JPEG", "*.png");
        fileChooser.getExtensionFilters().add(fileExtensions);
        fileChooser.getExtensionFilters().add(fileExtensions2);
        File selected_img = fileChooser.showOpenDialog(null);
        pathToImg=selected_img.getAbsolutePath();
    }
    public void Signup() throws SQLException {
        Boolean s=true;
        String userName = Username_Input.getText();
        String pass = Password_Input.getText();
        String firstName = Firstname_Input.getText();
        String lastName = Lastname_Input.getText();
        String email = Email_Input.getText();
        String address = Address_Input.getText();
        String phone_num = Phone_Input.getText();
        String pp_usr = Paypal_Input.getText();
        //  String date=
        if (userName.equals("")||pass.equals("")||firstName.equals("")||lastName.equals("")||address.equals("")||email.equals("")||userName.equals("")||phone_num.equals("")||firstName.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cant finish Sign up");
            alert.setHeaderText("You must fill all of the fields for Sign up");
            alert.showAndWait();
            return;
        }
        String Message=new String("");
        if (!email.contains("@")&&!email.contains(".")) {
            s = false;
            Message+=" Invalid Email";
        }
        if (!Character.isDigit(phone_num.charAt(0))) {
            s = false;
            Message+=" invalid Phone Number";
        }
        if (Main.Usersnames.contains(userName))
        {
            s=false;
            Message=new String("User name allready exists , please select new User name");
        }
        if (Main.Emails.contains(email))
        {
            s=false;
            Message=new String("Email allready exists , please select new Email");
        }

        if (s) {
            // conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            //  Statement  stat = conn.createStatement();
            PreparedStatement prep = Main.conn.prepareStatement(
                    "insert into people values (?, ?,?, ?,?, ?,?,?);");

            prep.setString(1, userName);
            prep.setString(2, pass);
            prep.setString(3, firstName);
            prep.setString(4, lastName);
            prep.setString(5, email);
            prep.setString(6, address);
            prep.setString(7, phone_num);
            prep.setString(8, pp_usr);
            prep.addBatch();
            prep.executeBatch();
            //send confirmation mail
            Thread thread = new Thread(new Runnable()
            {
                public void run()
                {
                    Main.SendMail(email,firstName+" , Welcome to Everething for Rent! \n","Your Registration has been Succefull");
                }
            });
            thread.start();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Welcome "+firstName);
            alert.setHeaderText("Thank you and enjoy your Renting \n A confirmation mail has been sent to your email");
            alert.showAndWait();
            Main.Emails.add(email);
            Main.Usersnames.add(userName);
            Stage window = (Stage)Finish_btn.getScene().getWindow();
            window.close();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cant finish Sign up");
            alert.setHeaderText(Message);
            alert.showAndWait();
        }
        // conn.close();
    }
    public void home()
    {
        Stage window = (Stage) Finish_btn.getScene().getWindow();
        window.close();
    }
}
