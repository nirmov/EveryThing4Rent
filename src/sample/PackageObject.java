package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 1/9/2018.
 */
public class PackageObject {
    String PackageName;
    String UserName;
    String Price;
    String Area;
    String Type;
    int  AmountOfProducts;
    String PackageType;
    List<Product> products;
    public PackageObject(String packageName, String userName, String price, String area, String type, int amountOfProducts, String packageType) {
        PackageName = packageName;
        UserName = userName;
        Price = price;
        Area = area;
        Type = type;
        AmountOfProducts = amountOfProducts;
        PackageType = packageType;
        products=new ArrayList<>();
    }
    public void setProducts(Product p)
    {
        products.add(p);
    }

    public String getUserName() {
        return UserName;
    }

    public String getPrice() {
        return Price;
    }

    public String getArea() {
        return Area;
    }

    public String getType() {
        return Type;
    }

    public int getAmountOfProducts() {
        return AmountOfProducts;
    }

    public String getPackageType() {
        return PackageType;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getPackageName() {
        return PackageName;
    }
}